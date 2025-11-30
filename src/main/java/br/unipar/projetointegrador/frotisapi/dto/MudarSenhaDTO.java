package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MudarSenhaDTO {
    private String senhaAtual;
    private String novaSenha;
}