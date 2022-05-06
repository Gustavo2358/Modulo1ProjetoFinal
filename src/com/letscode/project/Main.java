package com.letscode.project;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static final int QTD_COLUNAS = 9;
    public static final int QTD_LINHAS = 4;
    public static Object[][] tabelaProdutos = new Object[QTD_LINHAS][QTD_COLUNAS];
    /*
    0 Tipo
	1 Marca
	2 Identificador
	3 Nome
	4 Preco Custo
	5 Quantidade
	6 Data Compra
	7 Preco
	8 Estoque
     */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //fake mock data
        tabelaProdutos[0] = new Object[]{TipoProduto.ALIMENTOS,
                "Nestle",
                "abc123",
                "Nescau",
                4.00,
                50,
                LocalDateTime.now(),
                TipoProduto.ALIMENTOS.calcularPreco(4.00),
                50};
        tabelaProdutos[1] = new Object[]{TipoProduto.BEBIDA,
                "La Madre",
                "wer123",
                "don perrengue",
                10,
                20,
                LocalDateTime.now(),
                TipoProduto.BEBIDA.calcularPreco(10.00),
                20};
        tabelaProdutos[2] = new Object[]{TipoProduto.HIGIENE,
                "Neve",
                "asd234",
                "Soft Butt",
                12,
                200,
                LocalDateTime.now(),
                TipoProduto.HIGIENE.calcularPreco(12.00),
                200};

        while(true) {

            int opcao = menu(sc);

            switch (opcao) {
                case 1:
                    cadastrarComprar(receberInput(sc));
                    break;
                case 2:
                    imprimirEstoque(null, null, null);
                    break;
                case 3:
                    System.out.println("Digite o tipo: (ALIMENTOS - BEBIDA - HIGIENE)");
                    TipoProduto tipo = recebeTipoProduto(sc);
                    imprimirEstoque(tipo, null, null);
                    break;
                case 4:
                    System.out.println("Digite o código:");
                    String id = sc.nextLine();
                    imprimirEstoque(null, id, null);
                    break;
                case 5:
                    System.out.println("Digite o nome ou parte dele: ");
                    String nome = sc.nextLine();
                    imprimirEstoque(null,null,nome);
                    break;
                case 6:
                    venda(sc);
                    break;
            }
        }
    }


    private static void cadastrarComprar(Object[] inputs) {
        int linhaTabela = produtoEstaNaTabela(inputs);
        if( linhaTabela < 0){
            cadastrarNovoProduto(inputs);
            if( tabelaProdutos[tabelaProdutos.length - 1][0] != null){
                tabelaProdutos = aumentarMatriz(tabelaProdutos);
                System.out.printf("A tabela foi redimensionada, agora ela possui a capacidade de" +
                        " %d linhas.%n", tabelaProdutos.length);
            }
            return;
        }
        comprar(inputs, linhaTabela);

    }

    private static void comprar(Object[] inputs, int linhaTabela) {
        //recebe o novo preço de custo
        tabelaProdutos[linhaTabela][4] = inputs[4];
        //recebe quantidade da ulima compra
        tabelaProdutos[linhaTabela][5] = inputs[5];
        //recebe a nova data
        tabelaProdutos[linhaTabela][6] = LocalDateTime.now();
        //atualiza preço de venda
        double precoCusto = (double)inputs[4];
        TipoProduto tipo = (TipoProduto) inputs[0];
        tabelaProdutos[linhaTabela][7] = tipo.calcularPreco(precoCusto);
        //atualiza estoque
        int novoEstoque = (int)tabelaProdutos[linhaTabela][8] + (int)inputs[5];
        tabelaProdutos[linhaTabela][8] = novoEstoque;
        System.out.println("O produto foi atualizado.");
    }

    private static void cadastrarNovoProduto(Object[] inputs) {
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

        TipoProduto tipo = (TipoProduto)inputs[0];
        Double PrecoCusto = (double) inputs[4];
        tabelaProdutos[linhaVazia][7] = tipo.calcularPreco(PrecoCusto);
        tabelaProdutos[linhaVazia][8] = inputs[5];

        System.out.println("O produto foi cadastrado.");

    }

    /**
     * Caso o produto seja encontrado na tabela, retorna o número da linha qe ele se encontra.
     * Retorna -1 caso o produto não esteja na tabela.
     */
    private static int produtoEstaNaTabela(Object[] inputs) {
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
        produtos[0] = recebeTipoProduto(sc);

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

    public static TipoProduto recebeTipoProduto(Scanner sc){
        String tipo;
        do{
            tipo = sc.nextLine().toUpperCase().replaceAll("\\s+","");
            if(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE")){
                System.out.println("Digite um dos tipos válidos -> ALIMENTOS - BEBIDA - HIGIENE");
            }
        }while(!tipo.equals("ALIMENTOS") && !tipo.equals("BEBIDA") && !tipo.equals("HIGIENE"));
        return TipoProduto.valueOf(tipo);

    }

    private static void imprimirEstoque(TipoProduto tipo, String id, String nome) {
        //cabeçalho
        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");
        String[] tableLabels = {"TIPO","MARCA","IDENTIFICADOR","NOME","PREÇO DE CUSTO","QUANTIDADE DA ULTIMA COMPRA","DATA DA COMPRA",
                "PREÇO DE VENDA", "ESTOQUE"};
        for (String tableLabel : tableLabels) {
            System.out.printf("| %-27s", tableLabel);
        }
        System.out.println("|");

        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");


        //produtos
        if(tipo != null){   //se for passado um tipo, listar pelo tipo
            listarProdutosTipo(tipo);

        }else if(id != null){
          listarProdutosId(id);

        } else if(nome != null){
            listarProdutosNome(nome);
        }else { //listar todos os produtos
            listarTudo();
        }
        //linha final
        for (int k = 0; k < tabelaProdutos[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

    }

    private static void listarProdutosNome(String nome) {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            String nomeTabela = (String) tabelaProdutos[i][3];
            try {
                if (nomeTabela.contains(nome)) {
                    imprimeProdutos(i);
                }
            }catch (NullPointerException e){
                return;
            }
        }
    }

    private static void listarProdutosId(String id) {
        //considera que os identificadores são únicos
        String identificador;
        for (int i = 0; i < tabelaProdutos.length; i++) {
            identificador = (String) tabelaProdutos[i][2];
            try {
                if (identificador.equals(id)) {
                    imprimeProdutos(i);
                    return;
                }
            }catch (NullPointerException e){
                System.out.println("Código inválido.");
            }

        }
    }

    private static void listarTudo() {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            imprimeProdutos(i);
        }
    }

    private static void listarProdutosTipo(TipoProduto tipo) {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            TipoProduto tipoAtual = (TipoProduto) tabelaProdutos[i][0];
            if (tipo == tipoAtual){
                imprimeProdutos(i);
            }
        }
    }

    private static void imprimeProdutos(int i){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int j = 0; j < tabelaProdutos[0].length; j++) {
            if(j == 6){
                LocalDateTime date = (LocalDateTime) tabelaProdutos[i][j];
                System.out.printf("| %-27s", formatter.format(date));
            }else if(j == 7){
                System.out.printf("| %-27.2f", tabelaProdutos[i][j]);
            }else if (tabelaProdutos[i][j] != null) {
                System.out.printf("| %-27s", tabelaProdutos[i][j].toString());
            } else{
                return;
            }
        }
        System.out.println("|");

    }

    public static int menu(Scanner sc){
        int opcaoMaxima = 6;
        System.out.println("Digite a opção desejada: ");
        System.out.println("1 - Cadastrar/Comprar produtos");
        System.out.println("2 - Imprimir estoque");
        System.out.println("3 - Listar os produto pelo Tipo");
        System.out.println("4 - Pesquisar um produto pelo código");
        System.out.println("5 - Pesquisar um produto pelo nome");
        System.out.println("6 - Efetuar venda");
        int opcao = 0;
        do {
            try {
                opcao = Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException exception){
                System.out.printf("Opção inválida. Digite um número entre 1 e %d.%n", opcaoMaxima);
            }
            if(opcao < 1 || opcao > opcaoMaxima){
                System.out.printf("Digite um número entre 1 e %d (incluso).%n", opcaoMaxima);
            }
        }while(opcao < 1 || opcao > opcaoMaxima);

        return opcao;
    }

    public static Object[][] aumentarMatriz(Object[][] matriz){
        Object[][] novaMatriz = new Object [matriz.length * 2][QTD_COLUNAS];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < tabelaProdutos[0].length; j++) {
                novaMatriz[i][j] = matriz[i][j];
            }

        }

        return novaMatriz;
    }

    private static void venda(Scanner sc) {
        Object[] venda = new Object[4];
        String CPF = receberCPF(sc);
        venda[0] = CPF;
        if(CPF.equals("00000000191")){
            venda[1] = TipoCliente.PF;
        }else {
            venda[1] = receberTipoCliente(sc);
        }
        int qtdProdutos = 0;
        Object[][] resumo = efetuarVenda(venda, sc);

        //imprime matriz para DEBUG
        for( Object[] a : resumo){
            System.out.println(Arrays.toString(a));
        }


        //TODO: Imprimir resumo
        //TODO: Mostrar Valor total a pagar
        //TODO: Armazenar na matriz global
        //TODO: Mensagem para voltar ao menu principal


    }

    private static Object[][] efetuarVenda(Object[] venda, Scanner sc) {
        //Codigo | Nome | Quantidade | Preco | ValorPagar
        Object[][] resumo = new Object[10][5];
        int qtd = 0;
        int contagemProduto = 0;
        while(true) {
            System.out.println("Digite o código do produto: ");
            String codigo = sc.nextLine();
            if(codigo.equalsIgnoreCase("FIM")) break;
            Object[] produto = buscarProduto(codigo);
            if(produto == null) {
                System.out.println("Produto inválido.");
                continue;
            }
            System.out.println("Digite a quantidade:");
            int estoque = (int) produto[8];
            while(qtd <= 0) {
                try {
                    qtd = Math.abs(Integer.parseInt(sc.nextLine()));
                }catch (NumberFormatException e){
                    System.out.println("digíte um número inteiro.");
                }

                if(qtd > estoque){
                    System.out.printf("A quantidade desse item disponível no estoque é: %d%n" +
                            "Digíte uma quantidade válida: %n", estoque);
                    qtd = 0;
                }
            }

            //TODO: diminuir quantidade em estoque
            Object[] linhaResumo = new Object[5];
            linhaResumo[0] = codigo;
            linhaResumo[1] = produto[3];
            linhaResumo[2] = qtd;
            double preco = (double)produto[7];
            linhaResumo[3] = preco;
            linhaResumo[4] = preco * qtd;
            resumo[contagemProduto] = linhaResumo;
            //TODO: aumentar matriz quando chegar na capacidade máxima
            contagemProduto++;
            qtd = 0;
        }


        return resumo;
    }

    private static Object[] buscarProduto(String codigo) {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            String codigoTabela = (String) tabelaProdutos[i][2];
            try {
                if (codigoTabela.equals(codigo)) {
                    return tabelaProdutos[i];
                }
            }catch (NullPointerException e){
                return null;
            }
        }
        return null;
    }

    private static TipoCliente receberTipoCliente(Scanner sc) {
        System.out.println("Digíte o Tipo do cliente: \nPF para Pessoa física\n" +
                "PJ para pessoa juridica e \n" +
                "VIP para cliente vip ");
        String tipo;
        do{
            tipo = sc.nextLine().toUpperCase().replaceAll("\\s+","");
            if(!tipo.equals("PF") && !tipo.equals("PJ") && !tipo.equals("VIP")){
                System.out.println("Digite um dos tipos válidos -> PF - PJ - VIP");
            }
        }while(!tipo.equals("PF") && !tipo.equals("PJ") && !tipo.equals("VIP"));
        return TipoCliente.valueOf(tipo);
    }

    private static String receberCPF(Scanner sc) {
        System.out.println("Deseja inserir CPF? [Digite S para sim e N para não.]");
        String escolha = "";
        do {
            escolha = sc.nextLine().toUpperCase();
            if ( !escolha.equals("S") && !escolha.equals("N")){
                System.out.println("Entrada inválida, digite S para sim e N para não.");
            }
        }while( !escolha.equals("S") && !escolha.equals("N"));

        if (escolha.equals("S")){
            String CPF = "";
            System.out.println("Digite o CPF (sem usar \".\" e \"-\":)");
            boolean cpfValido = false;
            do {
                CPF = sc.nextLine();
                try{
                    Long.parseLong(CPF);
                }catch (NumberFormatException e){
                    System.out.println("Digite apenas números.");
                    continue;
                }
                if(CPF.length() == 11){
                    cpfValido = true;
                }else{
                    System.out.println("O CPF deve conter 11 números:");
                }

            }while(!cpfValido);
            return CPF;
        }

        return "00000000191";
    }

}


enum TipoProduto {
    ALIMENTOS(1.2),
    BEBIDA(2.3),
    HIGIENE(1.5);

    private double markup;

    TipoProduto(double markup) {
        this.markup = markup;
    }

    public double calcularPreco(double precoCusto){
        return this.markup * precoCusto;
    }
}

enum TipoCliente {
    PF(0),
    PJ(0.05),
    VIP(0.15);
    private double desconto;

    TipoCliente(double desconto) {this.desconto = desconto;}

    public double valorDescontar(double totalCompra) { return this.desconto * totalCompra;}
}