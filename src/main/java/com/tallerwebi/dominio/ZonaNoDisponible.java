package com.tallerwebi.dominio;

/**
 * Rectangulo del ambiente donde no se pueden ubicar muebles, en las mismas coordenadas en metros
 * que usa el plano (origen en la esquina superior izquierda).
 */
public class ZonaNoDisponible {

  /**
   * Margen para comparar medidas en metros: las coordenadas que calcula el recorrido arrastran
   * errores de redondeo de punto flotante, y apoyar un mueble justo al lado de una zona no debe
   * contar como superposicion.
   */
  static final double TOLERANCIA = 1e-9;

  private final Double posicionX;
  private final Double posicionY;
  private final Double ancho;
  private final Double largo;

  public ZonaNoDisponible(Double posicionX, Double posicionY, Double ancho, Double largo) {
    this.posicionX = posicionX;
    this.posicionY = posicionY;
    this.ancho = ancho;
    this.largo = largo;
  }

  public Double getPosicionX() {
    return posicionX;
  }

  public Double getPosicionY() {
    return posicionY;
  }

  public Double getAncho() {
    return ancho;
  }

  public Double getLargo() {
    return largo;
  }

  public boolean seSolapaCon(MuebleUbicado ubicado) {
    Mueble mueble = ubicado.getMueble();
    return (
      posicionX < ubicado.getPosicionX() + mueble.getAncho() - TOLERANCIA &&
      ubicado.getPosicionX() < posicionX + ancho - TOLERANCIA &&
      posicionY < ubicado.getPosicionY() + mueble.getLargo() - TOLERANCIA &&
      ubicado.getPosicionY() < posicionY + largo - TOLERANCIA
    );
  }
}
