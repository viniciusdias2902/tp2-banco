package br.uespi.viniciusdias.banco.infrastructure.repository;

import br.uespi.viniciusdias.banco.infrastructure.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
