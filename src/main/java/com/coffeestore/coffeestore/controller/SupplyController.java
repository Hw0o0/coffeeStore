package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Supply;
import com.coffeestore.coffeestore.entity.SupplyDetails;
import com.coffeestore.coffeestore.service.SupplyDetailsService;
import com.coffeestore.coffeestore.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/supply")
@RequiredArgsConstructor
public class SupplyController {
    private final SupplyService supplyService;
    private final SupplyDetailsService supplyDetailsService;

    @GetMapping
    public String supplyPage(Model model, @RequestParam("supplyId") Long supplyId) {
        List<SupplyDetails> supplyDetailsList = supplyService.findBySupplyDetailsList(supplyId);
        int totalPrice = supplyService.createByTotalPrice(supplyDetailsList);
        Supply supply = supplyService.setTotalPrice(supplyId, totalPrice);
        model.addAttribute(supply).addAttribute(supplyDetailsList);
        return "supplyPage";
    }

    @GetMapping("/supplySearch")
    public String supplierNameSearchView(Model model, @RequestParam("supplierName") String supplierName) {
        List<Supply> supply = supplyService.searchBySupplierName(supplierName);
        model.addAttribute("supplyList", supply);
        return "/management/supplyManagement";
    }

    @GetMapping("/ingredientSupply")
    public String supplyOkView(Model model, @RequestParam("supplyId") Long supplyId) {
        Supply supply = supplyService.findBySupply(supplyId);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supply.getSupplier().getId());
        supplyService.supplyOk(supply, supplyDetailsList);
        List<Supply> supplyList = supplyService.findBySupplyAll();
        model.addAttribute(supplyList);
        return "/management/supplyManagement";
    }
}
