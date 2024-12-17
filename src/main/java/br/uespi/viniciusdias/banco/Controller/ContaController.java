package br.uespi.viniciusdias.banco.Controller;

import br.uespi.viniciusdias.banco.infrastructure.entity.Conta;
import br.uespi.viniciusdias.banco.infrastructure.entity.Emprestimo;
import br.uespi.viniciusdias.banco.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/criar")
    public ResponseEntity<Conta> criarConta(@RequestParam Long agenciaId,
                                            @RequestParam Long usuarioId,
                                            @RequestParam BigDecimal saldoInicial) {
        Conta novaConta = contaService.criarConta(agenciaId, usuarioId, saldoInicial);
        return ResponseEntity.ok(novaConta);
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        List<Conta> contas = contaService.listarContas();
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        Conta conta = contaService.buscarContaPorId(id);
        return ResponseEntity.ok(conta);
    }

    @PutMapping("/{id}/depositar")
    public ResponseEntity<Conta> depositar(@PathVariable Long id, @RequestParam BigDecimal valor) {
        Conta contaAtualizada = contaService.depositar(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PutMapping("/{id}/sacar")
    public ResponseEntity<Conta> sacar(@PathVariable Long id, @RequestParam BigDecimal valor) {
        Conta contaAtualizada = contaService.sacar(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PutMapping("/{id}/transferir")
    public ResponseEntity<String> transferir(
            @PathVariable Long id,
            @RequestParam Long contaDestinoId,
            @RequestParam BigDecimal valor,
            @RequestParam String descricao) {
        contaService.transferir(id, contaDestinoId, valor, descricao);
        return ResponseEntity.ok("Transferência realizada com sucesso!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarConta(@PathVariable Long id) {
        contaService.cancelarConta(id);
        return ResponseEntity.ok("Conta cancelada com sucesso.");
    }

    @PutMapping("/{id}/emprestimos/{emprestimoId}/pagar")
    public ResponseEntity<String> pagarEmprestimo(
            @PathVariable Long id,
            @PathVariable Long emprestimoId,
            @RequestParam BigDecimal valorPago) {

        contaService.pagarEmprestimo(id, emprestimoId, valorPago);
        return ResponseEntity.ok("Pagamento do empréstimo realizado com sucesso.");
    }

    @PostMapping("/{id}/emprestimos/solicitar")
    public ResponseEntity<Emprestimo> pedirEmprestimo(
            @PathVariable Long id,
            @RequestParam BigDecimal valor,
            @RequestParam BigDecimal taxaJuros) {

        Emprestimo emprestimo = contaService.pedirEmprestimo(id, valor, taxaJuros);
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping("/{id}/emprestimos")
    public ResponseEntity<List<Emprestimo>> listarEmprestimos(@PathVariable Long id) {
        List<Emprestimo> emprestimos = contaService.listarEmprestimos(id);
        return ResponseEntity.ok(emprestimos);
    }
}
