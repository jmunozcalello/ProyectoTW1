package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Reglas de las categorías de mueble: completa las medidas que el usuario no cargó. */
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

  @Override
  public Mueble crearMueble(String nombre, Long idCategoria, Double ancho, Double profundidad) {
    return new Mueble(nombre, ancho, profundidad);
  }
}
