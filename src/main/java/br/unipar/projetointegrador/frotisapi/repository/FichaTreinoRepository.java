package br.unipar.projetointegrador.frotisapi.repository;

import br.unipar.projetointegrador.frotisapi.model.FichaTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichaTreinoRepository extends JpaRepository<FichaTreino, Long> {
    // Busca as fichas de um aluno específico
    List<FichaTreino> findByAlunoId(Long alunoId);
}