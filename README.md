# Trabalho de LFA:

### Autores:
* Lucas Erlacher Rodrigues  
* Tarcísio Bruni Rangel

### Descrição do Código Fonte
Para a realização deste trabalho foi utilizado a linguagem **Java**
com a finalidade de realizar a conversão da entrada de uma **S-Expression**
contendo a definição de uma máquina de **Mealy** ou **Moore**.  
Foi utilizado as seguintes classes: *App, ConversorMaquina (Interface), ConversorMaquinaFactory, MaquinaMealy, MaquinaMoore, OutMoore, TransMoore, TransMealy*, a seguir é mostrado a finalidade de cada classe.
1. **App:** Classe contendo a função principal que recebe os argumentos de entrada e saída fornecidas via linha de comando, cria um ConversorMaquina e chama a função para converter a máquina.
2. **ConversorMaquina:** Interface contendo a função _converteMaquina()_ que é implementada em MaquinaMealy e MaquinaMoore.
3. **ConversorMaquinaFactory:** Responsável por identificar o _pathname_, ler o arquivo de entrada e devolver uma máquina correspondente a **S-Expression**.
