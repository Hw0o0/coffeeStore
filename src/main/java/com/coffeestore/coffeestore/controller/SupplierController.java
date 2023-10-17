package com.coffeestore.coffeestore.controller;

import com.coffeestore.coffeestore.dto.supplier.SupplierRegistrationRequestDto;
import com.coffeestore.coffeestore.dto.supplier.SupplierUpdateRequestDto;
import com.coffeestore.coffeestore.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    @GetMapping
    public String supplierManagement(Model model){
        model.addAttribute("suppliers", supplierService.findByAll());
        return "/management/supplierManagement";
    }

    @PostMapping("/registration")
    public String registration(SupplierRegistrationRequestDto supplierRegistrationRequestDto){
        supplierService.createBySupplier(supplierRegistrationRequestDto);
        return "redirect:/supplier";
    }

    @PatchMapping("/state")
    public String supplierState(@RequestParam("supplierId") Long supplierId){
        supplierService.stateChangeBySupplier(supplierId);
        return"redirect:/supplier";
    }

    @PatchMapping("/update")
    public String supplierUpdate(@RequestParam("supplierId") Long supplierId, SupplierUpdateRequestDto supplierUpdateRequestDto){
        supplierService.updateBySupplier(supplierId,supplierUpdateRequestDto);
        return "redirect:/supplier";
    }

    @DeleteMapping("/delete")
    public String supplierDelete(@RequestParam("supplierId") Long supplierId){
        supplierService.deleteBySupplier(supplierId);
        return "redirect:/supplier";
    }
}
