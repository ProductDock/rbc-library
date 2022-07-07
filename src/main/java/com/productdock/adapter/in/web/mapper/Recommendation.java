package com.productdock.adapter.in.web.mapper;

public enum Recommendation {

    JUNIOR(0),
    MEDIOR(1),
    SENIOR(2);

    private int bit;

    Recommendation(int bit) {
        this.bit = (int) Math.pow(2, bit);
    }

    public int getBit() {
        return this.bit;
    }

}
