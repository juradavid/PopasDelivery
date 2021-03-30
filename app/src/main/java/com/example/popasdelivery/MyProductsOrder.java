package com.example.popasdelivery;

public class    MyProductsOrder {
    int id, comandaId, produsId, cantitate;
    String dataCreata, oraCreata;

    public MyProductsOrder(int id, int comandaId, int produsId, int cantitate, String dataCreata, String oraCreata) {
        this.id = id;
        this.comandaId = comandaId;
        this.produsId = produsId;
        this.cantitate = cantitate;
        this.dataCreata = dataCreata;
        this.oraCreata = oraCreata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComandaId() {
        return comandaId;
    }

    public void setComandaId(int comandaId) {
        this.comandaId = comandaId;
    }

    public int getProdusId() {
        return produsId;
    }

    public void setProdusId(int produsId) {
        this.produsId = produsId;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public String getDataCreata() {
        return dataCreata;
    }

    public void setDataCreata(String dataCreata) {
        this.dataCreata = dataCreata;
    }

    public String getOraCreata() {
        return oraCreata;
    }

    public void setOraCreata(String oraCreata) {
        this.oraCreata = oraCreata;
    }
}
