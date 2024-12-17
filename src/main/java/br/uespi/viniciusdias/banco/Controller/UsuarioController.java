package br.uespi.viniciusdias.banco.Controller;

import br.uespi.viniciusdias.banco.infrastructure.entity.Usuario;
import br.uespi.viniciusdias.banco.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Usuario salvarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.salvarUsuario(usuario);
    }

    @GetMapping("/{cpf}")
    public Optional<Usuario> buscarUsuario(@PathVariable String cpf) {
        return usuarioService.buscarPorCpf(cpf);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }
}
