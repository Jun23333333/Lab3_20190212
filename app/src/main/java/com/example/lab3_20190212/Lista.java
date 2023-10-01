package com.example.lab3_20190212;

public class Lista {

    public String nombre;
    public String genero;
    public String ciudad;
    public String pais;
    public String correo;
    public String phone;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String foto;

    public Lista(String nombre, String genero, String ciudad, String pais, String correo, String phone,String foto) {
        this.nombre = nombre;
        this.genero = genero;
        this.ciudad = ciudad;
        this.pais = pais;
        this.correo = correo;
        this.phone = phone;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
