package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ambiente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double ancho;
  private Double largo;

  @ManyToOne(optional = false)
  @JoinColumn(name = "tipo_de_ambiente_id", nullable = false)
  private TipoDeAmbiente tipoDeAmbiente;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  public Ambiente() {}

  public Ambiente(Double ancho, Double largo) {
    this.ancho = ancho;
    this.largo = largo;
  }

  public Ambiente(Double ancho, Double largo, TipoDeAmbiente tipoDeAmbiente) {
    this.ancho = ancho;
    this.largo = largo;
    this.tipoDeAmbiente = tipoDeAmbiente;
  }

  public Long getId() {
    return id;
  }

  public Double getAncho() {
    return ancho;
  }

  public Double getLargo() {
    return largo;
  }

  public TipoDeAmbiente getTipoDeAmbiente() {
    return tipoDeAmbiente;
  }

  public void setTipoDeAmbiente(TipoDeAmbiente tipoDeAmbiente) {
    this.tipoDeAmbiente = tipoDeAmbiente;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
}
