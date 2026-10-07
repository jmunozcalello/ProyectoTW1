package com.tallerwebi.dominio;

/**
 * Muro del ambiente visto desde arriba. La posicion de un obstaculo se mide sobre el muro:
 * superior e inferior desde la izquierda, derecho e izquierdo desde arriba.
 */
public enum Muro {
  SUPERIOR("superior", "la izquierda"),
  DERECHO("derecho", "arriba"),
  INFERIOR("inferior", "la izquierda"),
  IZQUIERDO("izquierdo", "arriba");

  private final String nombre;
  private final String origenDeLaPosicion;

  Muro(String nombre, String origenDeLaPosicion) {
    this.nombre = nombre;
    this.origenDeLaPosicion = origenDeLaPosicion;
  }

  public String getNombre() {
    return nombre;
  }

  /** Desde donde se cuenta la posicion de un obstaculo sobre este muro. */
  public String getOrigenDeLaPosicion() {
    return origenDeLaPosicion;
  }

  /** Cuanto mide el muro: los horizontales miden el ancho del ambiente, los verticales el largo. */
  public Double largoEn(Ambiente ambiente) {
    return switch (this) {
      case SUPERIOR, INFERIOR -> ambiente.getAncho();
      case DERECHO, IZQUIERDO -> ambiente.getLargo();
    };
  }

  /** Cuanto espacio hay desde el muro hasta el muro de enfrente. */
  public Double profundidadDisponibleEn(Ambiente ambiente) {
    return switch (this) {
      case SUPERIOR, INFERIOR -> ambiente.getLargo();
      case DERECHO, IZQUIERDO -> ambiente.getAncho();
    };
  }
}
