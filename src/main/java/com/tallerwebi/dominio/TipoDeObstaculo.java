package com.tallerwebi.dominio;

/**
 * Elementos fijos de un muro contra los que no se puede apoyar un mueble. Las medidas sugeridas
 * solo precargan el formulario; el usuario puede corregirlas.
 */
public enum TipoDeObstaculo {
  RADIADOR("Radiador", 0.8, 0.15),
  TOMA_DE_CORRIENTE("Toma de corriente", 0.15, 0.1),
  OTRO("Otro", 0.5, 0.2);

  private final String nombre;
  private final Double anchoSugerido;
  private final Double profundidadSugerida;

  TipoDeObstaculo(String nombre, Double anchoSugerido, Double profundidadSugerida) {
    this.nombre = nombre;
    this.anchoSugerido = anchoSugerido;
    this.profundidadSugerida = profundidadSugerida;
  }

  public String getNombre() {
    return nombre;
  }

  public Double getAnchoSugerido() {
    return anchoSugerido;
  }

  public Double getProfundidadSugerida() {
    return profundidadSugerida;
  }
}
