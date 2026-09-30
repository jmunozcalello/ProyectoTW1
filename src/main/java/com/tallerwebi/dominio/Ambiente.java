package com.tallerwebi.dominio;

public class Ambiente {

  private Double ancho;
  private Double largo;

  public Ambiente(Double ancho, Double largo) {
    this.ancho = ancho;
    this.largo = largo;
  }

  public Double getAncho() {
    return ancho;
  }

  public Double getLargo() {
    return largo;
  }
}
