package com.tallerwebi.dominio;

import jakarta.persistence.*;

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

    public Mueble() {
    }

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

    public Double getLargo() {
        return largo;
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
