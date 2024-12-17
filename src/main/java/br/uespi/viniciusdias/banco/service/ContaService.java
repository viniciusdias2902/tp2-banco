package br.uespi.viniciusdias.banco.service;

import br.uespi.viniciusdias.banco.infrastructure.entity.Agencia;
import br.uespi.viniciusdias.banco.infrastructure.entity.Conta;
import br.uespi.viniciusdias.banco.infrastructure.entity.Transacao;
import br.uespi.viniciusdias.banco.infrastructure.entity.Usuario;
import br.uespi.viniciusdias.banco.infrastructure.repository.AgenciaRepository;
import br.uespi.viniciusdias.banco.infrastructure.repository.ContaRepository;
import br.uespi.viniciusdias.banco.infrastructure.repository.TransacaoRepository;
import br.uespi.viniciusdias.banco.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public Conta criarConta(Long agenciaId, Long usuarioId, BigDecimal saldoInicial) {
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("O saldo inicial não pode ser negativo.");
        }

        Agencia agencia = agenciaRepository.findById(agenciaId)
                .orElseThrow(() -> new RuntimeException("Agência não encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String numeroConta = "CONTA-" + System.currentTimeMillis();  // Exemplo simples de número de conta

        Conta novaConta = new Conta();
        novaConta.setNumeroConta(numeroConta);
        novaConta.setSaldo(saldoInicial);
        novaConta.setAgencia(agencia);
        novaConta.setUsuario(usuario);

        return contaRepository.save(novaConta);
    }

    @Transactional
    public Conta depositar(Long contaId, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do depósito deve ser maior que zero.");
        }

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        conta.setSaldo(conta.getSaldo().add(valor)); // Atualizando saldo com o valor depositado
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta sacar(Long contaId, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do saque deve ser maior que zero.");
        }

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor)); // Atualizando saldo com o valor sacado
        return contaRepository.save(conta);
    }

    @Transactional
    public void transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, String descricao) {
        Conta contaOrigem = buscarContaPorId(contaOrigemId);
        Conta contaDestino = buscarContaPorId(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        // Atualiza os saldos das contas
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Cria a transação para a conta de origem
        Transacao transacaoOrigem = new Transacao();
        transacaoOrigem.setDescricao("Transferência enviada: " + descricao);
        transacaoOrigem.setValor(valor.negate());
        transacaoOrigem.setConta(contaOrigem);
        transacaoRepository.save(transacaoOrigem);

        // Cria a transação para a conta de destino
        Transacao transacaoDestino = new Transacao();
        transacaoDestino.setDescricao("Transferência recebida: " + descricao);
        transacaoDestino.setValor(valor);
        transacaoDestino.setConta(contaDestino);
        transacaoRepository.save(transacaoDestino);
    }

    @Transactional
    public void cancelarConta(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));


        if (conta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Não é possível cancelar a conta com saldo positivo.");
        }

        contaRepository.delete(conta);
    }

    public List<Conta> listarContas() {
        return contaRepository.findAll();
    }

    public Conta buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada com ID: " + id));
    }
}
