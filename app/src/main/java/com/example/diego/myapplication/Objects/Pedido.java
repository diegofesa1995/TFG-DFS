package com.example.diego.myapplication.Objects;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 26/02/2018.
 */

public class Pedido {
    private String CodigoCliente;
    private String RazonSocial;
    private String Direccion;
    private String Estado;
    private String Fecha;
    private String trabajador;
    private HashMap<String,ProductoVendido> Productos;
    private HashMap<String,HashMap<String,Double>> baseImponible;
    private Double totalcoste;

    public Pedido() {
    }

    public Pedido(String codigoCliente, String razonSocial, String direccion, String estado, String fecha, String trabajador, HashMap<String, ProductoVendido> productos, HashMap<String, HashMap<String, Double>> baseImponible, Double totalcoste) {
        CodigoCliente = codigoCliente;
        RazonSocial = razonSocial;
        Direccion = direccion;
        Estado = estado;
        Fecha = fecha;
        this.trabajador = trabajador;
        Productos = productos;
        this.baseImponible = baseImponible;
        this.totalcoste = totalcoste;
    }

    public String getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        CodigoCliente = codigoCliente;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public HashMap<String, ProductoVendido> getProductos() {
        return Productos;
    }

    public void setProductos(HashMap<String, ProductoVendido> productos) {
        Productos = productos;
    }

    public HashMap<String, HashMap<String, Double>> getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(HashMap<String, HashMap<String, Double>> baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Double getTotalcoste() {
        return totalcoste;
    }

    public void setTotalcoste(Double totalcoste) {
        this.totalcoste = totalcoste;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "CodigoCliente='" + CodigoCliente + '\'' +
                ", RazonSocial='" + RazonSocial + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Estado='" + Estado + '\'' +
                ", Fecha='" + Fecha + '\'' +
                ", trabajador='" + trabajador + '\'' +
                ", Productos=" + Productos +
                ", baseImponible=" + baseImponible +
                ", totalcoste=" + totalcoste +
                '}';
    }
}
