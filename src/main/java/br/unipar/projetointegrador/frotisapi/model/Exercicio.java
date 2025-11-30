package br.unipar.projetointegrador.frotisapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int series;
    private int repeticoes;

    // --- CORREÇÃO AQUI ---
    // O nome deve ser IGUAL ao definido na lista 'exercicios' da classe Treino.
    @JsonBackReference("treino-exercicios")
    @ManyToOne
    @JoinColumn(name = "treino_id")
    private Treino treino;
}