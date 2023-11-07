package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.supplyDetails.SupplyDetailsRegistrationRequestDto;
import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Supplier;
import com.coffeestore.coffeestore.entity.Supply;
import com.coffeestore.coffeestore.entity.SupplyDetails;
import com.coffeestore.coffeestore.service.IngredientService;
import com.coffeestore.coffeestore.service.SupplierService;
import com.coffeestore.coffeestore.service.SupplyDetailsService;
import com.coffeestore.coffeestore.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/supplyDetails")
public class supplyDetailsController {
    private final SupplyDetailsService supplyDetailsService;
    private final SupplierService supplierService;
    private final IngredientService ingredientService;
    private final SupplyService supplyService;

    @GetMapping
    public String supplyDetailView(Model model, @RequestParam("supplierId") Long supplierId) {
        List<Ingredient> ingredientList = ingredientService.findByAll();
        Supplier supplier = supplierService.findBySupplier(supplierId);
        model.addAttribute(supplier).addAttribute(ingredientList);
        return "/management/supplyCartManagement";
    }

    @GetMapping("/supplyDetailManagement")
    public String supplyDetailManagement(Model model) {
        List<Supply> supplyList = supplyService.findBySupplyAll();
        model.addAttribute(supplyList);
        return "/management/supplyManagement";
    }

    @GetMapping("/supplyDetailList")
    public String supplyDetailCartView(Model model, @RequestParam("supplierId") Long supplierId) {
        Supplier supplier = supplierService.findBySupplier(supplierId);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplier).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }

    @PostMapping("/supplyIngredient")
    public String addSupplyToList(@RequestParam("supplierId") Long supplierId, @RequestParam("supplyIngredientId") Long supplyIngredientId, SupplyDetailsRegistrationRequestDto supplyDetailsRegistrationRequestDto, HttpServletRequest request) {
        supplyDetailsService.createBySupplyDetails(supplierId, supplyIngredientId, supplyDetailsRegistrationRequestDto);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PatchMapping("/updateSupplyAmount")
    public String updateSupplyAmount(Model model, @RequestParam("supplierId") Long supplierId, @RequestParam("supplyDetailId") Long supplyDetailId, int updateAmount) {
        Supplier supplier = supplierService.findBySupplier(supplierId);
        supplyDetailsService.updateBySupplyDetailsAmount(supplyDetailId, updateAmount);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplier).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }

    @DeleteMapping("/deleteSupplyDetail")
    public String deleteSupplyDetail(Model model, @RequestParam("supplierId") Long supplierId, @RequestParam("supplyDetailId") Long supplyDetailId) {
        Supplier supplier = supplierService.findBySupplier(supplierId);
        supplyDetailsService.deleteBySupplyDetails(supplyDetailId);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplier).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }
}
