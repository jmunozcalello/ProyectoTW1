package com.tallerwebi.dominio;

import java.util.List;

public class Plano {

    private Ambiente ambiente;
    private List<MuebleUbicado> muebles;
    private List<Mueble> mueblesExcluidos;
    private List<String> motivosDeExclusion;
    private String mensaje;

    public Plano(
            Ambiente ambiente,
            List<MuebleUbicado> muebles,
            List<Mueble> mueblesExcluidos,
            List<String> motivosDeExclusion
    ) {
        this.ambiente = ambiente;
        this.muebles = muebles;
        this.mueblesExcluidos = mueblesExcluidos;
        this.motivosDeExclusion = motivosDeExclusion;
    }

    public List<MuebleUbicado> getMuebles() {
        return muebles;
    }

    public List<Mueble> getMueblesExcluidos() {
        return mueblesExcluidos;
    }

    public List<String> getMotivosDeExclusion() {
        return motivosDeExclusion;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
