package br.unipar.projetointegrador.frotisapi.model;

import br.unipar.projetointegrador.frotisapi.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuario") // Define o nome da tabela no banco
@Getter
@Setter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login; // Pode ser CPF, Email, etc.

    @Column(nullable = false)
    @JsonIgnore // Nunca retorna a senha no JSON
    private String senha;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    // Relacionamento Opcional com Aluno (Só preenchido se for Aluno)
    @OneToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    // Relacionamento Opcional com Instrutor (Só preenchido se for Instrutor)
    @OneToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    // --- Métodos do UserDetails (Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte o Enum para a autoridade que o Spring entende
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() { return this.senha; }

    @Override
    public String getUsername() { return login; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}