package com.tallerwebi.dominio;

public class Mueble {

  private double precio;
  private Estilo estilo;

  public void setPrecio(double _precio) {
    this.precio = _precio;
  }

  public double getPrecio() {
    return this.precio;
  }

  public void SetEstilo(Estilo estilo) {
    this.estilo = estilo;
  }

  public Estilo getEstilo() {
    return this.estilo;
  }
}
