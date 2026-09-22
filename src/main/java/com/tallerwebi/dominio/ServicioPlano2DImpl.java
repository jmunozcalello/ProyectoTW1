package com.tallerwebi.dominio;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ServicioPlano2DImpl implements ServicioPlano2D {
    @Override
    public Plano generarPlano(Double ancho, Double largo, Mueble mueble1, Mueble mueble2) {
        return new Plano(ancho,largo,List.of(mueble1, mueble2));
    }
}
