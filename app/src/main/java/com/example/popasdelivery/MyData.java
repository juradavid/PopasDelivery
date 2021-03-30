package com.example.popasdelivery;

import android.os.AsyncTask;

public class MyData {

    private int id;
    private String nume, pret, descriere, imagine;

    public MyData(int id, String nume, String pret, String imagine, String descriere) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.descriere = descriere;
        this.imagine = imagine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }

    public String searchNume(int id) {
        if (this.id == id) {
            return nume;
        }else{
            return null;
        }
    }
}
