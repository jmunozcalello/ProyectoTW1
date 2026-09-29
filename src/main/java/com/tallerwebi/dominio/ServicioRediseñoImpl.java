package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.stereotype.Service;

@Service("servicioRediseño")
public class ServicioRediseñoImpl implements ServicioRediseño {

  @Override
  public Recomendacion recomendar(Estilo estilo) {
    return switch (estilo) {
      case Retro -> new Recomendacion(
        estilo,
        List.of(
          new Color("Mostaza", "#D4A017"),
          new Color("Oliva", "#6B7B3A"),
          new Color("Terracota", "#C1663F")
        ),
        Iluminacion.CALIDA
      );
      case Minimalista -> new Recomendacion(
        estilo,
        List.of(
          new Color("Blanco", "#F5F5F2"),
          new Color("Gris perla", "#C9C9C4"),
          new Color("Carbón", "#2B2B2B")
        ),
        Iluminacion.INTERMEDIA
      );
      case Japandi -> new Recomendacion(
        estilo,
        List.of(
          new Color("Arena", "#D9CBB8"),
          new Color("Madera clara", "#C8A87C"),
          new Color("Salvia", "#9CAF94")
        ),
        Iluminacion.CALIDA
      );
      case Industrial -> new Recomendacion(
        estilo,
        List.of(
          new Color("Cemento", "#8C8C86"),
          new Color("Hierro", "#33322E"),
          new Color("Ladrillo", "#A0522D")
        ),
        Iluminacion.FRIA
      );
      case Boho -> new Recomendacion(
        estilo,
        List.of(
          new Color("Terracota", "#C1663F"),
          new Color("Mostaza arena", "#E0B252"),
          new Color("Turquesa", "#4FA3A5")
        ),
        Iluminacion.CALIDA
      );
      case Escandinavo -> new Recomendacion(
        estilo,
        List.of(
          new Color("Blanco roto", "#F2F0EB"),
          new Color("Celeste", "#A8C3D1"),
          new Color("Gris cálido", "#C4B8A8")
        ),
        Iluminacion.INTERMEDIA
      );
    };
  }
}
