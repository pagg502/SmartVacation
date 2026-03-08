package com.backend.ecommercespringbootbackend.services;


public interface CheckoutInterface {
    PurchaseResponse placeOrder(Purchase purchase);
}
