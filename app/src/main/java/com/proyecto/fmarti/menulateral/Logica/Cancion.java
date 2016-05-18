package com.proyecto.fmarti.menulateral.Logica;

/**
 * Created by fmarti on 02/05/2016.
 */
public class Cancion {
    private int idCancion;
    private String autor;
    private String titulo;
    private String album;
    private String genero;
    private String pedida;
    private int idListaCancion;
    private int votos;

    public Cancion (){
        //Constructor por defecto
    }

    public Cancion(int idCancion,String autor, String titulo, String album, String genero, int votos) {
        this.idCancion = idCancion;
        this.autor = autor;
        this.titulo = titulo;
        this.album = album;
        this.genero = genero;
        this.votos = votos;
    }

    public Cancion(int idCancion,String autor, String titulo, String album, String genero, String pedida, int idListaCancion, int votos) {
        this.idCancion = idCancion;
        this.autor = autor;
        this.titulo = titulo;
        this.album = album;
        this.genero = genero;
        this.pedida = pedida;
        this.idListaCancion = idListaCancion;
        this.votos = votos;
    }

    public int getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(int idCancion) {
        this.idCancion = idCancion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPedida() {
        return pedida;
    }

    public void setPedida(String pedida) {
        this.pedida = pedida;
    }

    public int getIdListaCancion() {
        return idListaCancion;
    }

    public void setIdListaCancion(int idListaCancion) {
        this.idListaCancion = idListaCancion;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
}
