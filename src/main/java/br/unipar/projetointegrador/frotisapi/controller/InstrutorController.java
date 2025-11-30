package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.AlunoUpdateDTO; // Reaproveitando DTO compatível
import br.unipar.projetointegrador.frotisapi.model.Endereco;
import br.unipar.projetointegrador.frotisapi.model.Instrutor;
import br.unipar.projetointegrador.frotisapi.model.Usuario;
import br.unipar.projetointegrador.frotisapi.repository.EnderecoRepository;
import br.unipar.projetointegrador.frotisapi.repository.UsuarioRepository;
import br.unipar.projetointegrador.frotisapi.service.InstrutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutor")
public class InstrutorController {

    private final InstrutorService instrutorService;
    private final UsuarioRepository usuarioRepository;   // <--- Faltava declarar isso
    private final EnderecoRepository enderecoRepository; // <--- Faltava declarar isso

    @Autowired
    public InstrutorController(InstrutorService instrutorService,
                               UsuarioRepository usuarioRepository,
                               EnderecoRepository enderecoRepository) {
        this.instrutorService = instrutorService;
        this.usuarioRepository = usuarioRepository;     // <--- Injeção aqui
        this.enderecoRepository = enderecoRepository;   // <--- Injeção aqui
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Instrutor>> listar() {
        return ResponseEntity.ok(instrutorService.listarTodos());
    }

    @PostMapping("/salvar")
    public ResponseEntity<Instrutor> salvar(@RequestBody Instrutor instrutor) {
        return ResponseEntity.ok(instrutorService.salvar(instrutor));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Instrutor> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(instrutorService.buscarPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        instrutorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint de Atualização do Perfil (Instrutor) ---
    @PutMapping("/me")
    public ResponseEntity<Instrutor> atualizarMe(@RequestBody AlunoUpdateDTO dto) {
        try {
            // 1. Identifica usuário logado
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            Usuario usuario = usuarioRepository.findByLoginOrEmail(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Instrutor instrutor = usuario.getInstrutor();

            if (instrutor == null) {
                return ResponseEntity.notFound().build();
            }

            // 2. Atualiza Dados Básicos
            if (dto.getNome() != null) instrutor.setNome(dto.getNome());
            if (dto.getEmail() != null) instrutor.setEmail(dto.getEmail());
            if (dto.getTelefone() != null) instrutor.setTelefone(dto.getTelefone());

            // 3. Atualiza Endereço
            Endereco endereco = instrutor.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
            }

            if (dto.getRua() != null) endereco.setRua(dto.getRua());
            if (dto.getCidade() != null) endereco.setCidade(dto.getCidade());
            if (dto.getEstado() != null) endereco.setEstado(dto.getEstado());
            if (dto.getCep() != null) endereco.setCep(dto.getCep());
            if (dto.getNumero() != null) endereco.setNumero(dto.getNumero());
            if (dto.getBairro() != null) endereco.setBairro(dto.getBairro());

            enderecoRepository.save(endereco);
            instrutor.setEndereco(endereco);

            // 4. Salva Instrutor
            return ResponseEntity.ok(instrutorService.salvar(instrutor));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Instrutor> atualizar(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        Instrutor instrutorAtualizado = instrutorService.atualizar(id, instrutor);

        if (instrutorAtualizado != null) {
            return ResponseEntity.ok(instrutorAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}