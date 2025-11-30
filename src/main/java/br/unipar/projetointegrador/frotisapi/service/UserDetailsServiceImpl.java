package br.unipar.projetointegrador.frotisapi.service;

import br.unipar.projetointegrador.frotisapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usa o método inteligente que aceita CPF ou Email
        return usuarioRepository.findByLoginOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com CPF ou Email: " + username));
    }
}