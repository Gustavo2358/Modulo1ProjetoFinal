package com.letscode;

public class Estoque {

    private Object[][] estoque = new Object[10][2]; //colunas: 0-produto 1-identificadorFinal

    public boolean checarProduto(String identificadorFinal){
        for(Object[] produtos: this.estoque){
            if(produtos[1].equals(identificadorFinal)) {
                return true;
            }
        }
        return false;
    }

    public Produto getProduto(String identificadorFinal){
        for(Object[] produtos: this.estoque){
            if(produtos[1].equals(identificadorFinal)){
                return (Produto) produtos[0];
            }
        }
        return null;
    }

    public void aumentaEstoque(){
        Object[][] estoqueNovo = new Object[(this.estoque.length + 20)][2];
        for(int i = 0; i < this.estoque.length; i++){
            estoqueNovo[i] = this.estoque[i];
            }
        this.estoque = estoqueNovo;
        }

    }
    //TODO:
    //adicionar o produto
    //adicionar a quantidade

    //printar o esqtoque
    // listar produtos por tipo

}
