package br.unipar.projetointegrador.frotisapi.dto;

import br.unipar.projetointegrador.frotisapi.model.Aluno;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class AlunoResponseDTO {
    // Dados do Aluno
    private Long id; // Importante: nome deve bater com o @SerializedName("id") do Android
    private String nome;
    private String cpf;
    private String email;
    private Date dataNascimento;
    private String telefone;
    private String sexo;
    private float altura;
    private float peso;

    // Dados do Endereço (Planos/Na Raiz para o Android ler)
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // Construtor que converte a Entidade em DTO
    public AlunoResponseDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nome = aluno.getNome();
        this.cpf = aluno.getCpf();
        this.email = aluno.getEmail();
        this.dataNascimento = aluno.getDataNascimento();
        this.telefone = aluno.getTelefone();
        this.sexo = aluno.getSexo();
        this.altura = aluno.getAltura();
        this.peso = aluno.getPeso();

        if (aluno.getEndereco() != null) {
            this.rua = aluno.getEndereco().getRua();
            this.numero = aluno.getEndereco().getNumero();
            this.bairro = aluno.getEndereco().getBairro();
            this.cidade = aluno.getEndereco().getCidade();
            this.estado = aluno.getEndereco().getEstado();
            this.cep = aluno.getEndereco().getCep();
        }
    }
}