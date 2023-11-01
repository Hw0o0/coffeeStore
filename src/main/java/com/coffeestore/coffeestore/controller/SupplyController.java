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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/supply")
@RequiredArgsConstructor
public class SupplyController {
    private final SupplyService supplyService;
    private final SupplyDetailsService supplyDetailsService;
    @GetMapping
    public String supplyPage(Model model,@RequestParam("supplyId")Long supplyId){
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findByAll()
                .stream()
                .filter(supplyDetails -> supplyDetails.getSupply().getId().equals(supplyId))
                .collect(Collectors.toList());
        int totalPrice = supplyDetailsList.stream()
                .mapToInt(supplyDetails -> supplyDetails.getPrice()*supplyDetails.getSupplyAmount())
                .sum();
        Supply supply = supplyService.findBySupply(supplyId);
        supply.setTotalPrice(totalPrice);
        supplyService.save(supply);
        model.addAttribute(supply).addAttribute(supplyDetailsList);
        return "supplyPage";
    }
    @GetMapping("/supplierNameSearch")
    public String supplierNameSearchView(Model model,@RequestParam("supplierName")String supplierName){
            List<Supply> supply = new ArrayList<>();
            supply.add(supplyService.findByName(supplierName));
            model.addAttribute("supplyList",supply);
            return "/management/supplyManagement";
        }
    @GetMapping("/ingredientSupply")
    public String supplyOkView(Model model,@RequestParam("supplyId")Long supplyId){
        Supply supply = supplyService.findBySupply(supplyId);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supply.getSupplier().getId());
        supplyService.supplyOk(supply,supplyDetailsList);
        List<Supply> supplyList = supplyService.findByAll();
        model.addAttribute(supplyList);
        return "/management/supplyManagement";
    }
}
