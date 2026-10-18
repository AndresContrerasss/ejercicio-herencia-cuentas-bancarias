package com.uniajc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

// NUEVO: esta clase estaba vacía. Los casos que estaban como comentarios en
// CuentaCorriente ahora son tests ejecutables.
class CuentaCorrienteTest {

    private static final float DELTA = 0.1f;

    // ---- Consignar ----

    @Test
    void consignarCaso1SinSobregiroSumaAlSaldo() {
        CuentaCorriente cuenta = new CuentaCorriente(10000f, 0.12f);

        cuenta.consignar(20000f);

        assertEquals(30000f, cuenta.getSaldo(), DELTA);
        assertEquals(0f, cuenta.getSobregiro(), DELTA);
    }

    @Test
    void consignarCaso2PagaSobregiroYElRestoVaAlSaldo() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.retirar(10000f); // sobregiro = 10.000

        cuenta.consignar(20000f);

        assertEquals(10000f, cuenta.getSaldo(), DELTA);
        assertEquals(0f, cuenta.getSobregiro(), DELTA);
    }

    @Test
    void consignarCaso3PagoParcialYLuegoTotalDelSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.retirar(10000f); // sobregiro = 10.000

        cuenta.consignar(5000f);
        assertEquals(0f, cuenta.getSaldo(), DELTA);
        assertEquals(5000f, cuenta.getSobregiro(), DELTA);

        cuenta.consignar(20000f);
        assertEquals(15000f, cuenta.getSaldo(), DELTA);
        assertEquals(0f, cuenta.getSobregiro(), DELTA);
    }

    @Test
    void consignacionParcialDelSobregiroTambienSeCuenta() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.retirar(10000f);

        cuenta.consignar(5000f);

        // Bug corregido: antes esta consignación no se contaba.
        assertEquals(1, cuenta.getNumeroConsignaciones());
    }

    @Test
    void consignarCantidadNegativaLanzaExcepcionYNoCambiaElSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente(0f, 0.12f);
        cuenta.retirar(10000f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.consignar(-500f));

        assertEquals(10000f, cuenta.getSobregiro(), DELTA);
        assertEquals(0, cuenta.getNumeroConsignaciones());
    }

    // ---- Retirar ----

    @Test
    void retirarCaso1MasDelSaldoGeneraSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente(100000f, 0.12f);

        cuenta.retirar(120000f);

        assertEquals(0f, cuenta.getSaldo(), DELTA);
        assertEquals(20000f, cuenta.getSobregiro(), DELTA);
    }

    @Test
    void retirarCaso2ConSaldoSuficienteNoGeneraSobregiro() {
        CuentaCorriente cuenta = new CuentaCorriente(100000f, 0.12f);

        cuenta.retirar(80000f);

        assertEquals(20000f, cuenta.getSaldo(), DELTA);
        assertEquals(0f, cuenta.getSobregiro(), DELTA);
    }

    @Test
    void retirosConSobregiroSeAcumulan() {
        CuentaCorriente cuenta = new CuentaCorriente(100000f, 0.12f);

        cuenta.retirar(120000f); // sobregiro 20.000
        cuenta.retirar(5000f);   // sobregiro 25.000 (antes se sobrescribía con 5.000)

        assertEquals(25000f, cuenta.getSobregiro(), DELTA);
        assertEquals(0f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void retirarContabilizaCadaRetiro() {
        CuentaCorriente cuenta = new CuentaCorriente(100000f, 0.12f);

        cuenta.retirar(120000f);
        cuenta.retirar(1000f);

        assertEquals(2, cuenta.getNumeroRetiros());
    }

    @Test
    void retirarCantidadNegativaLanzaExcepcionYNoCambiaLaCuenta() {
        CuentaCorriente cuenta = new CuentaCorriente(1000f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(-10f));

        assertEquals(1000f, cuenta.getSaldo(), DELTA);
        assertEquals(0f, cuenta.getSobregiro(), DELTA);
    }

    // ---- Extracto ----

    @Test
    void extractoMensualAplicaInteresSobreElSaldo() {
        CuentaCorriente cuenta = new CuentaCorriente(1200f, 0.12f);

        cuenta.extractoMensual();

        assertEquals(1212f, cuenta.getSaldo(), DELTA);
    }
}
