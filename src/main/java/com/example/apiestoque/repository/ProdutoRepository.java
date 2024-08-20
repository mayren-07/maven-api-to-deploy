package com.example.apiestoque.repository;

import com.example.apiestoque.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Query("delete from Produto where id = ?1")
    void deleteById(Long id);

    Produto[] findByNomeLikeIgnoreCase(String nome);
    Produto[] findByNomeLikeIgnoreCaseAndPrecoLessThanEqual(String nome, double preco);

}
