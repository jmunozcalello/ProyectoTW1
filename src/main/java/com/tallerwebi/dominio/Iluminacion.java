package com.tallerwebi.dominio;

public enum Iluminacion {
  CALIDA("Cálida", 2700),
  INTERMEDIA("Intermedia", 4000),
  FRIA("Fría", 6000);

  private final String descripcion;
  private final int kelvin;

  Iluminacion(String descripcion, int kelvin) {
    this.descripcion = descripcion;
    this.kelvin = kelvin;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public int getKelvin() {
    return this.kelvin;
  }
}
