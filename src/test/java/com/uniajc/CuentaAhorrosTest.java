package com.uniajc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

// NUEVO: esta clase estaba vacía; ahora cubre las reglas de la cuenta de ahorros.
class CuentaAhorrosTest {

    private static final float DELTA = 0.1f;

    @Test
    void cuentaConSaldoMinimoEstaActiva() {
        assertTrue(new CuentaAhorros(10000f, 0.12f).isActiva());
    }

    @Test
    void cuentaPorDebajoDelMinimoEstaInactiva() {
        assertFalse(new CuentaAhorros(9999f, 0.12f).isActiva());
    }

    @Test
    void retiroQueBajaDelMinimoDesactivaLaCuenta() {
        CuentaAhorros cuenta = new CuentaAhorros(15000f, 0.12f);

        cuenta.retirar(6000f);

        assertEquals(9000f, cuenta.getSaldo(), DELTA);
        assertFalse(cuenta.isActiva());
    }

    @Test
    void cuentaInactivaNoPermiteRetiros() {
        CuentaAhorros cuenta = new CuentaAhorros(5000f, 0.12f);

        cuenta.retirar(1000f);

        assertEquals(5000f, cuenta.getSaldo(), DELTA);
        assertEquals(0, cuenta.getNumeroRetiros());
    }

    @Test
    void cuentaInactivaPuedeConsignarYSeReactiva() {
        CuentaAhorros cuenta = new CuentaAhorros(5000f, 0.12f);

        cuenta.consignar(6000f);

        assertEquals(11000f, cuenta.getSaldo(), DELTA);
        assertTrue(cuenta.isActiva());
    }

    @Test
    void consignarCantidadNegativaLanzaExcepcion() {
        CuentaAhorros cuenta = new CuentaAhorros(15000f, 0.12f);

        assertThrows(IllegalArgumentException.class, () -> cuenta.consignar(-5000f));

        assertEquals(15000f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void escenarioDelMainConsignarYRetirar() {
        CuentaAhorros cuenta = new CuentaAhorros(15000f, 0.12f);

        cuenta.consignar(5000f);
        cuenta.retirar(12000f);

        assertEquals(8000f, cuenta.getSaldo(), DELTA);
        assertFalse(cuenta.isActiva());
    }

    @Test
    void hastaCuatroRetirosNoCobranComision() {
        CuentaAhorros cuenta = new CuentaAhorros(100000f, 0.12f);
        for (int i = 0; i < 4; i++) {
            cuenta.retirar(1000f);
        }

        cuenta.extractoMensual();

        // 96.000 * 1.01 = 96.960 (sin comisión)
        assertEquals(96960f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void cadaRetiroExcedenteCobraComision() {
        CuentaAhorros cuenta = new CuentaAhorros(100000f, 0.12f);
        for (int i = 0; i < 6; i++) {
            cuenta.retirar(1000f);
        }

        cuenta.extractoMensual();

        // 94.000 - (2 excedentes * 1.000) = 92.000; * 1.01 = 92.920
        assertEquals(92920f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void comisionNoSeAcumulaEntreMeses() {
        CuentaAhorros cuenta = new CuentaAhorros(100000f, 0f);
        for (int i = 0; i < 5; i++) {
            cuenta.retirar(1000f);
        }
        cuenta.extractoMensual(); // 95.000 - 1.000 = 94.000

        cuenta.extractoMensual(); // sin retiros nuevos: no cobra de nuevo

        assertEquals(94000f, cuenta.getSaldo(), DELTA);
    }

    @Test
    void extractoMensualPuedeDesactivarLaCuenta() {
        CuentaAhorros cuenta = new CuentaAhorros(10500f, 0.12f);
        for (int i = 0; i < 5; i++) {
            cuenta.retirar(100f);
        }
        assertTrue(cuenta.isActiva()); // saldo = 10.000

        cuenta.extractoMensual(); // (10.000 - 1.000) * 1.01 = 9.090

        assertEquals(9090f, cuenta.getSaldo(), DELTA);
        assertFalse(cuenta.isActiva());
    }
}
