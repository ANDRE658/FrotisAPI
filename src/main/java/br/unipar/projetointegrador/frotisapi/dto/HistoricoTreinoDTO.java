package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class HistoricoTreinoDTO {
    private Long alunoId;
    private Long treinoId;
    private Integer duracaoSegundos;
    private Double cargaTotalLevantada; // Soma de (peso * repetições * séries)
    private Integer totalSeries;
    private LocalDate dataTreino;
}