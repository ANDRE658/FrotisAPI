package br.unipar.projetointegrador.frotisapi.service;

import br.unipar.projetointegrador.frotisapi.model.Instrutor;
import br.unipar.projetointegrador.frotisapi.repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrutorService {

    private final InstrutorRepository instrutorRepository;

    @Autowired
    public InstrutorService(InstrutorRepository instrutorRepository) {
        this.instrutorRepository = instrutorRepository;
    }

    public List<Instrutor> listarTodos() {
        return instrutorRepository.findAll();
    }

    public Instrutor salvar(Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    public Instrutor buscarPorId(Long id) {
        return instrutorRepository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        instrutorRepository.deleteById(id);
    }

    public Instrutor atualizar(Long id, Instrutor instrutorAtualizado) {
        Instrutor instrutorExistente = buscarPorId(id);

        if (instrutorExistente != null) {
            // Atualiza dados básicos
            instrutorExistente.setNome(instrutorAtualizado.getNome());
            instrutorExistente.setEmail(instrutorAtualizado.getEmail());
            instrutorExistente.setTelefone(instrutorAtualizado.getTelefone());
            instrutorExistente.setSexo(instrutorAtualizado.getSexo());
            instrutorExistente.setDataNascimento(instrutorAtualizado.getDataNascimento());
            // CPF geralmente não se muda, mas se quiser permitir, descomente:
            // instrutorExistente.setCPF(instrutorAtualizado.getCPF());

            // Atualiza Endereço
            if (instrutorExistente.getEndereco() != null && instrutorAtualizado.getEndereco() != null) {
                br.unipar.projetointegrador.frotisapi.model.Endereco endExistente = instrutorExistente.getEndereco();
                br.unipar.projetointegrador.frotisapi.model.Endereco endNovo = instrutorAtualizado.getEndereco();

                endExistente.setRua(endNovo.getRua());
                endExistente.setNumero(endNovo.getNumero());
                endExistente.setBairro(endNovo.getBairro());
                endExistente.setCidade(endNovo.getCidade());
                endExistente.setEstado(endNovo.getEstado());
                endExistente.setCep(endNovo.getCep());
                // O JPA salvará o endereço automaticamente por causa do CascadeType.ALL
            } else if (instrutorAtualizado.getEndereco() != null) {
                // Se não tinha endereço e agora tem
                instrutorExistente.setEndereco(instrutorAtualizado.getEndereco());
            }

            return instrutorRepository.save(instrutorExistente);
        }
        return null;
    }
}