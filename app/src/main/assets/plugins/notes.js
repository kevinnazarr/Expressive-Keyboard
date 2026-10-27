/* @plugin
{
  "id": "builtin.notes",
  "name": "Catatan Cepat",
  "version": "1.1",
  "author": "Expressive Keyboard",
  "description": "Simpan teks penting atau salinan clipboard dan sisipkan kembali dengan satu ketukan. Disimpan aman secara lokal di perangkat.",
  "icon": "@note",
  "permissions": ["read_text", "clipboard"]
}
*/

var notes = kb.storage.get("notes", []);

function save() {
  kb.storage.set("notes", notes);
  kb.refresh();
}

function addNote(text) {
  text = (text || "").trim();
  if (!text) {
    kb.status("Pilih teks atau salin ke clipboard terlebih dahulu");
    return;
  }
  notes = notes.filter(function (n) { return n !== text; });
  notes.unshift(text);
  if (notes.length > 50) notes.length = 50;
  save();
  kb.status("Berhasil disimpan ke Catatan Cepat");
}

kb.addPanel({
  id: "notes",
  title: "Catatan Cepat",
  icon: "@note",
  render: function () {
    var ui = [
      { type: "row", children: [
        { type: "button", text: "Dari Seleksi", icon: "@select_all", onClick: function () { addNote(kb.selectedText()); } },
        { type: "button", text: "Dari Clipboard", icon: "@clipboard", onClick: function () { addNote(kb.clipboard()[0]); } }
      ] }
    ];
    if (notes.length === 0) {
      ui.push({ type: "text", text: "Belum ada catatan tersimpan. Pilih teks atau salin sesuatu lalu ketuk tombol di atas.", center: true });
    }
    notes.forEach(function (n, i) {
      ui.push({ type: "row", children: [
        { type: "button", text: n.length > 38 ? n.slice(0, 38) + "…" : n, onClick: function () { kb.commit(n); } },
        { type: "button", icon: "@delete", onClick: function () { notes.splice(i, 1); save(); } }
      ] });
    });
    return ui;
  }
});
