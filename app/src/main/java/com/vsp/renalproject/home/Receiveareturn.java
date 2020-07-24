package com.vsp.renalproject.home;

public class Receiveareturn {

    String amount, name;
    String descrip;
    String time;


    public Receiveareturn(String name,String amount, String descrip, String time) {
        this.amount = amount;
        this.name = name;
        this.descrip = descrip;
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getTime() {
        return time;
    }
}


