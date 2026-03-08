package com.backend.ecommercespringbootbackend.services;

import com.backend.ecommercespringbootbackend.dao.CartRepository;
import com.backend.ecommercespringbootbackend.dao.CustomerRepository;
import com.backend.ecommercespringbootbackend.entities.Cart;
import com.backend.ecommercespringbootbackend.entities.CartItem;
import com.backend.ecommercespringbootbackend.entities.Customer;
import com.backend.ecommercespringbootbackend.entities.StatusType;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutService implements CheckoutInterface {
    private CustomerRepository customerRepository;
    private CartRepository cartRepository;

    public CheckoutService(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
    }
    @Override
    public PurchaseResponse placeOrder(Purchase purchase) {
        Cart cart = purchase.getCart();

        //Set cart.id to null so JPA can trigger an insert and eliminates the error id 0 not found
        if (cart.getId() != null && cart.getId() == 0) {
            cart.setId(null);
        }
        
        String orderTrackingNumber = generateTrackingNumber();
        cart.setOrderTrackingNumber(orderTrackingNumber);

        //Update Cart Status from Pending to Ordered
        cart.setStatus(StatusType.ordered);

        Set<CartItem> cartItems = purchase.getCartItems();
        if (cartItems.isEmpty()) {
            return new PurchaseResponse("Cart is empty");
        }
        cartItems.forEach(item -> cart.add(item));

        //Load customer
        String customerEmail = purchase.getCustomer().getEmail();

        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        //Add cart
        customer.add(cart);

        //Save cart without overriding customer
        cartRepository.save(cart);

        //customerRepository.save(customer);
        System.out.println("Checked out completed");
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
