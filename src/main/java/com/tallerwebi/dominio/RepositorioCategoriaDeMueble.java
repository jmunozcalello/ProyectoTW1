package com.tallerwebi.dominio;

import java.util.List;

/** Acceso a las categorías de mueble y sus medidas promedio. */
public interface RepositorioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerTodas();

  /** Devuelve la categoría con ese id, o null si no existe. */
  CategoriaDeMueble buscarPorId(Long id);
}
