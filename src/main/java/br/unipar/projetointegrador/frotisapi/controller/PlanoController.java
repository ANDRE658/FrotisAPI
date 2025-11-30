package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.model.Plano;
import br.unipar.projetointegrador.frotisapi.service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plano")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Plano>> listar() {
        return ResponseEntity.ok(planoService.listarTodos());
    }

    // --- MÉTODOS QUE FALTAVAM ---

    @PostMapping("/salvar")
    public ResponseEntity<Plano> salvar(@RequestBody Plano plano) {
        Plano planoSalvo = planoService.salvar(plano);
        return ResponseEntity.status(HttpStatus.CREATED).body(planoSalvo);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Plano> buscarPorId(@PathVariable Long id) {
        Plano plano = planoService.buscarPorId(id);
        if (plano != null) {
            return ResponseEntity.ok(plano);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            planoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Plano> atualizar(@PathVariable Long id, @RequestBody Plano plano) {
        try {
            // Garante que o ID do objeto é o mesmo da URL
            plano.setId(id);
            // O service.atualizar verifica se existe antes de salvar
            Plano atualizado = planoService.atualizar(plano);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}