package com.letscode;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Object[][] tabelaProdutos = new Object[10][9];


        while(true) {

            int opcao = menu(sc);

            switch (opcao) {
                case 1:
                    cadastrarComprar(receberInput(sc));
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

    private static void cadastrarComprar(Object[] inputs) {
        System.out.println("TODO: implementar método cadastrarComprar");
        for(Object produto : inputs){
            System.out.println(produto);
        }



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
        System.out.println("TODO: implementar método listarProdutos");

    }

    private static void imprimirEstoque() {
        System.out.println("TODO: implementar método imprimirEstoque");
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
}
