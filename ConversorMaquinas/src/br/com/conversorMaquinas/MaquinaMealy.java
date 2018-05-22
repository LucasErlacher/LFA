package br.com.conversorMaquinas;


import java.io.*;
import java.util.*;

public class MaquinaMealy implements ConversorMaquina{
    //############ Controle de Leitura da Maq #########

    //Lista com S-Expressions
    private  String[] sexpressions = new String[6];

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
    private List<TransMealy> lst_transicoes = new ArrayList<TransMealy>();


    public MaquinaMealy(){

    }

    //Construtor
    public MaquinaMealy(String[] config_maq){

        //Definindo sexpressions
        sexpressions[0] = "symbols-in";
        sexpressions[1] = "symbols-out";
        sexpressions[2] = "states";
        sexpressions[3] = "finals";
        sexpressions[4] = "trans";
        sexpressions[5] = "start";

        int posicaoInicial = 0, posicaoFinal = 0;

        //All text of the machine
        for(int i = 0; i < config_maq.length; i++) {

            //Define Início de uma S-Expression
            if (config_maq[i].equals("(") && isSexpression(config_maq[i + 1])) {
                posicaoInicial = i;
            }

            //Define Final de uma S-Expression
            if (config_maq[i].equals("(")) parentStack.push("(");
            else if (config_maq[i].equals(")")) {
                parentStack.pop();
                if (parentStack.size() == 1) {
                    posicaoFinal = i;
                    registraSexpressions(config_maq, posicaoInicial, posicaoFinal); //Insere Sexpression nos atributos
                }
            }
        }


    }

    //Métodos
    public boolean converteMaquina(String arq_out){
        MaquinaMoore m_moore = new MaquinaMoore();

        //Mapa temporario
        Map<String,List<String>> map = this.mappingg(this.states);

        //Lista temporária
        List<TransMoore> lst_transMoore = new ArrayList();
        List<OutMoore> lst_out = new ArrayList<OutMoore>();

        for(TransMealy transicao : this.lst_transicoes){

            //Adicionando novo estado
            String newState = m_moore.newState(transicao.getQf(),
                              map.get(transicao.getQf()).size());
            map.get(transicao.getQf()).add(newState);

            if (map.get(transicao.getQi()).size() == 0){
                TransMoore t_moore = new TransMoore(transicao.getQi(),
                                        newState,
                                        transicao.getSymbol_in());
                lst_transMoore.add(t_moore);
            }else{
                for (String qi : map.get(transicao.getQi())
                        ) {
                    TransMoore t_moore = new TransMoore(qi,
                            newState,
                            transicao.getSymbol_in());
                    lst_transMoore.add(t_moore);
                }
            }
            OutMoore out_moore = new OutMoore(newState,
                    transicao.getSymbol_out());
            lst_out.add(out_moore);
        }

        m_moore.setLst_transicoes(lst_transMoore.toArray(new TransMoore[0]));
        m_moore.setLst_out(lst_out.toArray(new OutMoore[0]));
        m_moore.setStates(m_moore.getAllStatesAfterProcess(map).toArray(new String[0])); //Define estados da Máquina
        m_moore.setFinals(m_moore.getAllStatesAfterProcess(map,this.finals).toArray(new String[0])); //Define estados finais da Máquina
        m_moore.setSymbols_in(this.symbols_in.toArray(new String[0])); //Define conjunto de símbolos iniciais
        m_moore.setSymbols_out(this.symbols_out.toArray(new String[0])); //Define conjunto de símbolos terminais
        m_moore.setStart(m_moore.getAllStartAfterProcess(map,this.start)); //Define o estado inicial da Máquina

        try {
            createFile(m_moore, arq_out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void createFile(MaquinaMoore m_moore, String arq_out) throws IOException {
        File arq = new File(arq_out);

        arq.createNewFile();
        FileWriter fwa = new FileWriter(arq);
        BufferedWriter buffW = new BufferedWriter(fwa);

        buffW.write(m_moore.toString());

        buffW.close();
    }

    private boolean isSexpression(String palavra){
        for(String expression : sexpressions){
            if(expression.equals(palavra)) return true;
        }
        return false;
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

                        this.lst_transicoes.add(new TransMealy(config_maq[cont-4],
                                config_maq[cont-3],
                                config_maq[cont-2],
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

    public void setLst_transicoes(TransMealy[] transicoes){
        for(TransMealy transicao : transicoes){
            this.lst_transicoes.add(transicao);
        }
    }

    @Override
    public String toString(){
        String objeto;

        objeto = "( mealy\n";

        objeto += "( symbols-in ";
        for (String s:symbols_in
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
        for (TransMealy s:lst_transicoes
                ) {
            objeto += "( " + s.getQi() + " " +
                             s.getQf() + " " +
                             s.getSymbol_in() + " " +
                             s.getSymbol_out() +
                      " ) ";

        }

        objeto += ") ";

        objeto += ")";
        return objeto;
    }

    public Map<String, List<String>> mappingg(List<String> states){
        Map<String,List<String>> mapa = new HashMap<>();
        for (String state: states) {
            mapa.put(state,new ArrayList<>());
        }
        return mapa;

    }
}
