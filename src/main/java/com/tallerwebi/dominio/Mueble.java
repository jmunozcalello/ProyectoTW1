package com.tallerwebi.dominio;

public class Mueble {
    private String nombre;
    private Double ancho;
    private Double largo;

    /**
     * Convención de coordenadas: origen (0,0) en la esquina superior izquierda.
     * Eje X: horizontal, de 0 a ancho.
     * Eje Y: vertical, de 0 a largo.
     */

  private double precio;
  private Estilo estilo;

    public Mueble(String nombre) {
        this.nombre = nombre;
    }
  public void setPrecio(double _precio) {
    this.precio = _precio;
  }

    public Mueble (String nombre, Double ancho, Double largo) {
        this.nombre = nombre;
        this.ancho = ancho;
        this.largo = largo;
    }
  public double getPrecio() {
    return this.precio;
  }


    public Double getAncho() {
        return ancho;
    }
  public void SetEstilo(Estilo estilo) {
    this.estilo = estilo;
  }

    public Double getLargo() {
        return largo;
    }
  public Estilo getEstilo() {
    return this.estilo;
  }
}
