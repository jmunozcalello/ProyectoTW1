package com.tallerwebi.dominio;

public final class Color {

  private final String nombre;
  private final String hex;

  public Color(String nombre, String hex) {
    this.nombre = nombre;
    this.hex = hex;
  }

  public String getNombre() {
    return this.nombre;
  }

  public String getHex() {
    return this.hex;
  }
}
