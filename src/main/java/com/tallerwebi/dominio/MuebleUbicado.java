package com.tallerwebi.dominio;

public class MuebleUbicado {

  private Mueble mueble;
  private Double coordenadaX;
  private Double coordenadaY;

  public MuebleUbicado(Mueble mueble, Double coordenadaX, Double coordenadaY) {
    this.mueble = mueble;
    this.coordenadaX = coordenadaX;
    this.coordenadaY = coordenadaY;
  }

  public Mueble getMueble() {
    return mueble;
  }

  public Double getPosicionX() {
    return coordenadaX;
  }

  public Double getPosicionY() {
    return coordenadaY;
  }
}
