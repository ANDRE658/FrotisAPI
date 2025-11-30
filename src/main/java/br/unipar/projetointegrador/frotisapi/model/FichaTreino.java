package br.unipar.projetointegrador.frotisapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class FichaTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao; // Ex: "Hipertrofia Iniciante"

    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    private Date dataFim;

    private Boolean ativa = true;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    @JsonIgnore // O Aluno já carrega as fichas, evitamos loop
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    // Uma Ficha tem VÁRIOS Treinos (A, B, C)
    @OneToMany(mappedBy = "fichaTreino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // A ficha serializa os treinos
    private List<Treino> treinos = new ArrayList<>();

    public String getNomeAluno() {
        return (aluno != null) ? aluno.getNome() : "Aluno Removido";
    }

    public Long getIdAluno() {
        return (aluno != null) ? aluno.getId() : null;
    }


}