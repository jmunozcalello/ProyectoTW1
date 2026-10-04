package com.tallerwebi.dominio;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Plano {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  private Ambiente ambiente;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "plano_id")
  @OrderColumn(name = "posicion")
  private List<MuebleUbicado> muebles;

  @ManyToMany
  @JoinTable(name = "plano_muebles_excluidos")
  private List<Mueble> mueblesExcluidos;

  @ElementCollection
  @CollectionTable(name = "plano_motivos_exclusion", joinColumns = @JoinColumn(name = "plano_id"))
  @Column(name = "motivo")
  private List<String> motivosDeExclusion;

  private String mensaje;

  public Plano() {}

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
