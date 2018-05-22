package br.com.conversorMaquinas;

public class App {
    public static void main(String[] args){
        String arq_in = ""; //Nome do Arquivo de Entrada
        String arq_out = ""; //Nome do Arquivo de Saída

        if(args[0].equals("-i") && args[2].equals("-o")){
            arq_in = args[1];
            arq_out = args[3];
        }

        if(args[0].equals("-o") && args[2].equals("-i")){
            arq_out = args[1];
            arq_in = args[3];
        }

        //Retorna um conversor de maquinas
        ConversorMaquinaFactory cm_factory = new ConversorMaquinaFactory();

        ConversorMaquina cm = cm_factory.geraConversorMaquina(arq_in);

        try {
            cm.converteMaquina(arq_out); //Realiza a conversão
        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Arquivo Inválido - Ponteiro Null Retornado");
        }
    }
}
