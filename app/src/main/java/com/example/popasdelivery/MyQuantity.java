package com.example.popasdelivery;

public class MyQuantity {
    int id, cantitate;

    public MyQuantity(int id, int cantitate) {
        this.id = id;
        this.cantitate = cantitate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }
}
