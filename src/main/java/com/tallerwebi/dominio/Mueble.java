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


    public Mueble(String nombre) {
        this.nombre = nombre;
    }

    public Mueble (String nombre, Double ancho, Double largo) {
        this.nombre = nombre;
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
