package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DiaTreinoRequestDTO {
    private String diaSemana; // SEGUNDA, TERCA...
    private String nomeTreino;
    private List<ItemTreinoRequestDTO> exercicios;
}