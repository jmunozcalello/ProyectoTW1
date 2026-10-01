package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementación que delega en el repositorio de categorías. */
@Service("servicioCategoriaDeMueble")
@Transactional
public class ServicioCategoriaDeMuebleImpl implements ServicioCategoriaDeMueble {

  private final RepositorioCategoriaDeMueble repositorioCategoriaDeMueble;

  @Autowired
  public ServicioCategoriaDeMuebleImpl(RepositorioCategoriaDeMueble repositorioCategoriaDeMueble) {
    this.repositorioCategoriaDeMueble = repositorioCategoriaDeMueble;
  }

  @Override
  public List<CategoriaDeMueble> obtenerCategorias() {
    return repositorioCategoriaDeMueble.obtenerTodas();
  }
}
