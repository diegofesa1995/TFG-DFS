package com.example.diego.myapplication.Objects;

public class ProductoVendido {
    private String claveproducto;
    private String nombre;
    private Double cantidad;
    private Integer cantidadcajas;
    private Integer iva;
    private String lote;
    private Double preciounidad;
    private Double preciototal;
    private String unidadPeso;
    private String subUnidadPeso;
    private String comentario;

    public ProductoVendido() {

    }

    public ProductoVendido(String claveproducto, String nombre, Double cantidad, Integer cantidadcajas, Integer iva, String lote, Double preciounidad, Double preciototal, String unidadPeso, String subUnidadPeso, String comentario) {
        this.claveproducto = claveproducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.cantidadcajas = cantidadcajas;
        this.iva = iva;
        this.lote = lote;
        this.preciounidad = preciounidad;
        this.preciototal = preciototal;
        this.unidadPeso = unidadPeso;
        this.subUnidadPeso = subUnidadPeso;
        this.comentario = comentario;
    }

    public String getClaveproducto() {
        return claveproducto;
    }

    public void setClaveproducto(String claveproducto) {
        this.claveproducto = claveproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadcajas() {
        return cantidadcajas;
    }

    public void setCantidadcajas(Integer cantidadcajas) {
        this.cantidadcajas = cantidadcajas;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Double getPreciounidad() {
        return preciounidad;
    }

    public void setPreciounidad(Double preciounidad) {
        this.preciounidad = preciounidad;
    }

    public Double getPreciototal() {
        return preciototal;
    }

    public void setPreciototal(Double preciototal) {
        this.preciototal = preciototal;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public String getSubUnidadPeso() {
        return subUnidadPeso;
    }

    public void setSubUnidadPeso(String subUnidadPeso) {
        this.subUnidadPeso = subUnidadPeso;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
