package com.letscode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static final int QTD_COLUNAS = 9;
    public static final int QTD_LINHAS = 4;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Object[][] tabelaProdutos = new Object[QTD_LINHAS][QTD_COLUNAS];

        //fake mock data
        tabelaProdutos[0] = new Object[]{Tipo.ALIMENTOS,
                "Nestle",
                "abc123",
                "Nescau",
                4.00,
                50,
                LocalDateTime.now(),
                Tipo.ALIMENTOS.calcularPreco(4.00), 50};
        tabelaProdutos[1] = new Object[]{Tipo.BEBIDA,
                "La Madre",
                "wer123",
                "don perrengue",
                10,
                20,
                LocalDateTime.now(),
                Tipo.BEBIDA.calcularPreco(4.00), 20};
        tabelaProdutos[2] = new Object[]{Tipo.HIGIENE,
                "Neve",
                "asd234",
                "Soft Butt",
                10,
                200,
                LocalDateTime.now(),
                Tipo.HIGIENE.calcularPreco(4.00), 200};

        while(true) {

            int opcao = menu(sc);

            switch (opcao) {
                case 1:
                    tabelaProdutos = cadastrarComprar(receberInput(sc), tabelaProdutos);
                    break;
                case 2:
                    imprimirEstoque(tabelaProdutos,null);
                    break;
                case 3:
                    System.out.println("Digite o tipo: (ALIMENTOS - BEBIDA - HIGIENE)");
                    Tipo tipo = recebeTipo(sc);
                    imprimirEstoque(tabelaProdutos,tipo);
                    break;
            }
        }
    }

    private static Object[][] cadastrarComprar(Object[] inputs, Object[][] tabelaProdutos) {
        int linhaTabela = produtoEstaNaTabela(inputs, tabelaProdutos);
        if( linhaTabela < 0){
            cadastrarNovoProduto(inputs, tabelaProdutos);
            if( tabelaProdutos[tabelaProdutos.length - 1][0] != null){
                tabelaProdutos = aumentarMatriz(tabelaProdutos);
                System.out.printf("A tabela foi redimensionada, agora ela possui a capacidade de" +
                        " %d linhas.%n", tabelaProdutos.length);
            }
            return tabelaProdutos;
        }
        comprar(inputs, tabelaProdutos, linhaTabela);
        return tabelaProdutos;
    }

    private static void comprar(Object[] inputs, Object[][] tabelaProdutos, int linhaTabela) {
        //recebe o novo preço de custo
        tabelaProdutos[linhaTabela][4] = inputs[4];
        //recebe quantidade da ulima compra
        tabelaProdutos[linhaTabela][5] = inputs[5];
        //recebe a nova data
        tabelaProdutos[linhaTabela][6] = LocalDateTime.now();
        //atualiza preço de venda
        double precoCusto = (double)inputs[4];
        Tipo tipo = (Tipo) inputs[0];
        tabelaProdutos[linhaTabela][7] = (double)tipo.calcularPreco(precoCusto);
        //atualiza estoque
        int novoEstoque = (int)tabelaProdutos[linhaTabela][8] + (int)inputs[5];
        tabelaProdutos[linhaTabela][8] = novoEstoque;
        System.out.println("O produto foi atualizado.");
    }

    private static void cadastrarNovoProduto(Object[] inputs, Object[][] tabelaProdutos) {
        int linhaVazia = 0;
        for (int i = 0; i < tabelaProdutos.length; i++) {
            if(tabelaProdutos[i][0] == null) {
                linhaVazia = i;
                break;
            }
            linhaVazia++;
        }

        for (int i = 0; i < inputs.length; i++) {
            tabelaProdutos[linhaVazia][i] = inputs[i];
        }

        Tipo tipo = (Tipo)inputs[0];
        Double PrecoCusto = (double) inputs[4];
        tabelaProdutos[linhaVazia][7] = tipo.calcularPreco(PrecoCusto);
        tabelaProdutos[linhaVazia][8] = inputs[5];

        System.out.println("O produto foi cadastrado.");

    }

    /**
     * Caso o produto seja encontrado na tabela, retorna o número da linha qe ele se encontra.
     * Retorna -1 caso o produto não esteja na tabela.
     */
    private static int produtoEstaNaTabela(Object[] inputs, Object[][] tabelaProdutos) {
        String nome = (String) inputs[3];
        String identificador = (String) inputs[2];
        String marca = (String) inputs[1];
        String nomeTabela  = "", identificadorTabela="", marcaTabela = "";

        for (int i = 0; i < tabelaProdutos.length; i++) {
            nomeTabela = (String) tabelaProdutos[i][3];
            identificadorTabela = (String) tabelaProdutos[i][2];
            marcaTabela = (String) tabelaProdutos[i][1];
            if(tabelaProdutos[i][3] != null
                    && nomeTabela.equals(nome)
                    && identificadorTabela.equals(identificador)
                    && marcaTabela.equals(marca)){
                return i;
            }
        }
        return -1;
    }

    private static Object[] receberInput(Scanner sc) {
        Object[] produtos = new Object[7];
        System.out.println("Tipo do produto: (ALIMENTOS - BEBIDA - HIGIENE)");
        // recebe Tipo
        produtos[0] = recebeTipo(sc);

        System.out.println("Marca:");
        produtos[1] = sc.nextLine();

        System.out.println("Identificador:");
        produtos[2] = sc.nextLine().replaceAll("\\s+", "");

        System.out.println("Nome do produto:");
        produtos[3] = sc.nextLine();


        System.out.println("Preço de custo: ");
        String precoString;
        double precoCusto = 0.0;
        do {
            precoString = sc.nextLine().replace(",", ".");
            try {
                if(Double.parseDouble(precoString) > 0) {
                    precoCusto = Double.parseDouble(precoString);
                }else{
                    System.out.println("Digite um valor positivo");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor decimal valido:");
            }

        }while(precoCusto <= 0);

        produtos[4] = precoCusto;

        System.out.println("Quantidade: ");
        String qtdString;
        int qtd = 0;
        do{
            qtdString = sc.nextLine().replaceAll("\\s+", "");
            try{
                if(Integer.parseInt(qtdString) > 0) {
                    qtd = Integer.parseInt(qtdString);
                }else{
                    System.out.println("Digite um valor positivo");
                }
            }catch (NumberFormatException e) {
                System.out.println("Digíte um número inteiro válido");
            }

        }while(qtd <= 0);

        produtos[5] = qtd;

        produtos[6] = LocalDateTime.now();

        return produtos;
    }

    public static Tipo recebeTipo(Scanner sc){
        String tipo;
        do{
            tipo = sc.nextLine().toUpperCase().replaceAll("\\s+","");
            if(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE")){
                System.out.println("Digite um dos tipos válidos -> ALIMENTOS - BEBIDA - HIGIENE");
            }
        }while(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE"));
        return Tipo.valueOf(tipo);

    }

    private static void imprimirEstoque(Object[][] tabelaProdutos, Tipo tipo) {
       //cabeçalho
        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");
        String[] tableLabels = {"TIPO","MARCA","IDENTIFICADOR","NOME","PREÇO DE CUSTO","QUANTIDADE DA ULTIMA COMPRA","DATA DA COMPRA",
                                "PREÇO DE VENDA", "ESTOQUE"};
        for (int i = 0; i < tableLabels.length; i++) {
            System.out.printf("| %-27s", tableLabels[i]);
        }
        System.out.println("|");

        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

        //produtos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if(tipo != null){
            listarProdutosTipo(tabelaProdutos, tipo);
        }else {
            first:
            for (int i = 0; i < tabelaProdutos.length; i++) {

                for (int j = 0; j < tabelaProdutos[0].length; j++) {
                    if(j == 6){
                        LocalDateTime date = (LocalDateTime) tabelaProdutos[i][j];
                        System.out.printf("| %-27s", formatter.format(date));
                    }else if(j == 7){
                        System.out.printf("| %-27.2f", tabelaProdutos[i][j]);
                    }else if (tabelaProdutos[i][j] != null) {
                        System.out.printf("| %-27s", tabelaProdutos[i][j].toString());
                    } else{
                        break first;
                    }
                }
                System.out.println("|");
            }
        }
        //linha final
        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

    }

    private static void listarProdutosTipo(Object[][] tabelaProdutos, Tipo tipo) {
        first:
        for (int i = 0; i < tabelaProdutos.length; i++) {
            Tipo tipoAtual = (Tipo) tabelaProdutos[i][0];
            if (tipo == tipoAtual){
                for (int j = 0; j < tabelaProdutos[0].length; j++) {
                    if (tabelaProdutos[i][j] != null) System.out.printf("| %-27s", tabelaProdutos[i][j].toString());
                    else break first;
                }
                System.out.println("|");
            }
        }
    }

    public static int menu(Scanner sc){
        System.out.println("Digite a opção desejada: ");
        System.out.println("1 - Cadastrar/Comprar produtos");
        System.out.println("2 - Imprimir estoque");
        System.out.println("3 - Listar os produto pelo Tipo");
        int opcao = 0;
        do {
            try {
                opcao = Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException exception){
                System.out.println("Opção inválida. Digite um número entre 1 e 3.");
            }
            if(opcao < 1 || opcao > 3){
                System.out.println("Digite 1, 2 ou 3.");
            }
        }while(opcao < 1 || opcao > 3);

        return opcao;
    }

    public static Object[][] aumentarMatriz(Object[][] matriz){
        Object[][] novaMatriz = new Object [matriz.length * 2][QTD_COLUNAS];

        for (int i = 0; i < matriz.length; i++) {
            novaMatriz[i] = matriz[i];
        }

        return novaMatriz;
    }



}


enum Tipo {
    ALIMENTOS(1.2),
    BEBIDA(2.3),
    HIGIENE(1.5);

    private double markup;

    Tipo(double markup) {
        this.markup = markup;
    }

    //TODO: implementdar cálculo de preço usando markup
    public double calcularPreco(double precoCusto){
        return this.markup * precoCusto;
    }
}
