package com.tallerwebi.dominio;

import java.util.List;

/** Consulta las categorías de mueble para autocompletar medidas en los formularios (HU-08). */
@FunctionalInterface
public interface ServicioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerCategorias();
}
