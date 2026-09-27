package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class ServicioCatalogoImpl implements ServicioCatalogo {

  private RepositorioCatalogo repositorioCatalogo;

  @Autowired
  public ServicioCatalogoImpl(RepositorioCatalogo repositorioCatalogo) {
    this.repositorioCatalogo = repositorioCatalogo;
  }

  @Override
  public List<Mueble> ObtenerMueblesConUnPrecioMenorAl(Double precioPresupuestoMaximo)
    throws PresupuestoNegativoException, PresupuestoNuloException {
    if (precioPresupuestoMaximo == null) {
      throw new PresupuestoNuloException("El presupuesto máximo no puede ser nulo");
    }

    if (precioPresupuestoMaximo < 0) {
      throw new PresupuestoNegativoException("El presupuesto máximo no puede ser negativo");
    }

    List<Mueble> mueblesFiltrados = new ArrayList<>();
    List<Mueble> catalogo = repositorioCatalogo.ObtenerTodosLosMuebles();

    for (Mueble mueble : catalogo) {
      if (mueble.getPrecio() <= precioPresupuestoMaximo) {
        mueblesFiltrados.add(mueble);
      }
    }

    return mueblesFiltrados;
  }

  @Override
  public List<Mueble> ObtenerMueblesDeEstilos(List<Estilo> estilos) {
    List<Mueble> mueblesFiltrados = new ArrayList<>();
    List<Mueble> catalogo = repositorioCatalogo.ObtenerTodosLosMuebles();
    for (Mueble mueble : catalogo) {
      if (estilos.contains(mueble.getEstilo())) {
        mueblesFiltrados.add(mueble);
      }
    }
    return mueblesFiltrados;
  }

  @Override
  public List<Mueble> ObtenerMueblesDeEstilo(Estilo estilo) {
    List<Mueble> mueblesFiltrados = new ArrayList<>();
    List<Mueble> catalogo = repositorioCatalogo.ObtenerTodosLosMuebles();
    for (Mueble mueble : catalogo) {
      if (mueble.getEstilo() == estilo) {
        mueblesFiltrados.add(mueble);
      }
    }
    return mueblesFiltrados;
  }

  @Override
  public Mueble ObtenerMuebleMasBaratoDeCategoria(Estilo categoria) {
    Mueble muebleMasBarato = null;
    List<Mueble> catalogo = repositorioCatalogo.ObtenerTodosLosMuebles();
    for (Mueble mueble : catalogo) {
      if (mueble.getEstilo() == categoria) {
        if (muebleMasBarato == null || mueble.getPrecio() < muebleMasBarato.getPrecio()) {
          muebleMasBarato = mueble;
        }
      }
    }
    return muebleMasBarato;
  }

  @Override
  public void RegistrarMueble(Mueble mueble) {
    this.repositorioCatalogo.guardarMueble(mueble);
  }

  @Override
  public List<Mueble> ObtenerMueblesOrdenadosPorPrecioAsc() {
    List<Mueble> muebles = repositorioCatalogo.ObtenerTodosLosMuebles();

    return muebles.stream().sorted(Comparator.comparing(Mueble::getPrecio)).toList();
  }

  @Override
  public List<Mueble> ObtenerMueblesOrdenadosPorPrecioDesc() {
    List<Mueble> muebles = repositorioCatalogo.ObtenerTodosLosMuebles();

    return muebles.stream().sorted(Comparator.comparing(Mueble::getPrecio).reversed()).toList();
  }
}
