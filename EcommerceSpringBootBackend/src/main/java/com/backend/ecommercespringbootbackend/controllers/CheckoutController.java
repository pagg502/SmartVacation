package com.backend.ecommercespringbootbackend.controllers;

import com.backend.ecommercespringbootbackend.services.CheckoutService;
import com.backend.ecommercespringbootbackend.services.Purchase;
import com.backend.ecommercespringbootbackend.services.PurchaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {
        return checkoutService.placeOrder(purchase);
    }
}
