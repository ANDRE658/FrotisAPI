package br.unipar.projetointegrador.frotisapi.repository;

import br.unipar.projetointegrador.frotisapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca Exata (Padrão)
    Optional<Usuario> findByLogin(String login);

    // --- SMART LOGIN REFORÇADO (TRIM + LOWER) ---
    @Query("SELECT u FROM Usuario u " +
            "LEFT JOIN u.aluno a " +
            "LEFT JOIN u.instrutor i " +
            "WHERE LOWER(TRIM(u.login)) = LOWER(TRIM(:identificador)) " +
            "OR LOWER(TRIM(a.email)) = LOWER(TRIM(:identificador)) " +
            "OR LOWER(TRIM(i.email)) = LOWER(TRIM(:identificador))")
    Optional<Usuario> findByLoginOrEmail(@Param("identificador") String identificador);
}