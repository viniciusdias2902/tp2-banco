package br.uespi.viniciusdias.banco.infrastructure.repository;

import br.uespi.viniciusdias.banco.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
