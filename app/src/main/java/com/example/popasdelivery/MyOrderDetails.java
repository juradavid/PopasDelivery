package com.example.popasdelivery;

import java.util.List;

public class MyOrderDetails {
    int comandaId;
    List<Integer> produsId;
    List<Integer> cantitate;

    public MyOrderDetails(int comandaId, List<Integer> produsId, List<Integer> cantitate) {
        this.comandaId = comandaId;
        this.produsId = produsId;
        this.cantitate = cantitate;
    }

    public int getComandaId() {
        return comandaId;
    }

    public void setComandaId(int comandaId) {
        this.comandaId = comandaId;
    }

    public List<Integer> getProdusId() {
        return produsId;
    }

    public void setProdusId(List<Integer> produsId) {
        this.produsId = produsId;
    }

    public List<Integer> getCantitate() {
        return cantitate;
    }

    public void setCantitate(List<Integer> cantitate) {
        this.cantitate = cantitate;
    }
}
