package com.tallerwebi.dominio;

public class MuebleUbicado {

    private Mueble mueble;
    private Double x;
    private Double y;

    public MuebleUbicado(Mueble mueble, Double x, Double y) {
        this.mueble = mueble;
        this.x = x;
        this.y = y;
    }

    public Mueble getMueble() {
        return mueble;
    }

    public Double getPosicionX() {
        return x;
    }

    public Double getPosicionY() {
        return y;
    }
}
