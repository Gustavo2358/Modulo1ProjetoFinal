package com.letscode.project;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static final int QTD_COLUNAS = 9;
    public static final int QTD_LINHAS = 4;
    public static Object[][] tabelaProdutos = new Object[QTD_LINHAS][QTD_COLUNAS];
    public static final int QTD_COLUNASV = 4;
    public static final int QTD_LINHASV = 5;
    public static Object[][] tabelaVendas = new Object[QTD_LINHASV][QTD_COLUNASV];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //fake mock data
//        tabelaProdutos[0] = new Object[]{TipoProduto.ALIMENTOS,
//                "Nestle",
//                "abc123",
//                "Nescau",
//                4.00,
//                50,
//                LocalDateTime.now(),
//                TipoProduto.ALIMENTOS.calcularPreco(4.00),
//                50};
//        tabelaProdutos[1] = new Object[]{TipoProduto.BEBIDA,
//                "La Madre",
//                "wer123",
//                "don perrengue",
//                10,
//                20,
//                LocalDateTime.now(),
//                TipoProduto.BEBIDA.calcularPreco(10.00),
//                20};
//        tabelaProdutos[2] = new Object[]{TipoProduto.HIGIENE,
//                "Neve",
//                "asd234",
//                "Soft Butt",
//                12,
//                200,
//                LocalDateTime.now(),
//                TipoProduto.HIGIENE.calcularPreco(12.00),
//                200};
//
//        tabelaVendas[0] = new Object[]{"00000000191",
//                TipoCliente.PF,
//                10,
//                45.00};
//
//        tabelaVendas[1] = new Object[]{"00000000191",
//                TipoCliente.PF,
//                15,
//                295.00};
//
//        tabelaVendas[2] = new Object[]{"12345678901",
//                TipoCliente.VIP,
//                30,
//                410.55};
//
//        tabelaVendas[3] = new Object[]{"17171717171",
//                TipoCliente.PJ,
//                50,
//                855.00};

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
                case 7:
                    imprimirVendas();
                    break;
                case 8:
                    relatorioSintetico();
                    break;
                case 9:
                    if(confirmacaoSair(sc)) {
                        return;
                    }
                    break;
            }
        }
    }

    private static boolean confirmacaoSair(Scanner sc) {
        System.out.println("Deseja mesmo sair? \n" +
                        "Caso o programa seja encerrado, todos os dados serão perdidos...\n" +
                        "[S para sair, ou digite qualquer tecla para cancelar.] \n"
                );
        if(sc.nextLine().toUpperCase(Locale.ROOT).equals("S")){
            return true;
        }
        return false;
    }

    private static void relatorioSintetico() {
        //criar um array com todos os cpf (sem repetir)
        String[] arrayCPF = buscarCPF();
        //construir matriz, percorrer o array e trazer a quantidade de produtos e valores para a matriz
        Object[][] relatorioSintetico = construirRelatorioSintetico(arrayCPF);
        //imprimir
        imprimirRelatorioSintetico(relatorioSintetico);
    }

    private static Object[][] construirRelatorioSintetico(String[] arrayCPF) {
        Object[][] relatorioSintetico = new Object[arrayCPF.length][3];
        int qtdProdutos = 0;
        double valorPago = 0;
        for (int i = 0; i < arrayCPF.length; i++) {
            for (int j = 0; j < tabelaVendas.length; j++) {
                if(tabelaVendas[j][0] == null){
                    break;
                }
                if(arrayCPF[i].equals(tabelaVendas[j][0])){
                    qtdProdutos += (int)tabelaVendas[j][2];
                    valorPago += (double)tabelaVendas[j][3];
                }
            }
            relatorioSintetico[i][0] = arrayCPF[i];
            relatorioSintetico[i][1] = qtdProdutos;
            relatorioSintetico[i][2] = valorPago;
            qtdProdutos = 0;
            valorPago = 0;
        }
        return relatorioSintetico;
    }

    private static String[] buscarCPF(){
        String[] arrayCPF = new String[0];
        String cpf;
        for (int i = 0; i < tabelaVendas.length; i++) {
            cpf = (String)tabelaVendas[i][0];
            if(cpf == null) break;
            if(!isInArray(cpf, arrayCPF)){
                arrayCPF = aumentarArray(arrayCPF);
                arrayCPF[arrayCPF.length - 1] = cpf;
            }
        }
        return arrayCPF;
    }

    private static String[] aumentarArray(String[] arrayCPF) {
        String[] novoArrayCpf = new String[arrayCPF.length + 1];
        for (int i = 0; i < arrayCPF.length; i++) {
            novoArrayCpf[i] = arrayCPF[i];
        }
        return novoArrayCpf;

    }

    private static boolean isInArray(String cpf, String[] arrayCPF) {
        for (String c : arrayCPF){
            try {
                if(c.equals(cpf)){
                    return true;
                }
            }catch (NullPointerException e){
                break;
            }
        }
        return false;
    }


    private static void cadastrarComprar(Object[] inputs) {
        int linhaTabela = produtoEstaNaTabela(inputs);
        if( linhaTabela < 0){
            cadastrarNovoProduto(inputs);
            if( tabelaProdutos[tabelaProdutos.length - 1][0] != null){
                tabelaProdutos = aumentarMatrizGenerico(tabelaProdutos);
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

    private static void imprimirVendas() {
        //cabeçalho
        for (int k = 0; k < tabelaVendas[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");
        String[] tableLabels = {"CPF","TIPO DE CLIENTE","QUANTIDADE VENDIDA","PREÇO DA VENDA"};
        for (String tableLabel : tableLabels) {
            System.out.printf("| %-27s", tableLabel);
        }
        System.out.println("|");

        for (int k = 0; k < tabelaVendas[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

        first:
        for (int i = 0; i < tabelaVendas.length; i++) {
            for (int j = 0; j < tabelaVendas[0].length; j++) {
                if(tabelaVendas[i][j] == null) {
                    break first;
                } else if(j == 1){
                    TipoCliente tipo = (TipoCliente) tabelaVendas[i][j];
                    System.out.printf("| %-27s", tipo.getDescrição());

                } else if(j == 3){
                    double valor = (double) tabelaVendas[i][j];
                    System.out.printf("| %-27.2f", valor);
                }else {
                    System.out.printf("| %-27s", tabelaVendas[i][j].toString());
                }
            }
            System.out.println("|");
        }

        //linha final
        for (int k = 0; k < tabelaVendas[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

    }
    private static void imprimirRelatorioSintetico(Object[][] relatorio) {
        //cabeçalho
        System.out.println("###############################");
        System.out.println("#    Relatório Consolidado    #");
        System.out.println("###############################");
        for (int k = 0; k < relatorio[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");
        String[] tableLabels = {"CPF","QUANTIDADE VENDIDA","PREÇO DA VENDA"};
        for (String tableLabel : tableLabels) {
            System.out.printf("| %-27s", tableLabel);
        }
        System.out.println("|");

        for (int k = 0; k < relatorio[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

        first:
        for (int i = 0; i < relatorio.length; i++) {
            for (int j = 0; j < relatorio[0].length; j++) {
                if(relatorio[i][j] == null) {
                    break first;
                } else if(j == 2){
                    double valor = (double) relatorio[i][j];
                    System.out.printf("| %-27.2f", valor);
                }else {
                    System.out.printf("| %-27s", relatorio[i][j].toString());
                }
            }
            System.out.println("|");
        }

        //linha final
        for (int k = 0; k < relatorio[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

    }

    private static void listarProdutosNome(String nome) {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            String nomeTabela = (String) tabelaProdutos[i][3];
            try {
                if (nomeTabela.toLowerCase(Locale.ROOT).contains(nome.toLowerCase(Locale.ROOT))) {
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
        int opcaoMaxima = 9;
        System.out.println("Digite a opção desejada: ");
        System.out.println("1 - Cadastrar/Comprar produtos");
        System.out.println("2 - Imprimir estoque");
        System.out.println("3 - Listar os produto pelo Tipo");
        System.out.println("4 - Pesquisar um produto pelo código");
        System.out.println("5 - Pesquisar um produto pelo nome");
        System.out.println("6 - Efetuar venda");
        System.out.println("7 - Relatório de vendas analítico");
        System.out.println("8 - Relatório de vendas consolidado");
        System.out.println("9 - Sair");
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

    public static Object[][] aumentarMatrizGenerico(Object[][] matriz){
        Object[][] novaMatriz = new Object [matriz.length * 2][matriz[0].length];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                novaMatriz[i][j] = matriz[i][j];
            }
        }

        return novaMatriz;
    }

    private static void venda(Scanner sc) {
        int indiceTabelaVendas = indiceTabelaVendas();
        if(indiceTabelaVendas == -1){
            tabelaVendas = aumentarMatrizGenerico(tabelaVendas);
            indiceTabelaVendas = indiceTabelaVendas();

        }
        Object[] venda = new Object[4];
        String CPF = receberCPF(sc);
        tabelaVendas[indiceTabelaVendas][0] = CPF;
        venda[0] = CPF;

        if(CPF.equals("00000000191")){
            venda[1] = TipoCliente.PF;
        }else {
            venda[1] = receberTipoCliente(sc);
        }
        tabelaVendas[indiceTabelaVendas][1] = venda[1];

        int qtdProdutos = 0;
        Object[][] resumo = efetuarVenda(venda, sc);
        for (int i = 0; i < resumo.length; i++) {
            if(resumo[i][2] == null)
                break;
            qtdProdutos += (int)resumo[i][2];
        }
        tabelaVendas[indiceTabelaVendas][2] = qtdProdutos;

        imprimirResumo(resumo,sc);
        double valorTotal = calcularValorTotal(resumo);
        TipoCliente tipo = (TipoCliente) venda[1];
        double valorTotalDescontado = valorTotal - tipo.valorDescontar(valorTotal);
        System.out.printf("Valor Total: %.2f%n", valorTotalDescontado);
        tabelaVendas[indiceTabelaVendas][3] = valorTotalDescontado;
        String enter;
        do{
            System.out.println("digite ENTER para voltar para o menu principal");
            enter = sc.nextLine();
        }while(!enter.equals(""));


    }

    private static int indiceTabelaVendas() {
        for (int i = 0; i < tabelaVendas.length; i++) {
            if(tabelaVendas[i][0] == null)
                return i;
        }
        return -1;
    }

    private static double calcularValorTotal(Object[][] resumo) {
        double soma = 0;
        for (int i = 0; i < resumo.length; i++) {
            try {
                soma += (double) resumo[i][4];
            }
            catch(NullPointerException e){
                return soma;
            }
        }
        return soma;
    }

    private static void imprimirResumo(Object[][] resumo, Scanner sc) {
        System.out.println("################");
        System.out.println("#    RESUMO    #");
        System.out.println("################");
        //cabeçalho
        for (int k = 0; k < resumo[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");
        String[] tableLabels = {"CODIGO","NOME","QUANTIDADE","PREÇO","VALOR A PAGAR"};
        for (String tableLabel : tableLabels) {
            System.out.printf("| %-27s", tableLabel);
        }
        System.out.println("|");

        for (int k = 0; k < resumo[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

        first:
        for (int i = 0; i < resumo.length; i++) {
            for (int j = 0; j < resumo[0].length; j++) {
                if(resumo[i][j] != null) {
                    System.out.printf("| %-27s", resumo[i][j].toString());
                }else{
                    break first;
                }
            }
            System.out.println("|");
        }

        //linha final
        for (int k = 0; k < resumo[0].length; k++) {
            System.out.print("+----------------------------");
        }
        System.out.println("+");

    }

    private static Object[][] efetuarVenda(Object[] venda, Scanner sc) {
        //Codigo | Nome | Quantidade | Preco | ValorPagar
        Object[][] resumo = new Object[5][5];
        int qtd = 0;
        int contagemProduto = 0;
        while(true) {
            System.out.println("Digite o código do produto, ou FIM para finalizar: ");
            String codigo = sc.nextLine();
            if(codigo.equalsIgnoreCase("FIM")) break;
            Object[] produto = buscarProduto(codigo);
            if(produto == null) {
                System.out.println("Produto inválido.");
                continue;
            }

            int estoque = (int) produto[8];
            if (estoque == 0){
                System.out.println("Este produto está temporariamente esgotado. ");
                continue;
            }
            System.out.println("Digite a quantidade:");
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

            int estoqueAntigo = (int) tabelaProdutos[buscarIndice(codigo)][8];
            tabelaProdutos[buscarIndice(codigo)][8] = estoqueAntigo - qtd;
            Object[] linhaResumo = new Object[5];
            linhaResumo[0] = codigo;
            linhaResumo[1] = produto[3];
            linhaResumo[2] = qtd;
            double preco = (double) produto[7];
            linhaResumo[3] = preco;
            linhaResumo[4] = preco * qtd;
            resumo[contagemProduto] = linhaResumo;
            contagemProduto++;
            if(contagemProduto == resumo.length){
                resumo = aumentarMatrizGenerico(resumo);
            }

            qtd = 0;

        }

        return resumo;
    }

    private static int buscarIndice(String codigo) {
        for (int i = 0; i < tabelaProdutos.length; i++) {
            String codigoTabela = (String) tabelaProdutos[i][2];
            try {
                if (codigoTabela.equals(codigo)) {
                    return i;
                }
            }catch (NullPointerException e){
                return -1;
            }
        }
        return -1;
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
    PF(0, "Pessoa Física"),
    PJ(0.05, "Pessoa Jurídica"),
    VIP(0.15, "VIP");

    private double desconto;
    private String descrição;

    TipoCliente(double desconto, String descrição) {
        this.desconto = desconto;
        this.descrição = descrição;
    }

    public String getDescrição() {
        return descrição;
    }

    public double valorDescontar(double totalCompra) { return this.desconto * totalCompra;}
}