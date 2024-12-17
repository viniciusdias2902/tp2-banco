package br.uespi.viniciusdias.banco.service;

import br.uespi.viniciusdias.banco.infrastructure.entity.Conta;
import br.uespi.viniciusdias.banco.infrastructure.entity.Emprestimo;
import br.uespi.viniciusdias.banco.infrastructure.repository.ContaRepository;
import br.uespi.viniciusdias.banco.infrastructure.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ContaRepository contaRepository;

    public void cobrarEmprestimosMensais() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();

        for (Emprestimo emprestimo : emprestimos) {
            if (!emprestimo.isQuitado()) {
                Conta conta = emprestimo.getConta();

                BigDecimal valorMensal = emprestimo.getValor()
                        .multiply(emprestimo.getTaxaJuros())
                        .divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);

                if (conta.getSaldo().compareTo(valorMensal) >= 0) {
                    conta.setSaldo(conta.getSaldo().subtract(valorMensal));
                    emprestimo.setValorPago(emprestimo.getValorPago().add(valorMensal));

                    if (emprestimo.getValorPago().compareTo(emprestimo.getValor()) >= 0) {
                        emprestimo.setQuitado(true);
                    }

                    contaRepository.save(conta);
                    emprestimoRepository.save(emprestimo);
                } else {
                    System.out.println("Conta " + conta.getId() + " não tem saldo suficiente para a cobrança.");
                }
            }
        }
    }
}
