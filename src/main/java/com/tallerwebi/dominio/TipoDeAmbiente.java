package com.tallerwebi.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TipoDeAmbiente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  public TipoDeAmbiente() {}

  public TipoDeAmbiente(String nombre) {
    this.nombre = nombre;
  }

  public static List<String> nombresConocidos() {
    List<String> nombres = new ArrayList<>();
    nombres.add("Living");
    nombres.add("Habitación");
    nombres.add("Pasillo");
    nombres.add("Cocina");
    nombres.add("Comedor");
    return nombres;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Override
  public boolean equals(Object objeto) {
    if (this == objeto) {
      return true;
    }
    if (!(objeto instanceof TipoDeAmbiente)) {
      return false;
    }
    TipoDeAmbiente otro = (TipoDeAmbiente) objeto;
    if (this.nombre == null) {
      return otro.nombre == null;
    }
    return this.nombre.equals(otro.nombre);
  }

  @Override
  public int hashCode() {
    if (this.nombre == null) {
      return 0;
    }
    return this.nombre.hashCode();
  }
}
