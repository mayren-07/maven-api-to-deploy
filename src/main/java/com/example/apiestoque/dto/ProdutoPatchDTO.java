package com.example.apiestoque.dto;

import com.example.apiestoque.models.Produto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProdutoPatchDTO {
    @Size(min = 3, max = 100, message = "Informe no mínimo 3 caracteres e no máximo 100")
    private String nome;

    @Size(min = 3, max = 100, message = "Informe no mínimo 3 caracteres e no máximo 100")
    private String descricao;

    @PositiveOrZero(message = "O preço deve ser maior ou igual a zero")
    private Double preco;

    @Positive(message = "O estoque não pode ser negativo")
    private Integer quantidadeEstoque;

    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        return produto;
    }
}
