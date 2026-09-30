package com.tallerwebi.dominio.excepcion;

public class ValidacionException extends RuntimeException {

  /* Identificador para la serialización de la clase, requerido por PMD en excepciones */
  private static final long serialVersionUID = 1L;

  public ValidacionException(String message) {
    super(message);
  }
}
