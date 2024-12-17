package br.uespi.viniciusdias.banco.service;

import br.uespi.viniciusdias.banco.infrastructure.entity.Usuario;
import br.uespi.viniciusdias.banco.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
