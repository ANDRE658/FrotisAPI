package br.unipar.projetointegrador.frotisapi.dto;

public class LoginResponse {

    private String message;
    private String token;
    private String nomeUsuario; // Campo Novo para a Web

    // Construtor Completo (3 argumentos) - Resolve o erro do AuthController
    public LoginResponse(String message, String token, String nomeUsuario) {
        this.message = message;
        this.token = token;
        this.nomeUsuario = nomeUsuario;
    }

    // Construtor Simples (2 argumentos) - Mantido para compatibilidade, se necessário
    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // --- Getters e Setters ---
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}