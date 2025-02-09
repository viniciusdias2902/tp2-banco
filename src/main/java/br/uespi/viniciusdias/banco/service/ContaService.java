package br.uespi.viniciusdias.banco.service;

import br.uespi.viniciusdias.banco.infrastructure.entity.*;
import br.uespi.viniciusdias.banco.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private EmprestimoRepository emprestimoRepository;

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

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacaoOrigem = new Transacao();
        transacaoOrigem.setDescricao("Transferência enviada: " + descricao);
        transacaoOrigem.setValor(valor.negate());
        transacaoOrigem.setConta(contaOrigem);
        transacaoRepository.save(transacaoOrigem);

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

    @Transactional
    public void pagarEmprestimo(Long contaId, Long emprestimoId, BigDecimal valorPago) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (!conta.getEmprestimos().contains(emprestimo)) {
            throw new RuntimeException("Empréstimo não pertence a esta conta");
        }

        if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor pago deve ser positivo");
        }

        BigDecimal valorRestante = emprestimo.getValor().subtract(emprestimo.getValorPago());
        if (valorPago.compareTo(valorRestante) > 0) {
            throw new RuntimeException("O valor pago excede o valor restante do empréstimo");
        }

        emprestimo.setValorPago(emprestimo.getValorPago().add(valorPago));
        emprestimoRepository.save(emprestimo);

        if (conta.getSaldo().compareTo(valorPago) < 0) {
            throw new RuntimeException("Saldo insuficiente para pagar o empréstimo");
        }
        conta.setSaldo(conta.getSaldo().subtract(valorPago));
        contaRepository.save(conta);
    }

    public Emprestimo pedirEmprestimo(Long contaId, BigDecimal valor, BigDecimal taxaJuros) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setValor(valor);
        novoEmprestimo.setValorPago(BigDecimal.ZERO);
        novoEmprestimo.setTaxaJuros(taxaJuros);
        novoEmprestimo.setData(LocalDate.now());
        novoEmprestimo.setConta(conta);

        emprestimoRepository.save(novoEmprestimo);

        return novoEmprestimo;
    }

    public List<Emprestimo> listarEmprestimos(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        return conta.getEmprestimos();
    }
}
