package com.example.raul.navegadorweb;

/**
 * Created by Raul on 28/10/2016.
 */

public class Historial {

    private String texto;
    private String url;

    public Historial(String texto){
        this.texto = texto;
    }

    public Historial(String texto, String url){
        this.texto = texto;
        this.url = url;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String text) {
        this.texto = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
