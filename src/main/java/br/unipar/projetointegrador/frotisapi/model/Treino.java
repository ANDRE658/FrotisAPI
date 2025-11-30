package br.unipar.projetointegrador.frotisapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String diaSemana;

    @ManyToOne
    @JoinColumn(name = "ficha_treino_id")
    @JsonBackReference
    private FichaTreino fichaTreino;

    // --- CORREÇÃO AQUI: @OrderBy garante a ordem dos exercícios ---
    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("treino-exercicios")
    @OrderBy("id ASC") // Ordena os exercícios por ordem de criação
    private List<Exercicio> exercicios = new ArrayList<>();
}