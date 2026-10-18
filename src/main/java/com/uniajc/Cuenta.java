package com.uniajc;


public abstract class Cuenta {

    private float saldo;
    private int numeroConsignaciones = 0;
    private int numeroRetiros = 0;
    private final float tasaAnual;
    private float comisionMensual;

    Cuenta(float saldo, float tasaAnual) {
        this.saldo = saldo;
        this.tasaAnual = tasaAnual;
    }

    // CAMBIO: la misma validación sirve para consignar y retirar (antes retirar
    // aceptaba cantidades negativas y el saldo AUMENTABA).
    protected void validarCantidad(float cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
    }

    public void consignar(float cantidad) {
        validarCantidad(cantidad);
        saldo += cantidad;
        numeroConsignaciones++;
    }

    public void retirar(float cantidad) {
        validarCantidad(cantidad);
        if (cantidad <= saldo) {
            saldo -= cantidad;
            numeroRetiros++;
        }
    }

    // CAMBIO: tres líneas -> una.
    public void calcularInteres() {
        saldo += saldo * tasaAnual / 12;
    }

    // CAMBIO: al cerrar el mes se reinician los contadores; antes los retiros
    // del mes anterior se volvían a cobrar en el siguiente extracto.
    public void extractoMensual() {
        saldo -= comisionMensual;
        calcularInteres();
        numeroConsignaciones = 0;
        numeroRetiros = 0;
    }

    public void imprimir() {
        System.out.println("Saldo: " + saldo);
        System.out.println("Número de consignaciones: " + numeroConsignaciones);
        System.out.println("Número de retiros: " + numeroRetiros);
        System.out.println("Numero de transacciones: " + (numeroConsignaciones + numeroRetiros));
        System.out.println("Tasa anual: " + tasaAnual);
        System.out.println("Comisión mensual: " + comisionMensual);
    }

    // NUEVO (Encapsulación): getters, y un setter protegido para que
    // CuentaAhorros pueda fijar su comisión (una comisión negativa aumentaría el saldo).
    public float getSaldo() {
        return saldo;
    }

    public float getTasaAnual() {
        return tasaAnual;
    }

    public int getNumeroConsignaciones() {
        return numeroConsignaciones;
    }

    public int getNumeroRetiros() {
        return numeroRetiros;
    }

    public float getComisionMensual() {
        return comisionMensual;
    }

    protected void setComisionMensual(float comisionMensual) {
        validarCantidad(comisionMensual);
        this.comisionMensual = comisionMensual;
    }
}
