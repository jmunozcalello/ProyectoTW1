package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ambiente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double ancho;
  private Double largo;

  public Ambiente() {}

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
