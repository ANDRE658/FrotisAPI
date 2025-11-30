package br.unipar.projetointegrador.frotisapi.service;

import br.unipar.projetointegrador.frotisapi.model.Aluno;
import br.unipar.projetointegrador.frotisapi.model.Matricula;
import br.unipar.projetointegrador.frotisapi.model.Mensalidade;
import br.unipar.projetointegrador.frotisapi.repository.MatriculaRepository;
import br.unipar.projetointegrador.frotisapi.repository.MensalidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MensalidadeService {

    @Autowired private MensalidadeRepository mensalidadeRepository;
    @Autowired private MatriculaRepository matriculaRepository;

    public List<Mensalidade> listarTodas() {
        return mensalidadeRepository.findAllByOrderByDataVencimentoDesc();
    }

    public Mensalidade pagar(Long id) {
        Mensalidade m = mensalidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensalidade não encontrada"));
        m.setPago(true);
        m.setDataPagamento(new Date());
        return mensalidadeRepository.save(m);
    }

    public void gerarMensalidadesDoMes() {
        // 1. Busca todas as matrículas
        List<Matricula> matriculas = matriculaRepository.findAll();

        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();

        for (Matricula matricula : matriculas) {
            // Só gera se a matrícula estiver ativa e tiver plano
            if (Boolean.TRUE.equals(matricula.getAtiva()) && matricula.getPlano() != null) {

                // Define o dia de vencimento (Padrão 10 se não tiver definido)
                int diaVenc = (matricula.getDiaVencimento() != null && matricula.getDiaVencimento() > 0)
                        ? matricula.getDiaVencimento() : 10;

                // Cria a data de vencimento deste mês
                LocalDate dataVencLocal = LocalDate.of(anoAtual, mesAtual, diaVenc);

                // Se hoje já passou do dia de vencimento, talvez queira gerar para o mês que vem?
                // Por simplicidade, vamos gerar sempre para o mês corrente.

                Date dataVencimento = Date.from(dataVencLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

                // Verifica se já gerou para não duplicar
                boolean jaExiste = mensalidadeRepository.existsByAlunoAndVencimento(matricula.getAluno().getId(), dataVencimento);

                if (!jaExiste) {
                    Mensalidade nova = new Mensalidade();
                    nova.setAluno(matricula.getAluno());
                    nova.setMatricula(matricula);
                    nova.setValor(matricula.getPlano().getValor());
                    nova.setDataVencimento(dataVencimento);
                    nova.setPago(false);

                    mensalidadeRepository.save(nova);
                }
            }
        }
    }
}