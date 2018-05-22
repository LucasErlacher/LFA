package br.com.conversorMaquinas;

public class OutMoore {
    private String estado;
    private String saida;

    public OutMoore(String estado, String saida){
        this.estado = estado;
        this.saida = saida;
    }

    public String getEstado() {
        return estado;
    }

    public String getSaida() {
        return saida;
    }
}
