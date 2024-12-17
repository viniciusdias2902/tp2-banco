package br.uespi.viniciusdias.banco.infrastructure.repository;

import br.uespi.viniciusdias.banco.infrastructure.entity.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
}
