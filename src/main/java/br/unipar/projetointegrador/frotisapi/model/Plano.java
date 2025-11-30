package br.unipar.projetointegrador.frotisapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter // <-- ESSENCIAL PARA GERAR O getId()
@Setter // <-- ESSENCIAL PARA GERAR OS SETTERS
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double valor;
    private Boolean ativo = true;

    @OneToMany(mappedBy = "plano")
    @JsonIgnore
    private List<Matricula> matriculas;

    public Plano() {
    }
}