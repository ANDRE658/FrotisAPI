package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.ExercicioDTO;
import br.unipar.projetointegrador.frotisapi.model.Exercicio;
import br.unipar.projetointegrador.frotisapi.model.Treino;
import br.unipar.projetointegrador.frotisapi.repository.ExercicioRepository;
import br.unipar.projetointegrador.frotisapi.repository.TreinoRepository;
import br.unipar.projetointegrador.frotisapi.service.ExercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exercicio")
public class ExercicioController {

    private final ExercicioService exercicioService;

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private TreinoRepository treinoRepository;
    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    /**
     * ATUALIZADO (Melhor Prática):
     * O Controller agora retorna uma Lista de DTOs.
     */
    @GetMapping("/listar")
    public ResponseEntity<List<ExercicioDTO>> listarTodos() {
        List<Exercicio> lista = exercicioRepository.findAll();
        List<ExercicioDTO> dtos = lista.stream().map(this::converterParaDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * ATUALIZADO (Melhor Prática):
     * O Controller agora recebe um DTO do cliente e já retorna o DTO
     * que o serviço preparou.
     */
    @PostMapping("/salvar/{treinoId}")
    public ResponseEntity<ExercicioDTO> salvarExercicio(
            @PathVariable Long treinoId,
            @RequestBody ExercicioDTO exercicioDTO) { // <-- Recebe DTO
        try {
            // O serviço agora recebe DTO e retorna DTO
            ExercicioDTO novoExercicioDTO = exercicioService.salvarExercicio(treinoId, exercicioDTO);
            return ResponseEntity.status(201).body(novoExercicioDTO); // 201 Created
        } catch (Exception e) {
            // Se o Treino não for encontrado
            return ResponseEntity.notFound().build(); // Retorna 404
        }
    }

    @PostMapping("/treino/{treinoId}")
    public ResponseEntity<ExercicioDTO> adicionarExercicio(@PathVariable Long treinoId, @RequestBody ExercicioDTO dto) {
        System.out.println("--- TENTATIVA ADICIONAR EXERCÍCIO ---");
        System.out.println("Treino ID: " + treinoId);
        System.out.println("Exercicio: " + dto.getNome());

        Optional<Treino> treinoOpt = treinoRepository.findById(treinoId);

        if (treinoOpt.isPresent()) {
            Exercicio exercicio = new Exercicio();
            exercicio.setNome(dto.getNome());
            exercicio.setSeries(dto.getSeries());
            exercicio.setRepeticoes(dto.getRepeticoes());
            exercicio.setTreino(treinoOpt.get());

            Exercicio salvo = exercicioRepository.save(exercicio);
            System.out.println("SUCESSO: Exercício salvo com ID " + salvo.getId());

            dto.setId(salvo.getId());
            return ResponseEntity.ok(dto);
        } else {
            System.out.println("ERRO: Treino " + treinoId + " não encontrado no banco.");
            return ResponseEntity.notFound().build();
        }
    }

    // --- MÉTODO QUE FALTAVA: Atualizar exercício ---
    @PutMapping("/{exercicioId}")
    public ResponseEntity<ExercicioDTO> atualizarExercicio(@PathVariable Long exercicioId, @RequestBody ExercicioDTO dto) {
        Optional<Exercicio> exercicioOpt = exercicioRepository.findById(exercicioId);

        if (exercicioOpt.isPresent()) {
            Exercicio exercicio = exercicioOpt.get();
            exercicio.setNome(dto.getNome());
            exercicio.setSeries(dto.getSeries());
            exercicio.setRepeticoes(dto.getRepeticoes());

            exercicioRepository.save(exercicio);

            return ResponseEntity.ok(dto); // Retorna o DTO atualizado
        }
        return ResponseEntity.notFound().build();
    }

    // --- Método Deletar ---
    @DeleteMapping("/{exercicioId}")
    public ResponseEntity<Void> deletarExercicio(@PathVariable Long exercicioId) {
        if (exercicioRepository.existsById(exercicioId)) {
            exercicioRepository.deleteById(exercicioId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/salvar")
    public ResponseEntity<ExercicioDTO> salvarExercicioGenerico(@RequestBody ExercicioDTO dto) {
        // Salva sem treino (null) para ser usado como modelo no catálogo
        Exercicio exercicio = converterDTO(dto);
        exercicio.setTreino(null);
        Exercicio salvo = exercicioRepository.save(exercicio);
        return ResponseEntity.status(201).body(converterParaDTO(salvo));
    }

    private Exercicio converterDTO(ExercicioDTO dto) {
        Exercicio e = new Exercicio();
        e.setId(dto.getId());
        e.setNome(dto.getNome());
        e.setSeries(dto.getSeries());
        e.setRepeticoes(dto.getRepeticoes());
        return e;
    }
    private ExercicioDTO converterParaDTO(Exercicio e) {
        ExercicioDTO dto = new ExercicioDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setSeries(e.getSeries());
        dto.setRepeticoes(e.getRepeticoes());
        return dto;
    }
}
