package com.tallerwebi.dominio;

import java.util.List;

public class Plano {
    private List<MuebleUbicado> muebles;
    private Double ancho;
    private Double largo;



    public Plano(Double ancho, Double largo, List<MuebleUbicado> muebles) {
        this.ancho = ancho;
        this.largo = largo;
        this.muebles = muebles;
    }

    public List<MuebleUbicado> getMuebles() {
        return muebles;
    }

}
