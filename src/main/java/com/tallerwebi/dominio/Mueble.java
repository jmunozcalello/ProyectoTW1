package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mueble {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String nombre;
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

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return this.nombre;
  }
}
