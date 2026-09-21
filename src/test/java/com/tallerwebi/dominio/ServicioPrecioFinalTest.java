package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.TipoClienteInvalidoException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioPrecioFinalTest {


    /**
     * si un cliente es de tipo normal el precio final es el precio base
     * si un cliente es vip, el precio final es el precio base con un 10% de descuento
     * si un cliente es premium, el precio final es el precio base con un 20% de descuento
     * si el tipo de cliente no existe tiene que lanzar una excepcion
     */

    // un test unitario prueba una sola clase en particular

    ServicioPrecioFinal servicioPrecioFinal = new ServicioPrecioFinalImpl();
    private final Double PRECIO_BASE = 1000.00;

    @Test
    public void deberiaDevolverElMismoPrecioParaClienteNormal() {

        //preparacion --> givenTengoUnCliente();

        //ejecucion --> when
        Double precioCalculado = whenCalculoPrecioFinalParaCliente("NORMAL");

        //validacion --> then
        thenElPrecioFinalEs(1000.00, precioCalculado);
    }

    @Test
    public void deberiaDevolverPrecioCon10PorcientoDeDescuentoSiElClienteEsVip() {

        // ejecucion --> when
        Double precioCalculado = whenCalculoPrecioFinalParaCliente("VIP");

        //validacion --> then
        thenElPrecioFinalEs(900.00, precioCalculado);
    }

    @Test
    public void deberiaDevolverPrecioCon20PorcientoDeDescuentoCuandoEsClientePremium() {

       Double precioCalculado = whenCalculoPrecioFinalParaCliente("PREMIUM");
        thenElPrecioFinalEs(800.00,precioCalculado);


    }

    @Test
    public void deberiaLanzarUnaExceptionSiElTipoDeClienteNoExiste() {

        assertThrows(TipoClienteInvalidoException.class,
                () -> whenCalculoPrecioFinalParaCliente("TIPO_NO_EXISTE")
        );

    }

    private void thenElPrecioFinalEs(Double precioEsperado, Double precioCalculado) {
        assertThat(precioCalculado, equalTo(precioEsperado));
    }

    private Double whenCalculoPrecioFinalParaCliente(String tipoCliente) {
        Double precioFinal = servicioPrecioFinal.calcularPrecio(PRECIO_BASE, tipoCliente);
        return precioFinal;
    }


}
