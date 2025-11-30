package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.model.Mensalidade;
import br.unipar.projetointegrador.frotisapi.service.MensalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensalidade")
public class MensalidadeController {

    @Autowired
    private MensalidadeService mensalidadeService;

    @GetMapping("/listar")
    public ResponseEntity<List<Mensalidade>> listar() {
        List<Mensalidade> lista = mensalidadeService.listarTodas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/gerar-agora")
    public ResponseEntity<Void> gerarMensalidades() {
        mensalidadeService.gerarMensalidadesDoMes();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pagar/{id}")
    public ResponseEntity<Mensalidade> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(mensalidadeService.pagar(id));
    }
}