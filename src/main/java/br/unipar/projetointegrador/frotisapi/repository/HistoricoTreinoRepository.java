package br.unipar.projetointegrador.frotisapi.repository;

import br.unipar.projetointegrador.frotisapi.model.HistoricoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoTreinoRepository extends JpaRepository<HistoricoTreino, Long> {

    // 1. Busca os últimos 30 treinos para o GRÁFICO (já existia)
    List<HistoricoTreino> findTop30ByAlunoIdOrderByDataDesc(Long alunoId);

    // 2. Busca TODOS os treinos ordenados para a LISTA DE HISTÓRICO (O que faltava)
    List<HistoricoTreino> findByAlunoIdOrderByDataDesc(Long alunoId);
}