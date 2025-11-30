package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class AlunoUpdateDTO {
    private String nome;
    private Date dataNascimento;
    private String telefone;
    private String email;

    // Dados de Endereço (Iguais ao Android)
    private String cep;
    private String rua;
    private String cidade;
    private String estado;
    private String numero;
    private String bairro;
}