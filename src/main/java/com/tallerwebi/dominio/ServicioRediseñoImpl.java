package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service("servicioRediseño")
public class ServicioRediseñoImpl implements ServicioRediseño {

    @Override
    public Recomendacion recomendar(Estilo estilo) {
        Recomendacion recomendacion = new Recomendacion();
        switch (estilo) {
            case Retro:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Mostaza", "#D4A017"),
                                                new Color("Oliva", "#6B7B3A"),
                                                new Color("Terracota", "#C1663F")
                                        )
                                ),
                                Iluminacion.CALIDA
                        );
                break;
            case Minimalista:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Blanco", "#F5F5F2"),
                                                new Color("Gris perla", "#C9C9C4"),
                                                new Color("Carbón", "#2B2B2B")
                                        )
                                ),
                                Iluminacion.INTERMEDIA
                        );
                break;
            case Japandi:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Arena", "#D9CBB8"),
                                                new Color("Madera clara", "#C8A87C"),
                                                new Color("Salvia", "#9CAF94")
                                        )
                                ),
                                Iluminacion.CALIDA
                        );
                break;
            case Industrial:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Cemento", "#8C8C86"),
                                                new Color("Hierro", "#33322E"),
                                                new Color("Ladrillo", "#A0522D")
                                        )
                                ),
                                Iluminacion.FRIA
                        );
                break;
            case Boho:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Terracota", "#C1663F"),
                                                new Color("Mostaza arena", "#E0B252"),
                                                new Color("Turquesa", "#4FA3A5")
                                        )
                                ),
                                Iluminacion.CALIDA
                        );
                break;
            case Escandinavo:
                recomendacion =
                        new Recomendacion(
                                estilo,
                                new ArrayList<Color>(
                                        Arrays.asList(
                                                new Color("Blanco roto", "#F2F0EB"),
                                                new Color("Celeste", "#A8C3D1"),
                                                new Color("Gris cálido", "#C4B8A8")
                                        )
                                ),
                                Iluminacion.INTERMEDIA
                        );
                break;
            //problema PMD con default, un solo cambio con una excepcion
            default:
                throw new IllegalArgumentException("Estilo no soportado: " + estilo);
        }

        return recomendacion;
    }
}
