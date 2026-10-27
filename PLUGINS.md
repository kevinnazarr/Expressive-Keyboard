# Plugin & Mod Expressive Keyboard (Plugins & Mods Guide)

Panduan format dan API JavaScript untuk memperluas fungsionalitas Expressive Keyboard.
*Documentation available in Indonesian & English.*

---

## 🇮🇩 Bahasa Indonesia

Plugin dapat diimpor melalui menu **Pengaturan → Plugin & Mod**: dari file langsung atau dari papan klip (clipboard).

Terdapat 3 tipe plugin:
| Tipe File | Keterangan |
|---|---|
| `.json` | **Plugin Deklaratif**: aksi, singkatan, simbol, tema visual, tata letak, ikon, dan pengaturan. Dapat menyertakan kode JavaScript di kolom `script`. |
| `.js` | **Mod JavaScript**: kode JavaScript mandiri dengan manifest JSON di blok komentar `/* @plugin { ... } */` di awal file. |
| `.zip` | **Mod Lengkap**: paket arsip berisi `plugin.json` + `main.js` + aset gambar atau data pendukung (maks. 8 MB). |

### Kemampuan Skrip Mod (`kb` API)
Skrip dijalankan dalam sandbox interpreter JavaScript Mozilla Rhino (dukungan sintaks ES5 dan sebagian besar ES6: `let`, `const`, arrow functions).
Objek global `kb` menyediakan interaksi dengan keyboard:
- Merespons pengetikan karakter dan kata (`kb.on("key")`, `kb.on("word")`, `kb.on("suggest")`).
- Menyisipkan dan memanipulasi teks di kolom input (`kb.commit()`).
- Menambahkan tombol pada bilah alat atau panel kustom (`kb.addButton()`).
- Menyimpan data lokal secara persisten (`kb.storage`).
- Menggambar efek visual atau animasi di atas Canvas keyboard (`kb.draw()`).

### Contoh Cepat Mod JavaScript (`.js`)

```javascript
/* @plugin
{
  "id": "contoh.mod",
  "name": "Mod Sapaan",
  "version": "1.0",
  "description": "Menyisipkan sapaan otomatis",
  "permissions": ["read_text"]
}
*/

// Menambahkan tombol sapaan di bilah alat
kb.addButton({
  title: "Halo",
  icon: "👋",
  toolbar: true,
  onClick: function () {
    kb.commit("Halo! Selamat datang di Expressive Keyboard.");
  }
});

// Mendengarkan kata yang selesai diketik
kb.on("word", function (e) {
  console.log("Kata yang diketik:", e.word);
});
```

### Event yang Tersedia (`kb.on(event, callback)`)
- `"key"`: `{ text }` — saat huruf/simbol ditekan. Kembalikan string baru untuk mengubah karakter, atau `false` untuk membatalkan.
- `"word"`: `{ word, previous }` — saat sebuah kata selesai diketik (misal setelah spasi).
- `"suggest"`: `{ word, previous }` — saat kata sedang diketik untuk memberikan saran kata kustom.
- `"space"`, `"enter"`, `"backspace"` — respons terhadap tombol fungsi spasi, enter, atau hapus.

### Daftar Plugin Bawaan (Built-in Plugins)
1. **Kamus & Kata Baku Indonesia** (`builtin.dictionary-id`): Perbaikan otomatis singkatan gaul/chat menjadi kata baku resmi KBBI (`yg` → `yang`, `dgn` → `dengan`, dll.) dan penambahan kosakata saran bahasa Indonesia.
2. **Template & Balas Cepat** (`builtin.quick-replies`): Balasan kilat untuk salam formal/santai, konfirmasi transaksi, nomor rekening, alamat, dan pesan harian.
3. **Format Teks Chat & Markdown** (`builtin.chat-formatting`): Pembungkus teks otomatis untuk *tebal*, _miring_, ~coret~, `monospace`, ```kode```, dan > kutipan.
4. **Koleksi Kaomoji Lengkap** (`builtin.kaomoji-expressive`): Koleksi kaomoji Jepang ekspresif terkelompok (senang, cinta, santai, sedih, gemas, marah).
5. **Alat Olah Teks** (`builtin.text-tools`): Kapitalisasi huruf, case pemrograman (`camelCase`, `snake_case`, `kebab-case`), font tebal/miring Unicode, dan hitung rumus.
6. **Navigasi & Edit Teks** (`builtin.navigation`): Tombol seleksi, salin, potong, tempel, urungkan (undo), ulangi (redo), dan loncat kursor.
7. **Simbol & Tipografi** (`builtin.symbols`): Koleksi simbol panah, matematika, mata uang (`Rp`, `$`, `€`, `¥`, dll.), dan tipografi profesional.
8. **Tanggal & Waktu** (`builtin.datetime`): Penyisipan tanggal dan jam real-time dalam beragam format Indonesia dan internasional.
9. **Catatan Cepat** (`builtin.notes`): Penyimpanan teks penting atau clipboard langsung di keyboard secara lokal.
10. **Kalkulator Keyboard** (`builtin.calculator`): Panel kalkulator mini untuk menghitung cepat dan menyisipkan hasil perhitungan ke input.
11. **Tanda Petik Pintar** (`builtin.smart-quotes`): Penggantian otomatis tanda petik biasa menjadi petik sudut « » sesuai konteks.
12. **Tema Visual Bawaan**:
    - **Material 3 · Google** (`builtin.material`)
    - **Liquid Glass** (`builtin.liquid-glass`)
    - **Neon Glow** (`builtin.neon`)
    - **Neumorfisme / Soft UI** (`builtin.neumorph`)
    - **Klasik / Gboard Style** (`builtin.classic`)

