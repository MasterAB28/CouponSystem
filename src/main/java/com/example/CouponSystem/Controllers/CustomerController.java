package com.example.CouponSystem.Controllers;

import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.facade.CustomerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
    private CustomerFacade customerFacade;

    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    public void setCustomerFacade(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @PostMapping("/coupon")
    public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon ){
        try {
            customerFacade.purchaseCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/coupon")
    public ResponseEntity<?>deletePurchaseCoupon(@RequestBody Coupon coupon){
        try {
            customerFacade.deletePurchaseCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon")
    public ResponseEntity<?>getCustomerCoupons(){
        try {
            return ResponseEntity.ok().body(customerFacade.getCustomerCoupons());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon/category")
    public ResponseEntity<?>getCustomerCouponsByCategory(@RequestBody Category category){
        try {
            return ResponseEntity.ok().body(customerFacade.getCustomerCoupons(category));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon/{maxPrice}")
    public ResponseEntity<?>getCustomerCouponsByCategory(@PathVariable double maxPrice){
        try {
            return ResponseEntity.ok().body(customerFacade.getCustomerCoupons(maxPrice));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/coupons")
    public List<Coupon> getAllCoupons(){
        return customerFacade.getAllCoupons();
    }
    @GetMapping
    public ResponseEntity<?> getCustomerDetails(){
        try {
            return ResponseEntity.ok().body(customerFacade.getCustomerDetails());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

