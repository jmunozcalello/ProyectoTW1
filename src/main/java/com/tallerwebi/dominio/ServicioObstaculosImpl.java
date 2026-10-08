package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public class ServicioObstaculosImpl implements ServicioObstaculos {

  @Override
  public void validarObstaculos(Ambiente ambiente) {
    for (Obstaculo obstaculo : ambiente.getObstaculos()) {
      obstaculo.validarEn(ambiente);
    }
  }
}
