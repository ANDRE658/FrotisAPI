package br.unipar.projetointegrador.frotisapi.dto;

import java.util.Date;

public class InstrutorUpdateDTO {
    private String nome;
    private Date dataNascimento;
    private String telefone;
    private String email;

    // Dados de Endereço (Iguais ao Android)
    private String cep;
    private String rua;
    private String cidade;
    private String estado;
}
