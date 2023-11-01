package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.entity.Ingredient;
import com.coffeestore.coffeestore.entity.Supplier;
import com.coffeestore.coffeestore.entity.SupplyDetails;
import com.coffeestore.coffeestore.service.IngredientService;
import com.coffeestore.coffeestore.service.SupplierService;
import com.coffeestore.coffeestore.service.SupplyDetailsService;
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

    @GetMapping
    public  String supplyDetailView(Model model, @RequestParam("supplierId")Long supplierId){
        List<Ingredient> ingredientList = ingredientService.findByAll();
        Supplier supplier = supplierService.findBySupplier(supplierId);
        model.addAttribute(supplier).addAttribute(ingredientList);
        return "management/supplyDetailsManagement";
    }
    @PostMapping("/supplyIngredient")
    public String addSupplyToList(@RequestParam("supplierId") Long supplierId, @RequestParam("supplyIngredientId") Long supplyIngredientId,int price, int supplyAmount, HttpServletRequest request){
        supplyDetailsService.createBySupplyDetails(supplierId,supplyIngredientId,price,supplyAmount);
        String referer = request.getHeader("Referer");
        return "redirect:"+referer;
    }
    @GetMapping("/supplyDetailList")
    public String supplyDetailCartView(Model model,@RequestParam("supplierId")Long supplierId){
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplierId).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }
    @PatchMapping("/updateSupplyAmount")
    public String updateSupplyAmount(Model model,@RequestParam("supplierId")Long supplierId,@RequestParam("supplyDetailId")Long supplyDetailId,int updateAmount){
        supplyDetailsService.updateBySupplyDetailsAmount(supplyDetailId,updateAmount);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplierId).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }
    @DeleteMapping("/deleteSupplyDetail")
    public String deleteSupplyDetail(Model model,@RequestParam("supplierId")Long supplierId,@RequestParam("supplyDetailId")Long supplyDetailId){
        supplyDetailsService.deleteBySupplyDetails(supplyDetailId);
        List<SupplyDetails> supplyDetailsList = supplyDetailsService.findBySupplyDetailAll(supplierId);
        model.addAttribute(supplierId).addAttribute(supplyDetailsList);
        return "supplyDetailList";
    }
}
