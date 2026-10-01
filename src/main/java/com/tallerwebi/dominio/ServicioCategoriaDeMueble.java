package com.tallerwebi.dominio;

import java.util.List;

/** Categorías de mueble y sus medidas promedio (HU-08). */
public interface ServicioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerCategorias();

  /**
   * Arma el mueble que cargó el usuario. Si eligió una categoría, las medidas que dejó vacías se
   * completan con el promedio de esa categoría; las que escribió a mano se respetan.
   */
  Mueble crearMueble(String nombre, Long idCategoria, Double ancho, Double profundidad);
}
