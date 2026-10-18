package com.uniajc;



public class CuentaCorriente extends Cuenta {

    private float sobregiro = 0f;

    CuentaCorriente(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);

    }
      // Caso 1:
    // saldo = 10.000
    // consignarMonto = 20.000
    // sobregiro = 0
    // saldo final = 30.000

    // Caso 2:
    // saldo = 0
    // sobregiro = 10.000
    // consignarMonto = 20.000
    // saldo final = 10.000

    // Caso 3:
    // saldo = 0
    // sobregiro = 10.000
    // consignarMonto = 5.000
    // saldo final = 0
    // sobregiro final = 5.000
    // cosingarMonto = 20.000
    // sobregiro final = 0
    // saldo final = 15.000

    // CAMBIO: primero se paga el sobregiro y el resto va al saldo. Antes, una
    // consignación menor al sobregiro no se contaba como consignación.
    // Se valida primero para que una cantidad negativa no aumente el sobregiro.
    @Override
    public void consignar(float cantidad) {
        validarCantidad(cantidad);
        float pago = Math.min(cantidad, sobregiro);
        sobregiro -= pago;
        super.consignar(cantidad - pago);
    }
    
    // caso 1;
    // saldo = 100.000
    // retirarMonto = 120.000
    // sobregiro = 20.000

    // caso 2:
    // saldo = 100.000
    // retirarMonto = 80.000
    // sobregiro = 0
    // saldo final = 20.000

    // CAMBIO (bug): antes "sobregiro = ..." sobrescribía el sobregiro anterior;
    // ahora se acumula. Lo que no alcanza el saldo se registra como sobregiro.
    @Override
    public void retirar(float cantidad) {
        float faltante = Math.max(0, cantidad - getSaldo());
        super.retirar(cantidad - faltante);
        sobregiro += faltante;
    }
    

    // CAMBIO: se eliminó extractoMensual() porque solo llamaba a super (código inútil).

    @Override
    public void imprimir() {
        super.imprimir();
        System.out.println("Sobregiro: " + sobregiro);
    }

    public float getSobregiro() {
        return sobregiro;
    }
}

