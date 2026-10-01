package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    if (idCategoria == null) {
      if (ancho == null || profundidad == null) {
        throw new ValidacionException(
          "Completá las medidas de " + nombre + " o elegí una categoría"
        );
      }
      return new Mueble(nombre, ancho, profundidad);
    }
    CategoriaDeMueble categoria = repositorioCategoriaDeMueble.buscarPorId(idCategoria);
    if (categoria == null) {
      throw new ValidacionException("La categoría elegida para " + nombre + " no existe");
    }
    return new Mueble(
      nombre,
      medidaFinal(ancho, categoria.getAnchoPromedio()),
      medidaFinal(profundidad, categoria.getProfundidadPromedio())
    );
  }

  private static Double medidaFinal(Double escritaPorElUsuario, Double promedioDeLaCategoria) {
    return escritaPorElUsuario != null ? escritaPorElUsuario : promedioDeLaCategoria;
  }
}
