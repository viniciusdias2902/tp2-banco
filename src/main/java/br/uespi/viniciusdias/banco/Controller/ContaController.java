package br.uespi.viniciusdias.banco.Controller;

import br.uespi.viniciusdias.banco.infrastructure.entity.Conta;
import br.uespi.viniciusdias.banco.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @PostMapping("/{contaId}/depositar")
    public ResponseEntity<Conta> depositar(@PathVariable Long contaId, @RequestParam BigDecimal valor) {
        Conta contaAtualizada = contaService.depositar(contaId, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/{contaId}/sacar")
    public ResponseEntity<Conta> sacar(@PathVariable Long contaId, @RequestParam BigDecimal valor) {
        Conta contaAtualizada = contaService.sacar(contaId, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestParam Long contaOrigemId,
                                             @RequestParam Long contaDestinoId,
                                             @RequestParam BigDecimal valor) {
        contaService.transferir(contaOrigemId, contaDestinoId, valor);
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @DeleteMapping("/{contaId}/cancelar")
    public ResponseEntity<String> cancelarConta(@PathVariable Long contaId) {
        contaService.cancelarConta(contaId);
        return ResponseEntity.ok("Conta cancelada com sucesso");
    }
}
