package com.tallerwebi.dominio;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Mueble {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String nombre;
  private String descripcion;
  private double precio;
  private Double ancho;
  private Double largo;

  @Enumerated(EnumType.STRING)
  private Estilo estilo;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Convención de coordenadas: origen (0,0) en la esquina superior izquierda.
   * Eje X: horizontal, de 0 a ancho.
   * Eje Y: vertical, de 0 a largo.
   */

  public Mueble() {}

  public Mueble(String nombre, Double ancho, Double largo) {
    this.nombre = nombre;
    this.ancho = ancho;
    this.largo = largo;
  }

  public void setPrecio(double _precio) {
    this.precio = _precio;
  }

  public double getPrecio() {
    return this.precio;
  }

  public Double getAncho() {
    return ancho;
  }

  public void setAncho(Double ancho) {
    this.ancho = ancho;
  }

  public Double getLargo() {
    return largo;
  }

  public void setLargo(Double largo) {
    this.largo = largo;
  }

  public void setEstilo(Estilo estilo) {
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
    return descripcion;
  }

  /**
   * Compara por los datos editables del mueble, no por su identidad en la base.
   * El id queda afuera a proposito: lo genera la base y para un mueble todavia no
   * persistido vale 0, asi que incluirlo haria que dos muebles nuevos distintos
   * fueran iguales entre si.
   */

  @Override
  public boolean equals(Object otro) {
    if (this == otro) {
      return true;
    }
    if (!(otro instanceof Mueble)) {
      return false;
    }
    Mueble otroMueble = (Mueble) otro;
    return (
      // ancho y largo son Double y pueden venir en null, asi que van con Objects.equals:
      // Double.compare desempaqueta y revienta. precio es double primitivo y no puede ser null.
      Objects.equals(ancho, otroMueble.ancho) &&
      Objects.equals(largo, otroMueble.largo) &&
      Double.compare(precio, otroMueble.precio) == 0 &&
      Objects.equals(nombre, otroMueble.nombre) &&
      Objects.equals(descripcion, otroMueble.descripcion) &&
      estilo == otroMueble.estilo
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre, descripcion, precio, ancho, largo, estilo);
  }
}
