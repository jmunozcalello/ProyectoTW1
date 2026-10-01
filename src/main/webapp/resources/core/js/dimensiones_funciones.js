/**
 * HU-08: autocompletado de dimensiones por categoría de mueble.
 *
 * Cada fila del formulario tiene un <select data-categoria> cuyas opciones traen las medidas
 * promedio en data-ancho y data-profundidad. Al elegir una categoría se completan el ancho y la
 * profundidad de esa fila, salvo los campos que el usuario escribió a mano.
 *
 * Se escucha en el contenedor (delegación de eventos) para que también funcione en las filas que
 * se agregan después con "Agregar mueble".
 */
const CAMPO_ANCHO = "input[name$='.ancho']";
const CAMPO_PROFUNDIDAD = "input[name$='.profundidad']";
const CAMPOS_DE_MEDIDA = `${CAMPO_ANCHO}, ${CAMPO_PROFUNDIDAD}`;

export function conectarAutocompletado(contenedor) {
  contenedor.addEventListener("input", (evento) => {
    if (evento.target.matches(CAMPOS_DE_MEDIDA)) {
      evento.target.dataset.editadoAMano = "true";
    }
  });

  contenedor.addEventListener("change", (evento) => {
    if (!evento.target.matches("select[data-categoria]")) {
      return;
    }
    const selector = evento.target;
    const opcion = selector.options[selector.selectedIndex];
    if (!opcion?.dataset.ancho) {
      return;
    }
    const fila = selector.closest("tr");
    autocompletar(fila.querySelector(CAMPO_ANCHO), opcion.dataset.ancho);
    autocompletar(fila.querySelector(CAMPO_PROFUNDIDAD), opcion.dataset.profundidad);
  });
}

function autocompletar(campo, valorPromedio) {
  const fueEscritoPorElUsuario = campo.dataset.editadoAMano === "true" && campo.value !== "";
  if (!fueEscritoPorElUsuario) {
    campo.value = valorPromedio;
  }
}
