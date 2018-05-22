package br.com.conversorMaquinas;

import java.util.List;

public class TransMoore {
    private String qi, qf, symbol_in, saida_qf;

    public TransMoore(String qi, String qf, String symbol_in){
        this.qi = qi;
        this.qf = qf;
        this.symbol_in = symbol_in;
    }

    public void setSaida_qf(List<OutMoore> lst_out){
        for (OutMoore out : lst_out){
            if (out.getEstado().equals(qf)) {
                this.saida_qf = out.getSaida();
                break;
            }
        }
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

    public String getSaida_qf(){
        return saida_qf;
    }
}
