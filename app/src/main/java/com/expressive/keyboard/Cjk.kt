package com.expressive.keyboard

import android.content.Context
import androidx.compose.runtime.Immutable

// ============================================================================
// STUB CJK: Fitur bahasa asing non-ID/EN dinonaktifkan
// ============================================================================

@Immutable
data class CjkCandidate(val text: String, val consumed: Int)

object CjkUser {
    fun clear() {}
    fun forget(lang: KeyboardLanguage, word: String) {}
    fun learn(lang: KeyboardLanguage, raw: String, text: String) {}
}

object Cjk {
    fun prepare(context: Context, lang: KeyboardLanguage) {}
    fun isCjk(c: Composer): Boolean = false
    fun candidates(lang: KeyboardLanguage, raw: String): List<CjkCandidate> = emptyList()
    fun display(c: Composer, text: String): String = text
    fun punctuation(c: Composer, text: String): String = text
}

object Kana {
    fun compose(raw: String): String = raw
}
