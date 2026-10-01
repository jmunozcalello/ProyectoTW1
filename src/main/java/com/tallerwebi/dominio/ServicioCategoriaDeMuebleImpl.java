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
    CategoriaDeMueble categoria = repositorioCategoriaDeMueble.buscarPorId(idCategoria);
    return new Mueble(
      nombre,
      medidaFinal(ancho, categoria.getAnchoPromedio()),
      medidaFinal(profundidad, categoria.getProfundidadPromedio())
    );
  }

  /** Criterio 2 de la HU-08: lo que el usuario escribió a mano gana sobre el promedio. */
  private static Double medidaFinal(Double escritaPorElUsuario, Double promedioDeLaCategoria) {
    return escritaPorElUsuario != null ? escritaPorElUsuario : promedioDeLaCategoria;
  }
}
