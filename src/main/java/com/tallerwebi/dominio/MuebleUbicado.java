package com.tallerwebi.dominio;

import jakarta.persistence.*;

@Entity
public class MuebleUbicado {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Un Mueble del catalogo puede aparecer en muchos MuebleUbicado (uno por cada Plano donde se
  // ubico, e incluso varias veces en el mismo Plano). La relacion es many-to-one, no one-to-one.
  @ManyToOne
  @JoinColumn(name = "mueble_id")
  private Mueble mueble;

  private Double coordenadaX;
  private Double coordenadaY;
  private boolean enConflicto;

  public MuebleUbicado(Mueble mueble, Double coordenadaX, Double coordenadaY) {
    this(mueble, coordenadaX, coordenadaY, false);
  }

  public MuebleUbicado(Mueble mueble, Double coordenadaX, Double coordenadaY, boolean enConflicto) {
    this.mueble = mueble;
    this.coordenadaX = coordenadaX;
    this.coordenadaY = coordenadaY;
    this.enConflicto = enConflicto;
  }

  public MuebleUbicado() {}

  public Mueble getMueble() {
    return mueble;
  }

  public Double getPosicionX() {
    return coordenadaX;
  }

  public Double getPosicionY() {
    return coordenadaY;
  }

  public boolean isEnConflicto() {
    return enConflicto;
  }
}
