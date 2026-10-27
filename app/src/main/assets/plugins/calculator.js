/* @plugin
{
  "id": "builtin.calculator",
  "name": "Kalkulator Keyboard",
  "version": "1.1",
  "author": "Expressive Keyboard",
  "description": "Kalkulator mini praktis langsung di dalam keyboard. Hitung cepat dan sisipkan hasilnya ke kolom input dengan satu tombol.",
  "icon": "@calc"
}
*/

var expr = "";
var result = "";

function calc(s) {
  var clean = s.replace(/×/g, "*").replace(/÷/g, "/").replace(/,/g, ".").replace(/−/g, "-");
  if (!clean || !/^[0-9+\-*\/().%\s]+$/.test(clean)) return null;
  clean = clean.replace(/(\d+(\.\d+)?)%/g, "($1/100)");
  try {
    var v = Function("return (" + clean + ");")();
    if (typeof v !== "number" || !isFinite(v)) return null;
    return String(Math.round(v * 1e10) / 1e10);
  } catch (e) {
    return null;
  }
}

function press(k) {
  if (k === "C") { expr = ""; result = ""; }
  else if (k === "⌫") expr = expr.slice(0, -1);
  else if (k === "=") { var r = calc(expr); if (r !== null) { expr = r; result = ""; } }
  else expr += k;
  if (k !== "=") { var p = calc(expr); result = p === null || p === expr ? "" : p; }
  kb.refresh();
}

kb.addPanel({
  id: "calc",
  title: "Kalkulator",
  icon: "@calc",
  render: function () {
    return [
      { type: "text", text: expr || "0", size: 26, bold: true },
      { type: "text", text: result ? "= " + result : " ", size: 15 },
      { type: "grid", minWidth: 76, height: 42,
        items: ["7", "8", "9", "÷", "4", "5", "6", "×", "1", "2", "3", "-", "0", ".", "%", "+", "(", ")", "C", "⌫"],
        onClick: function (k) { press(k); } },
      { type: "row", children: [
        { type: "button", text: "=", accent: true, onClick: function () { press("="); } },
        { type: "button", text: "Sisipkan", icon: "@send", onClick: function () {
          var r = calc(expr);
          if (r !== null) kb.commit(r);
        } },
        { type: "button", text: "Sisipkan dengan Soal", onClick: function () {
          var r = calc(expr);
          if (r !== null) kb.commit(expr + " = " + r);
        } }
      ] }
    ];
  }
});
