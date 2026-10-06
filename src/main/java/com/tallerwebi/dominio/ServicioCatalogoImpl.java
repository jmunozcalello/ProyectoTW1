package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.MuebleNoEncontrado;
import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioCatalogo")
@Transactional
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
    List<Mueble> mueblesFiltradorPorPrecioAsc =
      this.filtrarYOrdenar(null, null, null, null, OrdenDeMuebles.PRECIO_ASC);
    return mueblesFiltradorPorPrecioAsc;
  }

  @Override
  public List<Mueble> ObtenerMueblesOrdenadosPorPrecioDesc() {
    List<Mueble> mueblesFiltradorPorPrecioDesc =
      this.filtrarYOrdenar(null, null, null, null, OrdenDeMuebles.PRECIO_DESC);
    return mueblesFiltradorPorPrecioDesc;
  }

  @Override
  public List<Mueble> ObtenerMueblesQueCumplan(
    TipoDeAmbiente tipoDeAmbiente,
    Estilo estilo,
    Double precioMaximo
  ) throws PresupuestoNegativoException {
    this.validarPresupuestos(null, precioMaximo);
    List<Mueble> mueblesFiltrados =
      this.filtrarYOrdenar(null, precioMaximo, estilo, tipoDeAmbiente, null);
    return mueblesFiltrados;
  }

  @Override
  public List<Mueble> ObtenerMueblesFiltrados(
    Double precioMinimo,
    Double precioMaximo,
    Estilo estilo,
    TipoDeAmbiente tipoDeAmbiente,
    OrdenDeMuebles orden
  ) throws PresupuestoNegativoException {
    this.validarPresupuestos(precioMinimo, precioMaximo);
    List<Mueble> mueblesFiltrados =
      this.filtrarYOrdenar(precioMinimo, precioMaximo, estilo, tipoDeAmbiente, orden);
    return mueblesFiltrados;
  }

  @Override
  public Mueble ObtenerMueblePorId(int id) throws MuebleNoEncontrado {
    Mueble mueble = this.repositorioCatalogo.ObtenerMueblePorId(id);
    if (mueble == null) {
      throw new MuebleNoEncontrado("Mueble no encontrado");
    }
    return mueble;
  }

  private void validarPresupuestos(Double precioMinimo, Double precioMaximo)
    throws PresupuestoNegativoException {
    if ((precioMaximo != null && precioMaximo < 0) || (precioMinimo != null && precioMinimo < 0)) {
      throw new PresupuestoNegativoException("El presupuesto no puede ser negativo");
    }
  }

  private List<Mueble> filtrarYOrdenar(
    Double precioMinimo,
    Double precioMaximo,
    Estilo estilo,
    TipoDeAmbiente tipoDeAmbiente,
    OrdenDeMuebles orden
  ) {
    List<Mueble> mueblesFiltrados = new ArrayList<>();
    for (Mueble mueble : repositorioCatalogo.ObtenerTodosLosMuebles()) {
      if (
        cumpleElFiltroDeTipoDeAmbiente(mueble, tipoDeAmbiente) &&
        cumpleElFiltroDeEstilo(mueble, estilo) &&
        cumpleElFiltroDePrecioMinimo(mueble, precioMinimo) &&
        cumpleElFiltroDePrecioMaximo(mueble, precioMaximo)
      ) {
        mueblesFiltrados.add(mueble);
      }
    }

    mueblesFiltrados = ordenarPorPrecio(mueblesFiltrados, orden);
    return mueblesFiltrados;
  }

  private List<Mueble> ordenarPorPrecio(List<Mueble> muebles, OrdenDeMuebles orden) {
    if (orden == OrdenDeMuebles.PRECIO_ASC) {
      return muebles.stream().sorted(Comparator.comparing(Mueble::getPrecio)).toList();
    }
    if (orden == OrdenDeMuebles.PRECIO_DESC) {
      return muebles.stream().sorted(Comparator.comparing(Mueble::getPrecio).reversed()).toList();
    }
    return muebles;
  }

  private boolean cumpleElFiltroDeEstilo(Mueble mueble, Estilo estilo) {
    return estilo == null || mueble.getEstilo() == estilo;
  }

  private boolean cumpleElFiltroDePrecioMinimo(Mueble mueble, Double precioMinimo) {
    return precioMinimo == null || mueble.getPrecio() >= precioMinimo;
  }

  private boolean cumpleElFiltroDePrecioMaximo(Mueble mueble, Double precioMaximo) {
    return precioMaximo == null || mueble.getPrecio() <= precioMaximo;
  }

  private boolean cumpleElFiltroDeTipoDeAmbiente(Mueble mueble, TipoDeAmbiente tipoDeAmbiente) {
    if (tipoDeAmbiente == null || tipoDeAmbiente.getNombre() == null) {
      return true;
    }
    for (TipoDeAmbiente tipoDelMueble : mueble.getTiposDeAmbiente()) {
      if (tipoDeAmbiente.getNombre().equals(tipoDelMueble.getNombre())) {
        return true;
      }
    }
    return false;
  }
}
