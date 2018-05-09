package com.example.diego.myapplication.Objects;

/**
 * Created by diego on 26/02/2018.
 */

public class Cliente {
    private String CP;
    private String Nombre;
    private String Telefono;
    private String Direccion;
    private String Formalidad;
    private String Email;
    private String Fecha_Alta;
    private String nif;
    private String Razon_Social;
    private String Provincia;
    private String Poblacion;

    public Cliente() {
    }

    public Cliente(String CP, String nombre, String telefono, String direccion, String formalidad, String email, String fecha_Alta, String NIF, String razon_Social, String provincia, String poblacion) {
        this.CP = CP;
        Nombre = nombre;
        Telefono = telefono;
        Direccion = direccion;
        Formalidad = formalidad;
        Email = email;
        Fecha_Alta = fecha_Alta;
        this.nif = NIF;
        Razon_Social = razon_Social;
        Provincia = provincia;
        Poblacion = poblacion;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getFormalidad() {
        return Formalidad;
    }

    public void setFormalidad(String formalidad) {
        Formalidad = formalidad;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFecha_Alta() {
        return Fecha_Alta;
    }

    public void setFecha_Alta(String fecha_Alta) {
        Fecha_Alta = fecha_Alta;
    }

    public String getNif() {
        return nif;
    }

    public void setNIF(String NIF) {
        this.nif = NIF;
    }

    public String getRazon_Social() {
        return Razon_Social;
    }

    public void setRazon_Social(String razon_Social) {
        Razon_Social = razon_Social;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getPoblacion() {
        return Poblacion;
    }

    public void setPoblacion(String poblacion) {
        Poblacion = poblacion;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "CP='" + CP + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Formalidad='" + Formalidad + '\'' +
                ", Email='" + Email + '\'' +
                ", Fecha_Alta='" + Fecha_Alta + '\'' +
                ", nif='" + nif + '\'' +
                ", Razon_Social='" + Razon_Social + '\'' +
                ", Provincia='" + Provincia + '\'' +
                ", Poblacion='" + Poblacion + '\'' +
                '}';
    }
}
