package com.uniajc;

public class CuentaAhorros extends Cuenta {

    protected boolean activa;

    CuentaAhorros(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
    }

    @Override 
    public void consignar(float cantidad) { }

    @Override
    public void retirar(float cantidad) { }

    @Override
    public void calcularInteres() { }

    @Override
    public void extractoMensual() { }

    public void imprimir() {}
}
