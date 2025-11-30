package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.HistoricoTreinoDTO;
import br.unipar.projetointegrador.frotisapi.model.Aluno;
import br.unipar.projetointegrador.frotisapi.model.HistoricoTreino;
import br.unipar.projetointegrador.frotisapi.model.Treino;
import br.unipar.projetointegrador.frotisapi.repository.AlunoRepository;
import br.unipar.projetointegrador.frotisapi.repository.HistoricoTreinoRepository;
import br.unipar.projetointegrador.frotisapi.repository.TreinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired private HistoricoTreinoRepository historicoRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private TreinoRepository treinoRepository;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarHistorico(@RequestBody HistoricoTreinoDTO dto) {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = alunoRepository.findByCpf(cpf).orElseThrow();

        HistoricoTreino historico = new HistoricoTreino();
        historico.setAluno(aluno);
        historico.setDuracaoSegundos(dto.getDuracaoSegundos());
        historico.setCargaTotal(dto.getCargaTotalLevantada());
        historico.setTotalSeries(dto.getTotalSeries());
        historico.setData(LocalDate.now());

        if(dto.getTreinoId() != null) {
            Treino treino = treinoRepository.findById(dto.getTreinoId()).orElse(null);
            historico.setTreino(treino);
        }

        historicoRepository.save(historico);
        return ResponseEntity.ok("Histórico salvo com sucesso!");
    }

    // Endpoint para o Gráfico
    @GetMapping("/meu-progresso")
    public ResponseEntity<List<HistoricoTreino>> getMeuProgresso() {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = alunoRepository.findByCpf(cpf).orElseThrow();
        // Retorna os últimos 30 treinos
        return ResponseEntity.ok(historicoRepository.findTop30ByAlunoIdOrderByDataDesc(aluno.getId()));
    }

    @GetMapping("/realizados")
    public ResponseEntity<List<HistoricoTreino>> getExerciciosRealizados() {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        Aluno aluno = alunoRepository.findByCpf(cpf).orElseThrow();

        // Busca todos os históricos desse aluno, ordenados por data (mais recente primeiro)
        List<HistoricoTreino> realizados = historicoRepository.findByAlunoIdOrderByDataDesc(aluno.getId());

        return ResponseEntity.ok(realizados);
    }
}