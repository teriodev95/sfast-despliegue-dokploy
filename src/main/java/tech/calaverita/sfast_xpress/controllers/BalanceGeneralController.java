package tech.calaverita.sfast_xpress.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.DTOs.balance_general.BalanceGeneralDto;
import tech.calaverita.sfast_xpress.services.BalanceGeneralService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/balances-generales")
public class BalanceGeneralController {
    private final BalanceGeneralService balanceGeneralService;

    public BalanceGeneralController(BalanceGeneralService balanceGeneralService) {
        this.balanceGeneralService = balanceGeneralService;
    }

    @GetMapping(path = "/gerencia/{gerencia}")
    public BalanceGeneralDto getBalanceGeneralByGerencia(@PathVariable String gerencia) {
        return this.balanceGeneralService.getBalanceGeneralByGerencia(gerencia);
    }
}
