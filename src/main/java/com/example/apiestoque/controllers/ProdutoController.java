package com.example.apiestoque.controllers;

import com.example.apiestoque.dto.IdDTO;
import com.example.apiestoque.dto.ProdutoDTO;
import com.example.apiestoque.dto.ProdutoPatchDTO;
import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import com.example.apiestoque.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/produto")
public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final ProdutoService service;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
        this.service = new ProdutoService(this.produtoRepository);
    }

    @PostMapping()
    @Operation(summary = "Criar produto", description = "Cria um novo produto e o adiciona ao estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody ProdutoDTO produto, BindingResult result ){
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body("Erro de validação: " + errors);
        }

        try{
            this.service.create(produto.toProduto());

        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Erro ao inserir");
        }
        return ResponseEntity.ok("Produto inserido com sucesso");
    }

    @GetMapping()
    @Operation(summary = "Lista todos os produtos",
            description = "Retorna uma lista de todos os produtos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retonada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public List<Produto> listaProdutos() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public  ResponseEntity<Object> getById(@PathVariable Long id) {

        if(id<0) {
            return ResponseEntity.badRequest().body("O id não pode ser negativo");
        }

        try{
            Produto produto = this.service.getById(id);
            return ResponseEntity.status(200).body(produto);

        }catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto", description = "Exclui um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })    public ResponseEntity<String> excluirProduto(@PathVariable Long id){

        if(id<0) {
            return ResponseEntity.badRequest().body("O id não pode ser negativo");
        }

        try {
            this.service.delete(id);

        }catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (Exception e ){
            return ResponseEntity.internalServerError().body("Erro ao deletar");
        }

        return ResponseEntity.ok("Produto excluido com sucesso");


//        Optional<Produto> produtoExists = produtoRepository.findById(id);
//
//        if(produtoExists.isPresent()){
//            produtoRepository.deleteById(id);
//            return ResponseEntity.ok("Produto excluido com sucesso");
//        }
//
//        return ResponseEntity.status(404).body("Produto não encontrado");

//        Optional<Produto> produtoExists = produtoRepository.findById(id);
//
//        if(produtoExists.isPresent()){
//            produtoRepository.delete(produtoExists.get());
//            return ResponseEntity.ok("Produto excluido com sucesso");
//        }
//
//        return ResponseEntity.status(404).body("Produto não encontrado");

//        if(produtoRepository.existsById(id)) {
//            produtoRepository.deleteById(id);
//            return ResponseEntity.ok("Produto excluido com sucesso");
//        }
//        return ResponseEntity.status(404).body("Produto não encontrado");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO produtoToUpdate, BindingResult result){
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body("Erro de validação: " + errors);
        }


        try{
            this.service.update(id, produtoToUpdate.toProduto());
            return ResponseEntity.ok("Produto atulizado");

        }catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }catch (Exception e ){
            return ResponseEntity.internalServerError().body("Erro ao atualizar");
        }
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<String> atualizarProdutoParcial(@PathVariable Long id, @RequestBody Map<String, Object> dataToUpdate){
//
//        if(id<0) {
//            return ResponseEntity.badRequest().body("O id não pode ser negativo");
//        }
//
//        if(Double.parseDouble((String) dataToUpdate.get("preco")) < 0){
//            return ResponseEntity.badRequest().body("O preço não pode ser negativo");
//        }
//
//        if(Integer.parseInt((String) dataToUpdate.get("quantidadeEstoque")) < 0) {
//            return ResponseEntity.badRequest().body("O estoque não pode ser negativo");
//        }
//
//        try{
//            this.service.updatePartial(id, dataToUpdate);
//            return ResponseEntity.ok("Produto atulizado");
//
//        }catch (EntityNotFoundException e){
//            return ResponseEntity.status(404).body(e.getMessage());
//        }catch (Exception e ){
//            return ResponseEntity.internalServerError().body("Erro ao atualizar");
//        }
//    }

    //SOLUCAO COM DTO
//@PatchMapping("/{id}")
//public ResponseEntity<String> atualizarProdutoParcial(@PathVariable Long id, @Valid @RequestBody ProdutoPatchDTO produtoPatchDTO, BindingResult result) {
//    if (result.hasErrors()) {
//        List<String> errors = result.getAllErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .toList();
//        return ResponseEntity.badRequest().body("Erro de validação: " + errors);
//    }
//
//    try {
//        this.service.updatePartial(id, produtoPatchDTO);
//        return ResponseEntity.ok("Produto atualizado");
//    } catch (EntityNotFoundException e) {
//        return ResponseEntity.status(404).body(e.getMessage());
//    } catch (Exception e) {
//        return ResponseEntity.internalServerError().body("Erro ao atualizar");
//    }
//}
    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um produto", description = "Atualiza parcialmente um produto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<String> atualizarProdutoParcial(@PathVariable Long id, @Valid @RequestBody ProdutoPatchDTO produtoPatchDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body("Erro de validação: " + errors);
        }

        try {
            this.service.updatePartial(id, produtoPatchDTO);
            return ResponseEntity.ok("Produto atualizado");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar");
        }
    }


    @GetMapping("/nome")
    @Operation(summary = "Buscar produto por nome", description = "Retorna os detalhes de produtos pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto[].class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado", content = @Content)
    })
    public  ResponseEntity<Object> getByNome(@RequestParam String nome) {
        try{
            Produto[] produto = this.service.getByNome(nome);
            return ResponseEntity.status(200).body(produto);

        }catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/nomeAndPreco")
    @Operation(summary = "Buscar produto por nome e preço", description = "Retorna os detalhes de produtos pelo nome e preço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto[].class))),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado", content = @Content)
    })
    public  ResponseEntity<Object> getByNomeAndPreco(@RequestParam String nome,@RequestParam double preco) {
        try{
            Produto[] produto = this.service.getByNomeAndPreco(nome, preco);
            return ResponseEntity.status(200).body(produto);

        }catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
