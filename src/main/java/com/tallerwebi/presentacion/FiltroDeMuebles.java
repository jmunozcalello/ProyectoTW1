package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;
import java.util.Arrays;
import java.util.Optional;

public final class FiltroDeMuebles {

  private final Estilo estilo;
  private final Double precioMaximo;

  private FiltroDeMuebles(Estilo estilo, Double precioMaximo) {
    this.estilo = estilo;
    this.precioMaximo = precioMaximo;
  }

  public static Optional<FiltroDeMuebles> delCatalogo(String estilo, String precioMaximo) {
    if (esUnEstiloDesconocido(estilo) || esUnPrecioInvalido(precioMaximo)) {
      return Optional.empty();
    }

    return Optional.of(new FiltroDeMuebles(aEstilo(estilo), aPrecioMaximo(precioMaximo)));
  }

  public static Optional<FiltroDeMuebles> deUnaPropuesta(String estilo, String presupuesto) {
    if (
      estaVacio(estilo) ||
      estaVacio(presupuesto) ||
      esUnEstiloDesconocido(estilo) ||
      esUnPrecioInvalido(presupuesto)
    ) {
      return Optional.empty();
    }

    return Optional.of(
      new FiltroDeMuebles(Estilo.valueOf(estilo), Double.parseDouble(presupuesto))
    );
  }

  public Estilo getEstilo() {
    return this.estilo;
  }

  public Double getPrecioMaximo() {
    return this.precioMaximo;
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

  private static Double aPrecioMaximo(String precio) {
    return estaVacio(precio) ? null : Double.parseDouble(precio);
  }
}
