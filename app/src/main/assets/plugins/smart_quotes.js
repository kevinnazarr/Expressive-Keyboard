/* @plugin
{
  "id": "builtin.smart-quotes",
  "name": "Tanda Petik Pintar «»",
  "version": "1.1",
  "author": "Expressive Keyboard",
  "description": "Mengubah tanda petik biasa \" menjadi petik sudut « atau » secara otomatis tergantung apakah sedang membuka atau menutup kutipan.",
  "icon": "«»",
  "permissions": ["read_text"],
  "enabledByDefault": false
}
*/

kb.on("key", function (e) {
  if (e.text !== "\"") return;
  var before = kb.textBefore(1);
  var opening = before === "" || /[\s(\[{«„—-]$/.test(before);
  return opening ? "«" : "»";
});
