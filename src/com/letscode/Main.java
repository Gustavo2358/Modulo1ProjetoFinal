package com.letscode;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //TODO:Tamanho da matriz está fixa. Implementar o método para aumentar linhas.
        Object[][] tabelaProdutos = new Object[10][9];


        while(true) {

            int opcao = menu(sc);

            switch (opcao) {
                case 1:
                    cadastrarComprar(receberInput(sc), tabelaProdutos);
                    break;
                case 2:
                    imprimirEstoque();
                    break;
                case 3:
                    listarProdutos();
                    break;
            }
        }
    }

    private static void cadastrarComprar(Object[] inputs, Object[][] tabelaProdutos) {
        //TODO: implementar

        //metodo para checar se o produto está na tabela, caso sim, efetuar operação de compra
        // caso nao, cadastrar novo produto;
        //produtoEstaNaTabela();


    }

    private static boolean produtoEstaNaTabela(Object[] inputs, Object[][] tabelaProdutos) {
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
                return true;
            }
        }
        return false;
    }

    private static Object[] receberInput(Scanner sc) {
        Object[] produtos = new Object[7];
        System.out.println("Tipo do produto: (ALIMENTOS - BEBIDA - HIGIENE)");
        // recebe Tipo
        String tipo;
        do{
            tipo = sc.nextLine().toUpperCase().replaceAll("\\s+","");
            if(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE")){
                System.out.println("Digite um dos tipos válidos -> ALIMENTOS - BEBIDA - HIGIENE");
            }
        }while(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE"));

        produtos[0] = Tipo.valueOf(tipo);

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

    private static void listarProdutos() {
        //TODO: Implementar

    }

    private static void imprimirEstoque() {
        //TODO: Implementar
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
}
