package com.example.popasdelivery;

public class MyOrder {

    int id;
    private String nume,pret,imagine;

    public MyOrder(int id, String nume, String pret, String imagine) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
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

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }
}
