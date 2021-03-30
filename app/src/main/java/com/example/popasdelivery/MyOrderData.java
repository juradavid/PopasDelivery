package com.example.popasdelivery;

public class MyOrderData {
    int id, idClient,pretTotal,pretProduse,pretLivrare;
    String dataCreare, oraCreare, dataLivrare, oraLivrare;

    public MyOrderData(int id, int idClient, int pretTotal, int pretProduse, int pretLivrare, String dataCreare, String oraCreare, String dataLivrare, String oraLivrare) {
        this.id = id;
        this.idClient = idClient;
        this.pretTotal = pretTotal;
        this.pretLivrare = pretLivrare;
        this.pretProduse = pretProduse;
        this.dataCreare = dataCreare;
        this.dataLivrare = dataLivrare;
        this.oraCreare = oraCreare;
        this.oraLivrare = oraLivrare;
    }

    public int getPretProduse() {
        return pretProduse;
    }

    public void setPretProduse(int pretProduse) {
        this.pretProduse = pretProduse;
    }

    public String getDataCreare() {
        return dataCreare;
    }

    public void setDataCreare(String dataCreare) {
        this.dataCreare = dataCreare;
    }

    public String getOraCreare() {
        return oraCreare;
    }

    public void setOraCreare(String oraCreare) {
        this.oraCreare = oraCreare;
    }

    public String getDataLivrare() {
        return dataLivrare;
    }

    public void setDataLivrare(String dataLivrare) {
        this.dataLivrare = dataLivrare;
    }

    public String getOraLivrare() {
        return oraLivrare;
    }

    public void setOraLivrare(String oraLivrare) {
        this.oraLivrare = oraLivrare;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getPretTotal() {
        return pretTotal;
    }

    public void setPretTotal(int pretTotal) {
        this.pretTotal = pretTotal;
    }

    public int getPretLivrare() {
        return pretLivrare;
    }

    public void setPretLivrare(int pretLivrare) {
        this.pretLivrare = pretLivrare;
    }
}
