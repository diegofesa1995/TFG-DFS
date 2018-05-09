package com.example.diego.myapplication.Objects;

import android.support.annotation.NonNull;
import com.example.diego.myapplication.Objects.Lote;

import java.util.HashMap;

/**
 * Created by diego on 08/03/2018.
 */

public class ProductoStock {
    private String Nombre;
    private Double cantidadTotal;
    private Integer IVA;
    private HashMap<String,Lote> lotes;
    private Double PrecioMaximo;
    private Double precioMinimo;
    private String unidadPeso;

    public ProductoStock() {
    }

    public ProductoStock(String nombre, Double cantidadTotal, Integer IVA, HashMap<String, Lote> lotes, Double precioMaximo, Double precioMinimo, String unidadPeso) {
        Nombre = nombre;
        this.cantidadTotal = cantidadTotal;
        this.IVA = IVA;
        this.lotes = lotes;
        PrecioMaximo = precioMaximo;
        this.precioMinimo = precioMinimo;
        this.unidadPeso = unidadPeso;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public Integer getIVA() {
        return IVA;
    }

    public void setIVA(Integer IVA) {
        this.IVA = IVA;
    }

    public HashMap<String, Lote> getLotes() {
        return lotes;
    }

    public void setLotes(HashMap<String, Lote> lotes) {
        this.lotes = lotes;
    }

    public Double getPrecioMaximo() {
        return PrecioMaximo;
    }

    public void setPrecioMaximo(Double precioMaximo) {
        PrecioMaximo = precioMaximo;
    }

    public Double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    @Override
    public String toString() {
        return "ProductoStock{" +
                "Nombre='" + Nombre + '\'' +
                ", cantidadTotal=" + cantidadTotal +
                ", IVA=" + IVA +
                ", lotes=" + lotes +
                ", PrecioMaximo=" + PrecioMaximo +
                ", precioMinimo=" + precioMinimo +
                ", unidadPeso='" + unidadPeso + '\'' +
                '}';
    }
}




