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

  public Estilo getEstilo() {
    return this.estilo;
  }

  public List<Color> getColores() {
    return this.colores;
  }

  public Iluminacion getIluminacion() {
    return this.iluminacion;
  }
}
