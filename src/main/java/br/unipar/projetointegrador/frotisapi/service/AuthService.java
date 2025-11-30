package br.unipar.projetointegrador.frotisapi.service;

import br.unipar.projetointegrador.frotisapi.model.Aluno;
import br.unipar.projetointegrador.frotisapi.repository.AlunoRepository;
import br.unipar.projetointegrador.frotisapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AlunoRepository alunoRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.alunoRepository = alunoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String cpf, String senhaPura) {
        System.out.println("--- Tentativa de Login (AuthService) ---");
        System.out.println("CPF Recebido: " + cpf);

        Aluno aluno = alunoRepository.findByCpf(cpf).orElse(null);

        if (aluno != null) {
            System.out.println("Aluno encontrado no DB. CPF: " + aluno.getCpf());
            System.out.println("Hash da Senha no DB: " + aluno.getSenha());

            boolean senhasBatem = passwordEncoder.matches(senhaPura, aluno.getSenha());
            System.out.println("Resultado da comparação (matches): " + senhasBatem);

            if (senhasBatem) {
                System.out.println("SENHAS CORRESPONDEM! Gerando token...");

                // --- CORREÇÃO AQUI ---
                // Atualizado para a nova assinatura: generateToken(username, role, alunoId, instrutorId)
                String token = jwtUtil.generateToken(
                        aluno.getCpf(),      // username
                        "ROLE_ALUNO",        // role (Hardcoded pois este serviço é específico de Aluno)
                        aluno.getId(),       // alunoId
                        null                 // instrutorId (nulo)
                );

                System.out.println("Token gerado: " + token);
                return token;
            } else {
                System.out.println("ERRO: Senhas não correspondem.");
            }
        } else {
            System.out.println("ERRO: Aluno não encontrado para o CPF: " + cpf);
        }

        System.out.println("Login falhou.");
        return null;
    }
}