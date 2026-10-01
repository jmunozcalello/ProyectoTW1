package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCategoriaDeMueble {
  List<CategoriaDeMueble> obtenerTodas();

  CategoriaDeMueble buscarPorId(Long id);
}
