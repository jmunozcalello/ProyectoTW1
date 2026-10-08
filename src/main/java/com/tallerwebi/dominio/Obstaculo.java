package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ValidacionException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Elemento fijo apoyado contra un muro del ambiente (radiador, toma de corriente, etc.). */
@Embeddable
public class Obstaculo {

  @Enumerated(EnumType.STRING)
  private TipoDeObstaculo tipo;

  @Enumerated(EnumType.STRING)
  private Muro muro;

  private Double posicion;
  private Double ancho;
  private Double profundidad;

  public Obstaculo() {}

  public Obstaculo(
    TipoDeObstaculo tipo,
    Muro muro,
    Double posicion,
    Double ancho,
    Double profundidad
  ) {
    this.tipo = tipo;
    this.muro = muro;
    this.posicion = posicion;
    this.ancho = ancho;
    this.profundidad = profundidad;
  }

  public TipoDeObstaculo getTipo() {
    return tipo;
  }

  public Muro getMuro() {
    return muro;
  }

  public Double getPosicion() {
    return posicion;
  }

  public Double getAncho() {
    return ancho;
  }

  public Double getProfundidad() {
    return profundidad;
  }

  public String getDescripcion() {
    return tipo.getNombre() + " (muro " + muro.getNombre() + ")";
  }

  public ZonaNoDisponible zonaNoDisponible(Ambiente ambiente) {
    return switch (muro) {
      case SUPERIOR -> new ZonaNoDisponible(posicion, 0.0, ancho, profundidad);
      case DERECHO -> new ZonaNoDisponible(
        ambiente.getAncho() - profundidad,
        posicion,
        profundidad,
        ancho
      );
      case INFERIOR -> new ZonaNoDisponible(
        posicion,
        ambiente.getLargo() - profundidad,
        ancho,
        profundidad
      );
      case IZQUIERDO -> new ZonaNoDisponible(0.0, posicion, profundidad, ancho);
    };
  }

  public boolean colisionaCon(MuebleUbicado ubicado, Ambiente ambiente) {
    return zonaNoDisponible(ambiente).seSolapaCon(ubicado);
  }

  private void validarQueEntraEnElMuro(Double largoDelMuro) {
    if (ancho > largoDelMuro + ZonaNoDisponible.TOLERANCIA) {
      throw new ValidacionException(
        tipo.getNombre() +
        " mide " +
        enMetros(ancho) +
        " de ancho y no entra en el muro " +
        muro.getNombre() +
        ", que mide " +
        enMetros(largoDelMuro) +
        "."
      );
    }
    if (posicion + ancho > largoDelMuro + ZonaNoDisponible.TOLERANCIA) {
      throw new ValidacionException(
        tipo.getNombre() +
        " se sale del muro " +
        muro.getNombre() +
        ": va de " +
        enMetros(posicion) +
        " a " +
        enMetros(posicion + ancho) +
        " contando desde " +
        muro.getOrigenDeLaPosicion() +
        " y el muro mide " +
        enMetros(largoDelMuro) +
        ". Ubicalo como máximo en la posición " +
        enMetros(largoDelMuro - ancho) +
        "."
      );
    }
  }

  private static String enMetros(double medida) {
    DecimalFormat formato = new DecimalFormat(
      "0.##",
      DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-AR"))
    );
    return formato.format(medida) + " m";
  }

  public void validarEn(Ambiente ambiente) {
    if (tipo == null || muro == null) {
      throw new ValidacionException("Elegí el tipo y el muro de cada obstáculo");
    }
    validarMedidas();
    validarQueEntraEnElMuro(muro.largoEn(ambiente));
    if (profundidad >= muro.profundidadDisponibleEn(ambiente)) {
      throw new ValidacionException(tipo.getNombre() + " es más profundo que el ambiente");
    }
  }

  private void validarMedidas() {
    if (!esPositiva(ancho) || !esPositiva(profundidad)) {
      throw new ValidacionException(
        "Las medidas de " + tipo.getNombre() + " deben ser mayores a 0"
      );
    }
    if (posicion == null || posicion < 0) {
      throw new ValidacionException("La posición de " + tipo.getNombre() + " debe ser 0 o mayor");
    }
  }

  private static boolean esPositiva(Double medida) {
    return medida != null && medida > 0;
  }
}
