package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PresupuestoNegativoException;
import com.tallerwebi.dominio.excepcion.PresupuestoNuloException;
import java.util.List;

public interface ServicioCatalogo {
  List<Mueble> ObtenerMueblesConUnPrecioMenorAl(Double precioMaximo)
    throws PresupuestoNegativoException, PresupuestoNuloException;
  List<Mueble> ObtenerMueblesDeEstilo(Estilo estilo);
  List<Mueble> ObtenerMueblesDeEstilos(List<Estilo> estilos);
  Mueble ObtenerMuebleMasBaratoDeCategoria(Estilo categoria);
  void RegistrarMueble(Mueble mueble1);
  List<Mueble> ObtenerMueblesOrdenadosPorPrecioAsc();
  List<Mueble> ObtenerMueblesOrdenadosPorPrecioDesc();
}
