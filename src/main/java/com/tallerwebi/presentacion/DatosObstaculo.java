package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Muro;
import com.tallerwebi.dominio.Obstaculo;
import com.tallerwebi.dominio.TipoDeObstaculo;

/**
 * Fila de la tabla de obstaculos fijos del formulario de definicion espacial. Spring convierte
 * tipo y muro desde el nombre de la constante del enum que manda cada select.
 */
public class DatosObstaculo {

  private TipoDeObstaculo tipo;
  private Muro muro;
  private Double posicion;
  private Double ancho;
  private Double profundidad;

  public DatosObstaculo() {}

  public DatosObstaculo(
    TipoDeObstaculo tipo,
    Muro muro,
    Double posicion,
    Double ancho,
    Double profundidad
  ) {
    this.tipo = tipo;
    this.muro = muro;
    this.posicion = posicion;
    this.ancho = ancho;
    this.profundidad = profundidad;
  }

  public Obstaculo aObstaculo() {
    return new Obstaculo(tipo, muro, posicion, ancho, profundidad);
  }

  /** Una fila agregada y dejada sin medidas no es un obstaculo (los selects siempre traen valor). */
  public boolean estaVacio() {
    return posicion == null && ancho == null && profundidad == null;
  }

  public TipoDeObstaculo getTipo() {
    return tipo;
  }

  public void setTipo(TipoDeObstaculo tipo) {
    this.tipo = tipo;
  }

  public Muro getMuro() {
    return muro;
  }

  public void setMuro(Muro muro) {
    this.muro = muro;
  }

  public Double getPosicion() {
    return posicion;
  }

  public void setPosicion(Double posicion) {
    this.posicion = posicion;
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
}
