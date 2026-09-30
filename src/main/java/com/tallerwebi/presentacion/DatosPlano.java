package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Formulario de confirmacion del ambiente. Es el objeto que Spring bindea del request, porque
 * Ambiente no se puede instanciar desde un formulario (no tiene constructor vacio) y Mueble es
 * una entidad de JPA que no conviene usar como portador de datos del formulario.
 */
public class DatosPlano {

  private Double ancho;
  private Double largo;
  private List<DatosMueble> muebles = new ArrayList<>();

  public DatosPlano() {}

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

  public List<DatosMueble> getMuebles() {
    return muebles;
  }

  public void setMuebles(List<DatosMueble> muebles) {
    this.muebles = muebles;
  }
}
