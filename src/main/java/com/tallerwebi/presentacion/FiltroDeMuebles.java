package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import com.tallerwebi.dominio.OrdenDeMuebles;
import com.tallerwebi.presentacion.excepcion.FiltroDeMueblesInvalidoException;
import java.util.Arrays;

public final class FiltroDeMuebles {

  private final Estilo estilo;
  private final Double precioMaximo;
  private final Double precioMinimo;
  private final String tipoDeAmbiente;
  private final OrdenDeMuebles orden;

  private FiltroDeMuebles(
    Estilo estilo,
    Double precioMaximo,
    Double precioMinimo,
    String tipoDeAmbiente,
    OrdenDeMuebles orden
  ) {
    this.estilo = estilo;
    this.precioMaximo = precioMaximo;
    this.precioMinimo = precioMinimo;
    this.tipoDeAmbiente = tipoDeAmbiente;
    this.orden = orden;
  }

  public static FiltroDeMuebles delCatalogo(
    String estilo,
    String precioMaximo,
    String precioMinimo,
    String tipoDeAmbiente,
    String orden
  ) throws FiltroDeMueblesInvalidoException {
    if (
      esUnEstiloDesconocido(estilo) ||
      esUnPrecioInvalido(precioMaximo) ||
      esUnPrecioInvalido(precioMinimo) ||
      esUnOrdenDesconocido(orden)
    ) {
      throw new FiltroDeMueblesInvalidoException(
        "El filtro del catalogo tiene criterios invalidos"
      );
    }

    return new FiltroDeMuebles(
      aEstilo(estilo),
      aPrecio(precioMaximo),
      aPrecio(precioMinimo),
      aTipoDeAmbiente(tipoDeAmbiente),
      aOrden(orden)
    );
  }

  public static FiltroDeMuebles deUnaPropuesta(String estilo, String presupuesto)
    throws FiltroDeMueblesInvalidoException {
    if (
      estaVacio(estilo) ||
      estaVacio(presupuesto) ||
      esUnEstiloDesconocido(estilo) ||
      esUnPrecioInvalido(presupuesto)
    ) {
      throw new FiltroDeMueblesInvalidoException(
        "La propuesta necesita un estilo y un presupuesto validos"
      );
    }

    return new FiltroDeMuebles(
      Estilo.valueOf(estilo),
      Double.parseDouble(presupuesto),
      null,
      null,
      null
    );
  }

  public Estilo getEstilo() {
    return this.estilo;
  }

  public Double getPrecioMaximo() {
    return this.precioMaximo;
  }

  public Double getPrecioMinimo() {
    return this.precioMinimo;
  }

  public String getTipoDeAmbiente() {
    return this.tipoDeAmbiente;
  }

  public OrdenDeMuebles getOrden() {
    return this.orden;
  }

  private static boolean estaVacio(String valor) {
    return valor == null || valor.isBlank();
  }

  private static boolean esUnEstiloDesconocido(String estilo) {
    return (
      !estaVacio(estilo) &&
      Arrays.stream(Estilo.values()).noneMatch(candidato -> candidato.name().equals(estilo))
    );
  }

  private static boolean esUnPrecioInvalido(String precio) {
    return !estaVacio(precio) && (noEsUnNumero(precio) || Double.parseDouble(precio) < 0);
  }

  private static boolean esUnOrdenDesconocido(String orden) {
    if (estaVacio(orden)) {
      return false;
    }
    return Arrays
      .stream(OrdenDeMuebles.values())
      .noneMatch(candidato -> candidato.name().equals(orden));
  }

  private static boolean noEsUnNumero(String valor) {
    try {
      Double.parseDouble(valor);
      return false;
    } catch (NumberFormatException e) {
      return true;
    }
  }

  private static Estilo aEstilo(String estilo) {
    return estaVacio(estilo) ? null : Estilo.valueOf(estilo);
  }

  private static Double aPrecio(String precio) {
    return estaVacio(precio) ? null : Double.parseDouble(precio);
  }

  private static String aTipoDeAmbiente(String tipoDeAmbiente) {
    return estaVacio(tipoDeAmbiente) ? null : tipoDeAmbiente;
  }

  private static OrdenDeMuebles aOrden(String orden) {
    return estaVacio(orden) ? null : OrdenDeMuebles.valueOf(orden);
  }
}
