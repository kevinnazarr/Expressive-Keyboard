package com.expressive.keyboard

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.min

// ============================================================================
// BAHASA & TATA LETAK KEYBOARD: BAHASA INDONESIA & BAHASA INGGRIS
// Mendukung pengetikan Bahasa Indonesia (QWERTY ID) dan Bahasa Inggris (QWERTY EN).
// ============================================================================

enum class Composer { NONE, HANGUL, TELEX, PINYIN, KANA }

@Immutable
class KeyboardLanguage(
    val id: String,
    /** Kode singkat pada tombol ganti bahasa: "ID" atau "EN". */
    val label: String,
    /** Label nama pada spasi. */
    val spaceLabel: String,
    /** Nama tampilan di pengaturan. */
    val title: String,
    /** Locale untuk input suara dan terjemahan. */
    val locale: String,
    val rows: List<List<String>>,
    val shiftMap: Map<String, String> = emptyMap(),
    val accents: Map<String, List<String>> = emptyMap(),
    val dict: String? = id,
    val hasCase: Boolean = true,
    val composer: Composer = Composer.NONE,
    val autoCorrect: Boolean = true,
    val rtl: Boolean = false,
    val fromPlugin: String? = null
) {
    override fun equals(other: Any?): Boolean = other is KeyboardLanguage && other.id == id && other.fromPlugin == fromPlugin
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = id

    /** Nama untuk pengaturan dalam bahasa yang aktif (Indonesia/Inggris). */
    fun displayTitle(): String = when (id) {
        "id" -> if (I18n.isEnglish) "Indonesian" else "Bahasa Indonesia"
        "en" -> if (I18n.isEnglish) "English" else "Bahasa Inggris"
        else -> title
    }

    /** Seluruh huruf pada tata letak. */
    val letters: Set<String> by lazy { rows.flatten().toSet() }

    /** Posisi tombol keyboard untuk kalkulasi kesalahan ketik. */
    val keyPositions: Map<Char, Pair<Float, Float>> by lazy {
        val m = HashMap<Char, Pair<Float, Float>>()
        val widest = rows.maxOfOrNull { it.size } ?: 10
        rows.forEachIndexed { r, row ->
            val shift = (widest - row.size) / 2f
            row.forEachIndexed { c, k -> if (k.length == 1) m[k[0]] = r.toFloat() to (c + shift) }
        }
        m
    }

    companion object {
        val ID: KeyboardLanguage get() = Languages.ID
        val EN: KeyboardLanguage get() = Languages.EN
        val RU: KeyboardLanguage get() = Languages.ID // fallback aman
    }
}

object Languages {
    private fun r(s: String): List<String> =
        if (' ' in s) s.split(' ').filter { it.isNotEmpty() }
        else buildList { var i = 0; while (i < s.length) { val cp = s.codePointAt(i); add(String(Character.toChars(cp))); i += Character.charCount(cp) } }

    private fun acc(s: String): Map<String, List<String>> =
        s.split(' ').filter { it.isNotEmpty() }.associate { t -> val p = r(t); p.first() to p.drop(1) }

    private fun sh(s: String): Map<String, String> =
        s.split(' ').filter { it.isNotEmpty() }.associate { t -> val p = r(t); p[0] to p.drop(1).joinToString("") }

    private fun L(
        id: String, label: String, space: String, title: String, locale: String, rows: List<String>,
        accents: String = "", shift: String = "", dict: String? = id, hasCase: Boolean = true,
        composer: Composer = Composer.NONE, autoCorrect: Boolean = true, rtl: Boolean = false
    ) = KeyboardLanguage(
        id, label, space, title, locale, rows.map(::r), sh(shift), acc(accents), dict, hasCase, composer, autoCorrect, rtl
    )

    private val QWERTY = listOf("qwertyuiop", "asdfghjkl", "zxcvbnm")

    val ID = L("id", "ID", "Indonesia", "Bahasa Indonesia", "id-ID", QWERTY, dict = "id")
    val EN = L("en", "EN", "English", "English", "en-US", QWERTY, dict = "en")
    val RU: KeyboardLanguage get() = ID

    val builtIn: List<KeyboardLanguage> = listOf(ID, EN)

    /** Bahasa tambahan dari plugin (opsional). */
    var extra by mutableStateOf<List<KeyboardLanguage>>(emptyList())

    val all: List<KeyboardLanguage> get() = builtIn + extra.filter { e -> builtIn.none { it.id == e.id } }

    fun byId(id: String): KeyboardLanguage? = when (id) {
        "id" -> ID
        "en" -> EN
        "ru" -> ID
        else -> extra.firstOrNull { it.id == id } ?: builtIn.firstOrNull { it.id == id }
    }

