import { conectarAutocompletado } from "./dimensiones_funciones.js";

conectarAutocompletado(document.getElementById("filas-muebles"));
conectarAutocompletado(document.getElementById("filas-obstaculos"), "select[data-medidas-sugeridas]");
