package br.unipar.projetointegrador.frotisapi.controller;

import br.unipar.projetointegrador.frotisapi.dto.*;
import br.unipar.projetointegrador.frotisapi.model.Aluno;
import br.unipar.projetointegrador.frotisapi.service.AlunoService;
import org.apache.catalina.connector.Response;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Aluno>> listarAlunos() {
        List<Aluno> alunos = alunoService.listarTodos();

        if (alunos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(alunos);
    }

    @PostMapping("/salvar")
    public ResponseEntity<Aluno> salvarAluno(@RequestBody Aluno aluno) {
        Aluno alunoSalvo = alunoService.salvar(aluno);
        return ResponseEntity.status(Response.SC_CREATED).body(alunoSalvo);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Aluno> buscarAlunoPorID(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aluno);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        Aluno aluno = alunoService.atualizar(id, alunoAtualizado);

        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/perfil")
    public ResponseEntity<AlunoResponseDTO> getPerfil() { // Retorno alterado para DTO
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = authentication.getName();

        Aluno aluno = alunoService.buscarPorCpf(cpf);

        if (aluno != null) {
            // Converte para o DTO que o Android entende
            return ResponseEntity.ok(new AlunoResponseDTO(aluno));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/perfil")
    public ResponseEntity<Aluno> atualizarPerfil(@RequestBody AlunoUpdateDTO alunoDTO) {
        try {
            // Obtém o CPF do token JWT (Usuário Logado)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String cpf = authentication.getName();

            Aluno alunoAtualizado = alunoService.atualizarPerfil(cpf, alunoDTO);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/mudar-senha")
    public ResponseEntity<String> mudarSenha(@RequestBody MudarSenhaDTO dto) {
        try {
            // 1. Identifica o usuário pelo Token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String cpf = authentication.getName();

            // 2. Tenta alterar a senha
            alunoService.alterarSenha(cpf, dto);

            return ResponseEntity.ok("Senha alterada com sucesso!");

        } catch (Exception e) {
            // Retorna 400 Bad Request com a mensagem de erro (ex: "Senha atual incorreta")
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //WEB

    @GetMapping("/me")
    public ResponseEntity<AlunoResponseDTO> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cpf = auth.getName();
        Aluno aluno = alunoService.buscarPorCpf(cpf);
        return (aluno != null) ? ResponseEntity.ok(new AlunoResponseDTO(aluno)) : ResponseEntity.notFound().build();
    }

    // 2. Estatísticas para o Dashboard (Admin/Gerente)
    @GetMapping("/estatisticas")
    public ResponseEntity<DashboardStatsDTO> getEstatisticas() {
        // Conta alunos totais para teste
        long total = alunoService.listarTodos().size();
        long ativos = alunoService.listarTodos().stream().filter(a -> a.getAtivo()).count();
        long inativos = total - ativos;

        return ResponseEntity.ok(new DashboardStatsDTO(ativos, inativos, 5)); // 5 novos (exemplo)
    }

    @GetMapping("/estatisticas/instrutor/{id}")
    public ResponseEntity<DashboardStatsDTO> getEstatisticasInstrutor(@PathVariable Long id) {
        // AQUI ERA ONDE O ERRO ACONTECIA (404)
        // O front chamava essa rota e ela não existia.

        // Lógica: Contar alunos vinculados a este instrutor via Ficha de Treino
        // Por enquanto, retornamos dados de teste para destravar a tela:
        return ResponseEntity.ok(new DashboardStatsDTO(12, 3, 1));
    }

    // 3. Atualização Específica (Peso) - Usado no VisualizarAluno.js
    @PutMapping("/atualizar-peso/{id}")
    public ResponseEntity<Void> atualizarPeso(@PathVariable Long id, @RequestBody AtualizarPesoRequestDTO dto) {
        alunoService.atualizarPeso(id, dto.getPeso());
        return ResponseEntity.ok().build();
    }

    // 4. Atualização Específica (Altura) - Usado no VisualizarAluno.js
    @PutMapping("/atualizar-altura/{id}")
    public ResponseEntity<Void> atualizarAltura(@PathVariable Long id, @RequestBody AtualizarAlturaRequestDTO dto) {
        alunoService.atualizarAltura(id, dto.getAltura());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    public ResponseEntity<AlunoResponseDTO> atualizarMe(@RequestBody AlunoUpdateDTO dto) {
        try {
            // 1. Descobre quem é o usuário logado pelo Token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String cpf = authentication.getName();

            // 2. Usa o serviço de atualização que já criamos (que trata endereço, nulos, etc.)
            Aluno alunoAtualizado = alunoService.atualizarPerfil(cpf, dto);

            // 3. Retorna os dados atualizados no formato correto
            return ResponseEntity.ok(new AlunoResponseDTO(alunoAtualizado));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/listar-web")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunosWeb() {
        List<Aluno> alunos = alunoService.listarTodos();
        List<AlunoResponseDTO> dtos = alunos.stream()
                .map(AlunoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/salvar-completo")
    public ResponseEntity<Aluno> salvarAlunoCompleto(@RequestBody AlunoCompletoDTO dto) {
        return ResponseEntity.ok(alunoService.salvarCompleto(dto, null));
    }

    @PutMapping("/atualizar-completo/{id}")
    public ResponseEntity<Aluno> atualizarAlunoCompleto(@PathVariable Long id, @RequestBody AlunoCompletoDTO dto) {
        return ResponseEntity.ok(alunoService.salvarCompleto(dto, id));
    }


}
