package br.com.conversorMaquinas;

import java.io.*;

public class ConversorMaquinaFactory {

    public ConversorMaquina geraConversorMaquina(String arq_in) {
        //Máquina em Texto
        String maq_texto = "";

        //Diretório do Arquivo
        String pathname = new File("").getAbsolutePath();

        //Faz leitura do arquivo e coloca em uma string
        try (BufferedReader br = new BufferedReader(new FileReader(pathname + "/" + arq_in))) {
            String linha = br.readLine();
            maq_texto = "" + linha;

            linha = br.readLine();
            while(linha != null){
                maq_texto += " " + linha;
                linha = br.readLine();
            }
        }catch (java.io.FileNotFoundException e){
            e.printStackTrace();
            System.out.println("########################### Arquivo não encontrado ###########################");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("########################### Erro na leitura do arquivo ###########################");
        } finally {
            System.out.println("--> Leitura Encerrada <--");
        }

        //Decide qual será o conversor
        String[] config_maq = maq_texto.split(" ");

        if(config_maq[1].equals("mealy")){
            return new MaquinaMealy(config_maq);
        } else if(config_maq[1].equals("moore")){
            return new MaquinaMoore(config_maq);
        } else{
            System.out.println("########################### Erro na configuração do arquivo ###########################");
        }

        return null;
    }
}
