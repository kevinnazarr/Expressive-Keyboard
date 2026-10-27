/* @plugin
{
  "id": "my.first.mod",
  "name": "Mod Contoh Saya",
  "version": "1.0",
  "author": "Saya",
  "description": "Contoh mod: panel kustom, respons kata, saran kustom, dan penyimpanan lokal",
  "icon": "🚀",
  "permissions": ["read_text"],
  "layouts": {
    "symbols2": [
      ["~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆"],
      ["£", "€", "$", "¥", "^", "°", "=", "{", "}", "\\"],
      ["{page}", "%", "©", "®", "™", { "label": "λ", "action": "lambda" }, "[", "]", "{backspace}"]
    ]
  }
}
*/

// Manifest di atas adalah JSON manifest plugin yang mendukung semua fitur di PLUGINS.md
// (actions, shortcuts, symbols, theme, skin, settings, layouts, icons, languages).
// Kode di bawah dijalankan di sandbox Mozilla Rhino: menyediakan kb, console, setTimeout/setInterval.

// Menghitung jumlah kata yang diketik secara persisten
var stats = kb.storage.get("stats", { words: 0 });

// 1. Event setiap kata selesai diketik (membutuhkan izin read_text)
kb.on("word", function (e) {
  stats.words++;
  if (stats.words % 10 === 0) kb.storage.set("stats", stats);
});

// 2. Saran kustom: saat mengetik "mksh", tampilkan saran otomatis
kb.on("suggest", function (e) {
  if (e.word.toLowerCase() === "mksh") return ["terima kasih banyak", "makasih ya"];
});

// 3. Modifikasi tombol: dua titik berturut-turut otomatis menjadi elipsis (…)
kb.on("key", function (e) {
  if (e.text === "." && kb.textBefore(1) === ".") {
    kb.deleteBefore(1);
    return "…";
  }
});

// 4. Panel kustom: render() mengembalikan elemen UI, kb.refresh() memperbarui tampilan
kb.addPanel({
  id: "main",
  title: "Mod Saya",
  icon: "🚀",
  render: function () {
    return [
      { type: "text", text: "Jumlah kata diketik: " + stats.words, size: 18, bold: true },
      { type: "row", children: [
        { type: "button", text: "Tanggal", icon: "@date", onClick: function () { kb.commit(new Date().toLocaleDateString()); } },
        { type: "button", text: "Reset", icon: "@refresh", onClick: function () {
          stats.words = 0;
          kb.storage.set("stats", stats);
          kb.refresh();
        } }
      ] }
    ];
  }
});
