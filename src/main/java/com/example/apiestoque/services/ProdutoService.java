package com.example.apiestoque.services;

import com.example.apiestoque.dto.ProdutoPatchDTO;
import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    public Produto getById(Long id){
        return produtoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Produto não encontrado"));
    }

    public void delete(Long id){
        this.getById(id);
        this.produtoRepository.deleteById(id);
    }

    public void create(Produto produto){
        Produto createdEntity = produtoRepository.save(produto);
        this.getById(createdEntity.getId());
    }

    public void update(Long id, Produto produtoToUpdate){
        Produto produto = this.getById(id);

        produto.setNome(produtoToUpdate.getNome());
        produto.setDescricao(produtoToUpdate.getDescricao());
        produto.setPreco(produtoToUpdate.getPreco());
        produto.setQuantidadeEstoque(produtoToUpdate.getQuantidadeEstoque());
        produtoRepository.save(produto);

    }

    public void updatePartial(Long id, ProdutoPatchDTO dataToUpdate){
//        Produto produto = this.getById(id);
//
//        if(dataToUpdate.containsKey("descricao")){
//            produto.setDescricao((String)( dataToUpdate.get("descricao")));
//        }
//
//        if(dataToUpdate.containsKey("nome")){
//            produto.setNome((String) (dataToUpdate.get("nome")));
//        }
//
//        if(dataToUpdate.containsKey("preco")){
//            produto.setPreco((Double) (dataToUpdate.get("preco")));
//        }
//
//        if(dataToUpdate.containsKey("quantidadeEstoque")){
//            produto.setQuantidadeEstoque((int) (dataToUpdate.get("quantidadeEstoque")));
//        }
//
//        produtoRepository.save(produto);

        Produto produto = this.getById(id);

        if (dataToUpdate.getDescricao() != null) {
            produto.setDescricao(dataToUpdate.getDescricao());
        }

        if (dataToUpdate.getNome() != null) {
            produto.setNome(dataToUpdate.getNome());
        }

        if (dataToUpdate.getPreco() != null) {
            produto.setPreco(dataToUpdate.getPreco());
        }

        if (dataToUpdate.getQuantidadeEstoque() != null) {
            produto.setQuantidadeEstoque(dataToUpdate.getQuantidadeEstoque());
        }

        produtoRepository.save(produto);
    }

    public Produto[] getByNome(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public Produto[] getByNomeAndPreco(String nome, double preco){
        return produtoRepository.findByNomeLikeIgnoreCaseAndPrecoLessThanEqual(nome, preco);
    }


}