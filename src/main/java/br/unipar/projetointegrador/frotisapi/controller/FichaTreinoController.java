package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.FichaCompletaRequestDTO;
import br.unipar.projetointegrador.frotisapi.model.FichaTreino;
import br.unipar.projetointegrador.frotisapi.repository.FichaTreinoRepository;
import br.unipar.projetointegrador.frotisapi.service.FichaTreinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/fichas", "/ficha-treino"})
@CrossOrigin(origins = "*")
public class FichaTreinoController {

    @Autowired
    private FichaTreinoService fichaTreinoService;
    private final FichaTreinoRepository fichaTreinoRepository;

    @Autowired
    public FichaTreinoController(FichaTreinoRepository fichaTreinoRepository) {
        this.fichaTreinoRepository = fichaTreinoRepository;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<FichaTreino>> listarTodas() {
        return ResponseEntity.ok(fichaTreinoRepository.findAll());
    }

    // POST: Cria uma ficha COMPLETA (Ficha -> Treinos -> Exercícios) para um aluno
    @PostMapping("/aluno/{alunoId}")
    public ResponseEntity<FichaTreino> criarFicha(@PathVariable Long alunoId, @RequestBody FichaTreino ficha) {
        FichaTreino novaFicha = fichaTreinoService.criarFicha(alunoId, ficha);
        return ResponseEntity.ok(novaFicha);
    }

    // GET: Busca todas as fichas de um aluno
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<FichaTreino>> listarPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(fichaTreinoService.buscarPorAluno(alunoId));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<FichaTreino> buscarPorId(@PathVariable Long id) {
        return fichaTreinoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/salvar")
    public ResponseEntity<FichaTreino> salvar(@RequestBody FichaTreino ficha) {
        return ResponseEntity.ok(fichaTreinoRepository.save(ficha));
    }

    @PostMapping("/salvar-completa")
    public ResponseEntity<FichaTreino> salvarCompleta(@RequestBody FichaCompletaRequestDTO dto) {
        try {
            FichaTreino novaFicha = fichaTreinoService.salvarFichaCompleta(dto);
            return ResponseEntity.status(201).body(novaFicha);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}