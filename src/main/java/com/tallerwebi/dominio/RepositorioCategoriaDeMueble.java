package com.tallerwebi.dominio;

import java.util.List;

/** Acceso a las categorías de mueble y sus medidas promedio. */
@FunctionalInterface
public interface RepositorioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerTodas();
}