---

## 🇬🇧 English

Plugins can be imported via **Settings → Plugins & mods**: from a local file or directly from your clipboard.

Three formats are supported:
| File Extension | Content Description |
|---|---|
| `.json` | **Declarative Plugin**: actions, shortcuts, symbols, visual themes, custom layouts, icons, and settings. Can embed JS code in the `script` field. |
| `.js` | **JavaScript Mod**: standalone JS script with a manifest header formatted as `/* @plugin { ... } */` at the top. |
| `.zip` | **Bundle Mod**: zip archive containing `plugin.json` + `main.js` + auxiliary images or data assets (up to 8 MB). |

### JavaScript Runtime Capabilities (`kb` API)
Scripts run in a sandboxed Mozilla Rhino engine (supporting ES5 + common ES6 features such as `let`, `const`, and arrow functions).
The global `kb` object provides access to the keyboard lifecycle:
- Listen to keypresses and completed words (`kb.on("key")`, `kb.on("word")`, `kb.on("suggest")`).
- Insert and edit text within the active input connection (`kb.commit()`).
- Inject custom buttons onto the toolbar or extra tabs (`kb.addButton()`).
- Persistent local key-value storage (`kb.storage`).
- Real-time custom drawing and animations on the keyboard canvas (`kb.draw()`).

### Quick JavaScript Mod Example (`.js`)

```javascript
/* @plugin
{
  "id": "sample.mod",
  "name": "Greeting Mod",
  "version": "1.0",
  "description": "Inserts friendly greetings",
  "permissions": ["read_text"]
}
*/

// Add a quick button to the toolbar
kb.addButton({
  title: "Hello",
  icon: "👋",
  toolbar: true,
  onClick: function () {
    kb.commit("Hello! Welcome to Expressive Keyboard.");
  }
});

// React to completed words
kb.on("word", function (e) {
  console.log("Typed word:", e.word);
});
```

### Built-in Plugins List
1. **Indonesian Dictionary & Standard Words** (`builtin.dictionary-id`): Automatically expands chat slang into formal Indonesian (`yg` → `yang`, `dgn` → `dengan`, etc.) and enhances dictionary word suggestions.
2. **Quick Replies & Templates** (`builtin.quick-replies`): Swift templates for formal greetings, payment transfers, bank accounts, shipping addresses, and daily chat responses.
3. **Chat Formatting & Markdown** (`builtin.chat-formatting`): Wrap selected text in WhatsApp, Telegram, and Markdown styles (*bold*, _italic_, ~strikethrough~, `code`, and > quotes).
4. **Complete Kaomoji Collection** (`builtin.kaomoji-expressive`): Expressive Japanese kaomoji categorized by mood (happy, love, chill, crying, cute animals, anger).
5. **Text Transformation Tools** (`builtin.text-tools`): Letter casing, programming conventions (`camelCase`, `snake_case`, `kebab-case`), mathematical Unicode fonts, and quick math evaluation.
6. **Navigation & Text Editing** (`builtin.navigation`): Dedicated keys for select all, copy, cut, paste, undo, redo, home, and end.
7. **Symbols & Typography** (`builtin.symbols`): Arrows, math symbols, currencies (`Rp`, `$`, `€`, `£`, `¥`), and typographic marks.
8. **Date & Time** (`builtin.datetime`): Insert current dates and times in various localized formats.
9. **Quick Notes** (`builtin.notes`): Lightweight, on-keyboard local scratchpad for saving and pasting snippets.
10. **Keyboard Calculator** (`builtin.calculator`): Embedded mini-calculator to compute expressions and insert answers directly into input fields.
11. **Smart Quotes** (`builtin.smart-quotes`): Automatically transforms regular straight quotes into directional guillemets « ».
12. **Built-in Visual Themes**:
    - **Material 3 · Google** (`builtin.material`)
    - **Liquid Glass** (`builtin.liquid-glass`)
    - **Neon Glow** (`builtin.neon`)
    - **Neumorphism / Soft UI** (`builtin.neumorph`)
    - **Classic / Gboard Style** (`builtin.classic`)
