package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.CadastroAdminDTO;
import br.unipar.projetointegrador.frotisapi.model.Usuario;
import br.unipar.projetointegrador.frotisapi.model.enums.RoleEnum;
import br.unipar.projetointegrador.frotisapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarGerenciador(@RequestBody CadastroAdminDTO dto) {
        // Verifica se já existe
        if (usuarioRepository.findByLoginOrEmail(dto.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: Já existe um usuário com este login.");
        }

        Usuario novoAdmin = new Usuario();
        novoAdmin.setLogin(dto.getLogin());
        novoAdmin.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoAdmin.setRole(RoleEnum.ROLE_GERENCIADOR);
        // IDs de aluno e instrutor ficam NULL, pois é um admin puro

        usuarioRepository.save(novoAdmin);

        return ResponseEntity.ok("Novo Gerenciador cadastrado com sucesso!");
    }
}