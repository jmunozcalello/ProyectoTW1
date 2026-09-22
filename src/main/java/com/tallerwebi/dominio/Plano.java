package com.tallerwebi.dominio;

import java.util.List;

public class Plano {
    private List<Mueble> muebles;
    private Double ancho;
    private Double largo;


    public Plano(Double ancho, Double largo, List<Mueble> muebles) {
        this.ancho = ancho;
        this.largo = largo;
        this.muebles = muebles;
    }

    public List<Mueble> getMuebles() {
        return muebles;
    }

}
