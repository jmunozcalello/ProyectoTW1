package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Tipo de mueble (cama, sofá, escritorio...) con sus medidas promedio en metros. Se usa para
 * autocompletar el ancho y la profundidad cuando el usuario carga un mueble (HU-08).
 */
@Entity
public class CategoriaDeMueble {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private Double anchoPromedio;
  private Double profundidadPromedio;

  protected CategoriaDeMueble() {}

  public CategoriaDeMueble(String nombre, Double anchoPromedio, Double profundidadPromedio) {
    this.nombre = nombre;
    this.anchoPromedio = anchoPromedio;
    this.profundidadPromedio = profundidadPromedio;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public Double getAnchoPromedio() {
    return anchoPromedio;
  }

  public Double getProfundidadPromedio() {
    return profundidadPromedio;
  }
}
