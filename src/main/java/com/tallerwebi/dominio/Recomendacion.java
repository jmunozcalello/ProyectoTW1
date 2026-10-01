package com.tallerwebi.dominio;

import java.util.List;

public final class Recomendacion {

  private final Estilo estilo;
  private final List<Color> colores;
  private final Iluminacion iluminacion;

  public Recomendacion(Estilo estilo, List<Color> colores, Iluminacion iluminacion) {
    this.estilo = estilo;
    this.colores = List.copyOf(colores);
    this.iluminacion = iluminacion;
  }

  public Recomendacion() {
    this.estilo = null;
    this.colores = null;
    this.iluminacion = null;
  }

  public Estilo getEstilo() {
    return this.estilo;
  }

  public List<Color> getColores() {
    return this.colores;
  }

  public Iluminacion getIluminacion() {
    return this.iluminacion;
  }

  public Estilo setEstilo(Estilo estilo) {
    return this.estilo;
  }

  public List<Color> setColores(List<Color> colores) {
    return this.colores;
  }

  public Iluminacion setIluminacion(Iluminacion iluminacion) {
    return this.iluminacion;
  }
}
