package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPlano2D {
  Plano generarPlano(Ambiente ambiente, List<Mueble> muebles);

  boolean estaDentroDelPerimetro(MuebleUbicado muebleUbicado, Ambiente ambiente);
}
