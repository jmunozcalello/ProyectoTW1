package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estilo;

public class DatosPropuesta {

  private final Estilo estilo;
  private final Double presupuesto;

  public DatosPropuesta(Estilo estilo, Double presupuesto) {
    this.estilo = estilo;
    this.presupuesto = presupuesto;
  }

  public Estilo getEstilo() {
    return estilo;
  }

  public Double getPresupuesto() {
    return presupuesto;
  }
}
