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
