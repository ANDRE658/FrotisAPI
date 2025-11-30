package br.unipar.projetointegrador.frotisapi.repository;

import br.unipar.projetointegrador.frotisapi.model.Mensalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {

    // Busca mensalidades de um aluno específico (útil para histórico)
    List<Mensalidade> findByAlunoId(Long alunoId);

    // Verifica se já existe mensalidade para este aluno com este vencimento
    // (Evita duplicidade ao clicar em "Gerar" várias vezes)
    @Query("SELECT COUNT(m) > 0 FROM Mensalidade m WHERE m.aluno.id = :alunoId AND m.dataVencimento = :vencimento")
    boolean existsByAlunoAndVencimento(@Param("alunoId") Long alunoId, @Param("vencimento") Date vencimento);

    // Ordena por vencimento (mais recentes primeiro ou antigas primeiro)
    List<Mensalidade> findAllByOrderByDataVencimentoDesc();
}