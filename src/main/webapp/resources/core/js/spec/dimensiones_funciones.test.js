/**
HU-08: al elegir una categoría se autocompletan ancho y profundidad con sus medidas promedio,
respetando los valores que el usuario haya escrito a mano.
DOCU DE JASMINE: https://jasmine.github.io/api/5.8/global
**/
import { JSDOM } from "jsdom";
import { conectarAutocompletado } from "../dimensiones_funciones.js";

const SELECTOR_DE_CATEGORIA = `
  <select data-categoria>
    <option value="">Sin categoría</option>
    <option data-ancho="1.4" data-profundidad="1.9">Cama doble</option>
    <option data-ancho="2.1" data-profundidad="0.9">Sofá 3 cuerpos</option>
  </select>`;

function crearFila(indice, ancho = "", profundidad = "") {
  return `
    <tr>
      <td>${SELECTOR_DE_CATEGORIA}</td>
      <td><input type="text" name="muebles[${indice}].nombre"></td>
      <td><input type="number" name="muebles[${indice}].ancho" value="${ancho}"></td>
      <td><input type="number" name="muebles[${indice}].profundidad" value="${profundidad}"></td>
    </tr>`;
}

describe("Autocompletado de dimensiones por categoría", function() {
  let documento;
  let filas;

  beforeEach(function() {
    documento = new JSDOM(`<table><tbody id="filas-muebles">${crearFila(0)}${crearFila(1)}</tbody></table>`)
      .window.document;
    filas = documento.getElementById("filas-muebles");
    conectarAutocompletado(filas);
  });

  function fila(indice) {
    return filas.querySelectorAll("tr")[indice];
  }

  function ancho(indice) {
    return fila(indice).querySelector("input[name$='.ancho']");
  }

  function profundidad(indice) {
    return fila(indice).querySelector("input[name$='.profundidad']");
  }

  function elegirCategoria(indice, nombre) {
    const selector = fila(indice).querySelector("select[data-categoria]");
    selector.selectedIndex = [...selector.options].findIndex((opcion) => opcion.text === nombre);
    selector.dispatchEvent(new documento.defaultView.Event("change", { bubbles: true }));
  }

  function escribir(campo, valor) {
    campo.value = valor;
    campo.dispatchEvent(new documento.defaultView.Event("input", { bubbles: true }));
  }

  it("al elegir una categoría completa el ancho y la profundidad con sus medidas promedio", function() {
    elegirCategoria(0, "Cama doble");

    expect(ancho(0).value).toBe("1.4");
    expect(profundidad(0).value).toBe("1.9");
  });

  it("reemplaza las medidas de ejemplo que el usuario no tocó", function() {
    filas.insertAdjacentHTML("beforeend", crearFila(2, "2.1", "0.9"));

    elegirCategoria(2, "Cama doble");

    expect(ancho(2).value).toBe("1.4");
    expect(profundidad(2).value).toBe("1.9");
  });

  it("respeta el ancho que el usuario escribió a mano y completa solo la profundidad", function() {
    escribir(ancho(0), "1.6");

    elegirCategoria(0, "Cama doble");

    expect(ancho(0).value).toBe("1.6");
    expect(profundidad(0).value).toBe("1.9");
  });

  it("respeta las dos medidas si el usuario escribió ambas", function() {
    escribir(ancho(0), "1.6");
    escribir(profundidad(0), "2.05");

    elegirCategoria(0, "Cama doble");

    expect(ancho(0).value).toBe("1.6");
    expect(profundidad(0).value).toBe("2.05");
  });

  it("al cambiar de categoría reemplaza las medidas que había autocompletado", function() {
    elegirCategoria(0, "Cama doble");

    elegirCategoria(0, "Sofá 3 cuerpos");

    expect(ancho(0).value).toBe("2.1");
    expect(profundidad(0).value).toBe("0.9");
  });

  it("al volver a 'Sin categoría' no modifica las medidas", function() {
    elegirCategoria(0, "Cama doble");

    elegirCategoria(0, "Sin categoría");

    expect(ancho(0).value).toBe("1.4");
    expect(profundidad(0).value).toBe("1.9");
  });

  it("también autocompleta en filas agregadas después de cargar la página", function() {
    filas.insertAdjacentHTML("beforeend", crearFila(2));

    elegirCategoria(2, "Sofá 3 cuerpos");

    expect(ancho(2).value).toBe("2.1");
    expect(profundidad(2).value).toBe("0.9");
  });

  it("lo que el usuario escribe en una fila no bloquea el autocompletado de otra", function() {
    escribir(ancho(0), "1.6");

    elegirCategoria(1, "Cama doble");

    expect(ancho(1).value).toBe("1.4");
  });

  it("si el usuario borró una medida, vuelve a autocompletarla", function() {
    escribir(ancho(0), "1.6");
    escribir(ancho(0), "");

    elegirCategoria(0, "Cama doble");

    expect(ancho(0).value).toBe("1.4");
  });
});
