package br.com.conversorMaquinas;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MaquinaMoore implements ConversorMaquina {

    //############ Controle de Leitura da Maq #########

    //Lista com S-Expressions
    private  String[] sexpressions = new String[7];

    //Pilha de Parentesis
    private Stack<String> parentStack = new Stack<String>();

    //############ Propriedades da Máquina ############

    //Estado Inicial
    private String start;

    //Lista de: symbols_in, symbols_out, states, finals
    private List<String> symbols_in = new ArrayList<String>();
    private List<String> symbols_out = new ArrayList<String>();
    private List<String> states = new ArrayList<String>();
    private List<String> finals = new ArrayList<String>();

    //Lista de: Transições
    private List<TransMoore> lst_transicoes = new ArrayList<TransMoore>();
    private List<OutMoore> lst_out = new ArrayList<OutMoore>();

    //##################################################

    //Construtor
    public MaquinaMoore(){

    }

    public MaquinaMoore(String[] config_maq){

        //Definindo sexpressions
        sexpressions[0] = "symbols-in";
        sexpressions[1] = "symbols-out";
        sexpressions[2] = "states";
        sexpressions[3] = "finals";
        sexpressions[4] = "trans";
        sexpressions[5] = "out-fn";
        sexpressions[6] = "start";

        int posicaoInicial = 0, posicaoFinal = 0;

        //All text of the machine
        for(int i = 0; i < config_maq.length; i++){

            //Define Início de uma S-Expression
            if(config_maq[i].equals("(") && isSexpression(config_maq[i+1])){
                posicaoInicial = i;
            }

            //Define Final de uma S-Expression
            if (config_maq[i].equals("(")) parentStack.push("(");
            else if(config_maq[i].equals(")")){
                parentStack.pop();
                if(parentStack.size() == 1){
                    posicaoFinal = i;
                    registraSexpressions(config_maq, posicaoInicial, posicaoFinal); //Insere Sexpression nos atributos
                }
            }

        }

        for(TransMoore transicao : lst_transicoes){
            transicao.setSaida_qf(lst_out);
        }

        for(OutMoore out : lst_out){
            if(out.getEstado().equals(this.start) && !out.getSaida().equals("( )")){
                throw new RuntimeException();
            }
        }

    }

    private boolean registraSexpressions(String[] config_maq, int posicaoInicial, int posicaoFinal){
        //Define onde é para guardar informações:
        String tipo = config_maq[posicaoInicial+1];

        //Faz um procedimento para cada tipo:
        switch (tipo){
            case "start":
                this.start = config_maq[posicaoFinal-1];
                break;

            case "symbols-in":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    this.symbols_in.add(config_maq[cont]);
                }
                break;

            case "symbols-out":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    this.symbols_out.add(config_maq[cont]);
                }
                break;

            case "states":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    this.states.add(config_maq[cont]);
                }
                break;

            case "finals":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    this.finals.add(config_maq[cont]);
                }
                break;

            case "trans":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    if (config_maq[cont].equals(")")){

                        this.lst_transicoes.add(new TransMoore(config_maq[cont-3],
                                                          config_maq[cont-2],
                                                          config_maq[cont-1]));
                    }
                }
                break;

            case "out-fn":
                for (int cont = posicaoInicial + 2; cont < posicaoFinal; cont++){
                    if (config_maq[cont].equals(")") && config_maq[cont-1].equals("(")){

                        lst_out.add(new OutMoore(config_maq[cont-2],
                                                 "( )"));

                    }
                    else if (config_maq[cont].equals(")") && !config_maq[cont-1].equals(")")){

                        lst_out.add(new OutMoore(config_maq[cont-2],
                                                 config_maq[cont-1]));

                    }
                }
                break;

            default:
                System.out.println("Um erro ocorreu durante armazenamento das informações");
                return false;
        }

        return true;
    }

    private boolean isSexpression(String palavra){
        for(String expression : sexpressions){
            if(expression.equals(palavra)) return true;
        }
        return false;
    }

    public boolean converteMaquina(String arq_out){
        MaquinaMealy m_mealy = new MaquinaMealy();

        m_mealy.setStates(this.states.toArray(new String[0])); //Define estados da Máquina
        m_mealy.setFinals(this.finals.toArray(new String[0])); //Define estados finais da Máquina
        m_mealy.setSymbols_in(this.symbols_in.toArray(new String[0])); //Define conjunto de símbolos iniciais
        m_mealy.setSymbols_out(this.symbols_out.toArray(new String[0])); //Define conjunto de símbolos terminais
        m_mealy.setStart(this.start); //Define o estado inicial da Máquina

        //Lista temporária
        List<TransMealy> lst_transMealy = new ArrayList();
        for(TransMoore transicao : this.lst_transicoes){
            TransMealy t_mealy = new TransMealy(transicao.getQi(),
                                              transicao.getQf(),
                                              transicao.getSymbol_in(),
                                              transicao.getSaida_qf());
            lst_transMealy.add(t_mealy);
        }

        m_mealy.setLst_transicoes(lst_transMealy.toArray(new TransMealy[0]));

        try {
            createFile(m_mealy, arq_out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void createFile(MaquinaMealy m_mealy, String arq_out) throws IOException {
        File arq = new File(arq_out);

        arq.createNewFile();
        FileWriter fwa = new FileWriter(arq);
        BufferedWriter buffW = new BufferedWriter(fwa);

        buffW.write(m_mealy.toString());

        buffW.close();
    }

    //Usado para montar a máquina

    public void setStart(String qi){
        this.start = qi;
    }

    public void setFinals(String[] qf){
        for(String states : qf){
            this.finals.add(states);
        }
    }

    public void setStates(String[] states){
        for(String state : states){
            this.states.add(state);
        }
    }

    public void setSymbols_out(String[] symbols_out){
        for(String symbol : symbols_out){
            this.symbols_out.add(symbol);
        }
    }

    public void setSymbols_in(String[] symbols_in){
        for(String symbol : symbols_in){
            this.symbols_in.add(symbol);
        }
    }

    public void setLst_transicoes(TransMoore[] transicoes){
        for(TransMoore transicao : transicoes){
            this.lst_transicoes.add(transicao);
        }
    }

    public void setLst_out(OutMoore[] outs){
        for(OutMoore out : outs){
            this.lst_out.add(out);
        }
    }

    @Override
    public String toString(){
        String objeto;

        objeto = "( moore\n";

        objeto += "( symbols-in ";
        for (String s: symbols_in
                ) {
            objeto += s + " ";
        }
        objeto += ")\n";

        objeto += "( symbols-out ";
        for (String s:symbols_out
                ) {
            objeto += s + " ";
        }
        objeto += ")\n";

        objeto += "( states ";
        for (String s:states
                ) {
            objeto += s + " ";
        }
        objeto += ")\n";

        objeto += "( start " + this.start + " )\n";

        objeto += "( finals ";
        for (String s:finals
                ) {
            objeto += s + " ";
        }
        objeto += ")\n";

        objeto += "( trans ";
        for (TransMoore s:lst_transicoes
                ) {
            objeto += "( " + s.getQi() + " " +
                    s.getQf() + " " +
                    s.getSymbol_in() + " " +
                    " ) ";

        }
        objeto += ")\n";

        objeto += "( out-fn\n";
        for (OutMoore s:lst_out
                ) {
            objeto += "( " + s.getEstado() + " " +
                    s.getSaida() +
                    " ) ";

        }

        objeto += ") ";
        objeto += ")";

        return objeto;
    }

    public String newState(String state, int apostrophe){
        String new_state=state;
        for(int i =0; i<apostrophe;i++)
            new_state+="'";
        return new_state;
    }

    public List<String> getAllStatesAfterProcess(Map<String,List<String>> mapa){
        List<String> allStates = new ArrayList<>();
        for (String chave : mapa.keySet()
             ) {
            if(mapa.get(chave).size()!=0)allStates.addAll(mapa.get(chave));
            else allStates.add(chave);
        }
        return allStates;
    }

    public List<String> getAllStatesAfterProcess(Map<String,List<String>> mapa, List<String> states){
        List<String> allStates = new ArrayList<>();
        for (String finalstate : states
                ) {
            allStates.addAll(mapa.get(finalstate));

        }
        return allStates;
    }

    public String getAllStartAfterProcess(Map<String,List<String>> mapa, String finalstate){
        return (mapa.get(finalstate).size()==0) ? finalstate : mapa.get(finalstate).get(0) ;
    }


}
