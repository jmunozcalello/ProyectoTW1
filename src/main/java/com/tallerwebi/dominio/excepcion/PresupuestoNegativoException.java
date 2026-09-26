package com.tallerwebi.dominio.excepcion;

public class PresupuestoNegativoException extends Exception {

  public PresupuestoNegativoException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 1L;
}