    fun enabled(settings: KeyboardSettings): List<KeyboardLanguage> =
        settings.languages.split(',').map { it.trim() }.filter { it.isNotEmpty() }.distinct().mapNotNull(::byId)
            .filter { it.id == "id" || it.id == "en" || it.fromPlugin != null }
            .ifEmpty { listOf(ID, EN) }

    fun parse(o: JSONObject, pluginId: String): KeyboardLanguage {
        val id = o.optString("id").trim().lowercase()
        require(Regex("^[a-z]{2,3}([-_][a-z0-9]{2,8})?$").matches(id)) { "languages: id format «xx» atau «xx-yy»" }
        val rowsArr = o.optJSONArray("rows") ?: throw IllegalArgumentException("languages.$id: butuh baris rows")
        require(rowsArr.length() in 1..5) { "languages.$id: 1 sampai 5 baris" }
        val rows = (0 until rowsArr.length()).map { i ->
            val v = rowsArr.get(i)
            if (v is org.json.JSONArray) (0 until min(v.length(), 14)).map { v.optString(it) }.filter { it.isNotEmpty() }
            else r(v.toString()).take(14)
        }.filter { it.isNotEmpty() }
        val shift = HashMap<String, String>()
        o.optJSONObject("shift")?.let { s -> s.keys().forEach { k -> shift[k] = s.optString(k) } }
        val accents = HashMap<String, List<String>>()
        o.optJSONObject("variants")?.let { s ->
            s.keys().forEach { k -> s.optJSONArray(k)?.let { a -> accents[k] = (0 until min(a.length(), 12)).map { a.optString(it) } } }
        }
        val name = o.optString("name", id).take(30)
        return KeyboardLanguage(
            id = id,
            label = o.optString("label", id.take(2).uppercase()).take(4),
            spaceLabel = name,
            title = o.optString("title", name).take(30),
            locale = o.optString("locale", id),
            rows = rows,
            shiftMap = shift,
            accents = accents,
            dict = if (o.isNull("dictionary")) null else o.optString("dictionary").takeIf { it.isNotBlank() },
            hasCase = o.optBoolean("hasCase", shift.isEmpty()),
            composer = Composer.NONE,
            autoCorrect = o.optBoolean("autoCorrect", true),
            rtl = o.optBoolean("rtl", false),
            fromPlugin = pluginId
        )
    }

    fun variantsFor(lang: KeyboardLanguage, key: String, digit: String?): List<String>? {
        val acc = lang.accents[key].orEmpty()
        val shifted = lang.shiftMap[key]
        val primary: String
        val others = ArrayList<String>()
        when {
            digit != null -> {
                primary = digit
                others += key
                if (shifted != null && !lang.hasCase) others += shifted
                others += acc
            }
            acc.isNotEmpty() -> {
                primary = acc.first()
                others += key
                others += acc.drop(1)
            }
            shifted != null && !lang.hasCase -> {
                primary = shifted
                others += key
            }
            else -> return null
        }
        val list = others.distinct().filter { it != primary }.take(8).toMutableList()
        list.add(min((list.size + 1) / 2, list.size), primary)
        return list
    }

    fun buildRows(lang: KeyboardLanguage): List<KeyRow> {
        val widest = max(10, lang.rows.maxOf { it.size })
        val last = lang.rows.lastIndex
        val digits = "1234567890"
        return lang.rows.mapIndexed { ri, row ->
            val keys = ArrayList<KeyModel>()
            val digitRow = ri == 0 && row.size >= 8
            val models = row.mapIndexed { ci, k ->
                val digit = if (digitRow && ci < 10) digits[ci].toString() else null
                KeyModel(
                    label = k,
                    upperLabel = lang.shiftMap[k],
                    langVariants = variantsFor(lang, k, digit)
                )
            }
            if (ri == last) {
                val side = max(1.2f, (widest - row.size) / 2f)
                keys += KeyModel("⇧", KeyType.SHIFT, side)
                keys += models
                keys += KeyModel("⌫", KeyType.BACKSPACE, side + 0.1f)
            } else {
                val pad = (widest - row.size) / 2f
                if (pad > 0f) keys += KeyModel("", KeyType.SPACER, pad)
                keys += models
                if (pad > 0f) keys += KeyModel("", KeyType.SPACER, pad)
            }
            KeyRow(keys)
        }
    }
}

object Hangul {
    fun compose(raw: String): String = raw
}

object Telex {
    fun compose(raw: String): String = raw
}
