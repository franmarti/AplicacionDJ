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
    private String latitud;
    private String longitud;


    public Establecimiento (){
        //Constructor por defecto
    }
    private Bitmap imagen;

    public Establecimiento(int id, String nombre, String tipoMusica, String ciudad, String latitud, String longitud, Bitmap imagen) {

        this.id = id;
        this.nombre = nombre;
        this.tipoMusica = tipoMusica;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String direccion) {
        this.latitud = direccion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
