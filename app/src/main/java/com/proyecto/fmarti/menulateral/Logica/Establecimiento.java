package com.proyecto.fmarti.menulateral.Logica;

import android.graphics.Bitmap;

/**
 * Created by fmarti on 25/04/2016.
 */
public class Establecimiento {
    private int id;
    private String nombre;
    private String tipoMusica;
    private String ciudad;
    private String descripcion;
    private String direccion;


    public Establecimiento (){
        //Constructor por defecto
    }
    private Bitmap imagen;

    public Establecimiento(int id, String nombre, String tipoMusica, String descripcion, String ciudad, String direccion, Bitmap imagen) {

        this.id = id;
        this.nombre = nombre;
        this.tipoMusica = tipoMusica;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMusica() {
        return tipoMusica;
    }

    public void setTipoMusica(String musica) {
        this.tipoMusica = musica;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
