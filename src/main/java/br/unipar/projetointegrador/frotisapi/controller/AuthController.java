package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.LoginRequest;
import br.unipar.projetointegrador.frotisapi.dto.LoginResponse;
import br.unipar.projetointegrador.frotisapi.dto.MudarSenhaDTO;
import br.unipar.projetointegrador.frotisapi.repository.UsuarioRepository;
import br.unipar.projetointegrador.frotisapi.service.AuthService;
import br.unipar.projetointegrador.frotisapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import br.unipar.projetointegrador.frotisapi.model.Usuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("=== TENTATIVA DE LOGIN UNIFICADO ===");
            System.out.println("Identificador: " + loginRequest.getCpf()); // O front manda no campo 'cpf'

            // 1. Autentica usando o Spring Security (Vai chamar o UserDetailsServiceImpl)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCpf(),
                            loginRequest.getSenha()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Recupera o usuário completo do banco para pegar os dados (Nome, IDs)
            Usuario usuario = usuarioRepository.findByLoginOrEmail(loginRequest.getCpf())
                    .orElseThrow(() -> new RuntimeException("Erro de integridade: Usuário autenticado não encontrado no banco."));

            // 3. Gera o Token com os dados extras (alunoId, instrutorId, role)
            Long alunoId = (usuario.getAluno() != null) ? usuario.getAluno().getId() : null;
            Long instrutorId = (usuario.getInstrutor() != null) ? usuario.getInstrutor().getId() : null;
            String nome = (usuario.getAluno() != null) ? usuario.getAluno().getNome() :
                    (usuario.getInstrutor() != null ? usuario.getInstrutor().getNome() : "Admin");

            String token = jwtUtil.generateToken(
                    usuario.getLogin(),
                    usuario.getRole().name(),
                    alunoId,
                    instrutorId
            );

            System.out.println("LOGIN SUCESSO: " + nome + " (" + usuario.getRole() + ")");

            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso", token, nome));

        } catch (Exception e) {
            System.out.println("LOGIN FALHOU: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<String> alterarSenha(@RequestBody MudarSenhaDTO dto) {
        try {
            // 1. Pega o login do usuário autenticado (Token)
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            // 2. Busca na tabela Usuario (que é comum a todos)
            Usuario usuario = usuarioRepository.findByLoginOrEmail(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

            // 3. Valida senha atual
            if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
                return ResponseEntity.badRequest().body("A senha atual está incorreta.");
            }

            // 4. Salva nova senha
            usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

            // Se for Aluno, atualiza também na tabela Aluno (para compatibilidade)
            if (usuario.getAluno() != null) {
                usuario.getAluno().setSenha(usuario.getSenha());
            }

            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Senha alterada com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}