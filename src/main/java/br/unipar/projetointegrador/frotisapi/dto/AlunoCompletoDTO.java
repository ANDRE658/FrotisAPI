package br.unipar.projetointegrador.frotisapi.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class AlunoCompletoDTO {
    // Dados Pessoais
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Date dataNascimento;
    private String sexo;
    private Float altura;
    private Float peso;
    private String senha;

    // Endereço
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    //plano
    private Long planoId;
    private Integer diaVencimento;
    private Long instrutorId;
}