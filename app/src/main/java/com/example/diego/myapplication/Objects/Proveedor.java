package com.example.diego.myapplication.Objects;

/**
 * Created by diego on 09/03/2018.
 */

public class Proveedor {
    private String Telefono;
    private String Nombre;
    private String Poblacion;
    private String Fecha_Alta;
    private String Razon_Social;
    private String Email;
    private String Direccion;
    private String NIF;
    private String Provincia;
    private String CP;

    public Proveedor() {
    }

    public Proveedor(String CP, String nombre, String telefono, String direccion, String email, String fecha_Alta, String NIF, String razon_Social, String provincia, String poblacion) {
        this.CP = CP;
        Nombre = nombre;
        Telefono = telefono;
        Direccion = direccion;
        Email = email;
        Fecha_Alta = fecha_Alta;
        this.NIF = NIF;
        Razon_Social = razon_Social;
        Provincia = provincia;
        Poblacion = poblacion;
    }
    public String getCP() {return CP;}

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
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

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPoblacion() {
        return Poblacion;
    }

    public void setPoblacion(String poblacion) {
        Poblacion = poblacion;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getRazon_Social() {
        return Razon_Social;
    }

    public void setRazon_Social(String razon_Social) {
        Razon_Social = razon_Social;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) { Telefono = telefono; }

    @Override
    public String toString() {
        return "Proveedor{" +
                "CP='" + CP + '\'' +
                ", Direccion='" + Direccion + '\'' +
                ", Email='" + Email + '\'' +
                ", Fecha_Alta='" + Fecha_Alta + '\'' +
                ", NIF='" + NIF + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Poblacion='" + Poblacion + '\'' +
                ", Provincia='" + Provincia + '\'' +
                ", Razon_Social='" + Razon_Social + '\'' +
                ", Telefono='" + Telefono + '\'' +
                '}';
    }
}
