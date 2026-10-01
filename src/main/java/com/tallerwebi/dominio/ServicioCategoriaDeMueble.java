package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerCategorias();

  Mueble crearMueble(String nombre, Long idCategoria, Double ancho, Double profundidad);
}
