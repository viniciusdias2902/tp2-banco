package br.uespi.viniciusdias.banco.Controller;

import br.uespi.viniciusdias.banco.infrastructure.entity.Agencia;
import br.uespi.viniciusdias.banco.service.AgenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    @Autowired
    private AgenciaService agenciaService;

    @PostMapping("/criar")
    public ResponseEntity<Agencia> criarAgencia(@RequestParam String nome, @RequestParam String numero) {
        Agencia novaAgencia = agenciaService.criarAgencia(nome, numero);
        return ResponseEntity.ok(novaAgencia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agencia> buscarAgencia(@PathVariable Long id) {
        Agencia agencia = agenciaService.buscarAgencia(id);
        return ResponseEntity.ok(agencia);
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<String> excluirAgencia(@PathVariable Long id) {
        agenciaService.excluirAgencia(id);
        return ResponseEntity.ok("Agência excluída com sucesso.");
    }
}
