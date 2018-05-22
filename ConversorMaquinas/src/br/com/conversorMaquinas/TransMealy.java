package br.com.conversorMaquinas;

import java.util.ArrayList;
import java.util.List;

public class TransMealy {

    private String qi;
    private String qf;
    private String symbol_in;
    private String symbol_out;


    public TransMealy(String qi, String qf, String symbol_in, String symbol_out){
        this.qi = qi;
        this.qf = qf;
        this.symbol_in = symbol_in;
        this.symbol_out = symbol_out;
    }

    public String getQi() {
        return qi;
    }

    public String getQf() {
        return qf;
    }

    public String getSymbol_in() {
        return symbol_in;
    }

    public String getSymbol_out() {
        return symbol_out;
    }
}
