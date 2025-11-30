package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FichaCompletaRequestDTO {
    private Long alunoId;
    private Long instrutorId;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Lista de Dias (Treinos)
    private List<DiaTreinoRequestDTO> dias;
}