package br.unipar.projetointegrador.frotisapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "aluno_id")
    @com.fasterxml.jackson.annotation.JsonBackReference("aluno-matriculas") // O nome deve ser IGUAL ao do Aluno.java
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Plano plano;

    private Integer diaVencimento;

    @Temporal(TemporalType.DATE)
    private Date dataInicio = new Date();

    private Boolean ativa = true;
}
