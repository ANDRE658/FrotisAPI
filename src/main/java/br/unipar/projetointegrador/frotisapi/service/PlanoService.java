package br.unipar.projetointegrador.frotisapi.service;

import br.unipar.projetointegrador.frotisapi.model.Plano;
import br.unipar.projetointegrador.frotisapi.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {
    private PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    public Plano salvar(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<Plano> listarTodos() {
        return planoRepository.findAll();
    }

    public Plano buscarPorId(Long id) {
        return planoRepository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        planoRepository.deleteById(id);
    }

    public Plano atualizar(Plano plano) {
        if (plano.getId() == null || !planoRepository.existsById(plano.getId())) {
            throw new IllegalArgumentException("Plano com ID inválido ou não existe.");
        }
        return planoRepository.save(plano);
    }
}
