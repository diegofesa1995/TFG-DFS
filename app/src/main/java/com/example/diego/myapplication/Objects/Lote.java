package com.example.diego.myapplication.Objects;

public class Lote {
    private Double Cantidad;
    private Integer UnidadesenCaja;
    private String subUnidadPeso;
    private Double CantidadTotal;
    private String Fecha;
    private String Proveedor;
    private Double margenBeneficio;
    private Double precioCompra;
    private Double precioVenta;
    private Double pesoSubUnidad;
    private Double pesoUnidad;


    public Lote() {
    }

    public Lote(Double cantidad, Integer unidadesenCaja, String subUnidadPeso, Double cantidadTotal, String fecha, String proveedor, Double margenBeneficio, Double precioCompra, Double precioVenta, Double pesoSubUnidad, Double pesoUnidad) {
        Cantidad = cantidad;
        UnidadesenCaja = unidadesenCaja;
        this.subUnidadPeso = subUnidadPeso;
        CantidadTotal = cantidadTotal;
        Fecha = fecha;
        Proveedor = proveedor;
        this.margenBeneficio = margenBeneficio;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.pesoSubUnidad = pesoSubUnidad;
        this.pesoUnidad = pesoUnidad;
    }

    public Double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Double cantidad) {
        Cantidad = cantidad;
    }

    public Integer getUnidadesenCaja() {
        return UnidadesenCaja;
    }

    public void setUnidadesenCaja(Integer unidadesenCaja) {
        UnidadesenCaja = unidadesenCaja;
    }

    public String getSubUnidadPeso() {
        return subUnidadPeso;
    }

    public void setSubUnidadPeso(String subUnidadPeso) {
        this.subUnidadPeso = subUnidadPeso;
    }

    public Double getCantidadTotal() {
        return CantidadTotal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        CantidadTotal = cantidadTotal;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String proveedor) {
        Proveedor = proveedor;
    }

    public Double getMargenBeneficio() {
        return margenBeneficio;
    }

    public void setMargenBeneficio(Double margenBeneficio) {
        this.margenBeneficio = margenBeneficio;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getPesoSubUnidad() {
        return pesoSubUnidad;
    }

    public void setPesoSubUnidad(Double pesoSubUnidad) {
        this.pesoSubUnidad = pesoSubUnidad;
    }

    public Double getPesoUnidad() {
        return pesoUnidad;
    }

    public void setPesoUnidad(Double pesoUnidad) {
        this.pesoUnidad = pesoUnidad;
    }

    @Override
    public String toString() {
        return "Lote{" +
                "Cantidad=" + Cantidad +
                ", UnidadesenCaja=" + UnidadesenCaja +
                ", subUnidadPeso='" + subUnidadPeso + '\'' +
                ", CantidadTotal=" + CantidadTotal +
                ", Fecha='" + Fecha + '\'' +
                ", Proveedor='" + Proveedor + '\'' +
                ", margenBeneficio=" + margenBeneficio +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", pesoSubUnidad=" + pesoSubUnidad +
                ", pesoUnidad=" + pesoUnidad +
                '}';
    }
}
