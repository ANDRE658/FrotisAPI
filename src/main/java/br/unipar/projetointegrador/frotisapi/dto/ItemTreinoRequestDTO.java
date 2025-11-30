// FrotisAPI/.../dto/ItemTreinoRequestDTO.java

package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemTreinoRequestDTO {
    private Long exercicioId;
    private Integer series;
    private Integer repeticoes; // No model Item_treino é String? Verifiquei, é String. Mude aqui se necessário.

    // Novos campos para suportar a Web completa
    private Integer carga;
    private Integer tempoDescansoSegundos;
}