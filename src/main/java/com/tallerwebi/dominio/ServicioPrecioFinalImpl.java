package com.tallerwebi.dominio;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServicioPrecioFinalImpl implements ServicioPrecioFinal {
    @Override
    public Double calcularPrecio(double precioBase, String tipoCliente) {
        return 0.0;
    }
}
