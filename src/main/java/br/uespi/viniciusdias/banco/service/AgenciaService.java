package br.uespi.viniciusdias.banco.service;

import br.uespi.viniciusdias.banco.infrastructure.entity.Agencia;
import br.uespi.viniciusdias.banco.infrastructure.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Transactional
    public Agencia criarAgencia(String nome, String numero) {
        Agencia agencia = new Agencia();
        agencia.setNome(nome);
        agencia.setNumero(numero);
        return agenciaRepository.save(agencia);
    }

    public Agencia buscarAgencia(Long id) {
        return agenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agência não encontrada"));
    }

    @Transactional
    public void excluirAgencia(Long id) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agência não encontrada"));

        agenciaRepository.delete(agencia);
    }
}
