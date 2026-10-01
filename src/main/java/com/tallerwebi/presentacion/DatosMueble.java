package com.tallerwebi.presentacion;

/**
 * Formulario de un mueble del ambiente. Existe para que Spring pueda bindear los datos del
 * request: el campo del formulario se llama "profundidad" (como en el wireframe) mientras que
 * el dominio lo guarda con el nombre "largo".
 */
public class DatosMueble {

  private String nombre;
  private Double ancho;
  private Double profundidad;
  private Long categoria;

  public DatosMueble() {}

  public DatosMueble(String nombre, Double ancho, Double profundidad) {
    this.nombre = nombre;
    this.ancho = ancho;
    this.profundidad = profundidad;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Double getAncho() {
    return ancho;
  }

  public void setAncho(Double ancho) {
    this.ancho = ancho;
  }

  public Double getProfundidad() {
    return profundidad;
  }

  public void setProfundidad(Double profundidad) {
    this.profundidad = profundidad;
  }

  /** Id de la categoría elegida en el formulario (HU-08), o null si no eligió ninguna. */
  public Long getCategoria() {
    return categoria;
  }

  public void setCategoria(Long categoria) {
    this.categoria = categoria;
  }
}
