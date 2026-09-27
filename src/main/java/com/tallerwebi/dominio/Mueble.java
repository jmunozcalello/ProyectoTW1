package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mueble {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String nombre;
  private String descripcion;
  private double precio;

  @Enumerated(EnumType.STRING)
  private Estilo estilo;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

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

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return this.descripcion;
  }
}
