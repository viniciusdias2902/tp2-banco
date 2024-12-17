package br.uespi.viniciusdias.banco.Scheduling;

import br.uespi.viniciusdias.banco.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class EmprestimoScheduler {

    @Autowired
    private EmprestimoService emprestimoService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void cobrarEmprestimosMensalmente() {
        emprestimoService.cobrarEmprestimosMensais();
    }
}
