# Trabalho de LFA:

### Autores:
* Lucas Erlacher Rodrigues  
* Tarcísio Bruni Rangel

### Descrição do Código Fonte:
Para a realização deste trabalho foi utilizado a linguagem **Java**
com a finalidade de realizar a conversão da entrada de uma **S-Expression**
contendo a definição de uma máquina de **Mealy** ou **Moore**.  
Foi utilizado as seguintes classes: *App, ConversorMaquina (Interface), ConversorMaquinaFactory, MaquinaMealy, MaquinaMoore, OutMoore, TransMoore, TransMealy*, a seguir é mostrado a finalidade de cada classe.
1. **App:** Classe contendo a função principal que recebe os argumentos de entrada e saída fornecidas via linha de comando, cria um ConversorMaquina e chama a função para converter a máquina.
2. **ConversorMaquina:** Interface contendo a função _converteMaquina()_ que é implementada em MaquinaMealy e MaquinaMoore.
3. **ConversorMaquinaFactory:** Responsável por identificar o _pathname_, ler o arquivo de entrada e devolver uma máquina correspondente a **S-Expression**.
4. **MaquinaMealy:** Classe que guarda toda a estrutura de dados correspondente a Maquina de Mealy.
5. **MaquinaMoore:** Classe que guarda toda a estrutura de dados correspondente a Maquina de Moore.
6. **OutMoore:** Guarda um estado e sua saída.
7. **TransMoore:** Armazena a transição de Moore.
8. **TransMealy:** Armazena a transição de Mealy.

### Compilação:
_Não é necessário compilar o programa._

### Nome e Modo de Uso:
O nome do programa é **ConversorMaquinas**, para que seja executado via linha de comando, basta entrar no diretório em que os arquivos _conversorMaquinas_ e _ConversorMaquinas.jar_ se localizam via terminal e digitar o seguinte comando:

> ./conversorMaquinas -i <_ArquivoEntrada_> -o <_ArquivoSaída_>

O algorítmo irá procurar o arquivo de entrada e criar um novo arquivo de saída, caso este não seja encontrado um erro é retornado e o programa é encerrado.

### Observação:
Os arquivos de entradas devem estar no mesmo diretório do arquivo _ConversorMaquinas.jar_.
