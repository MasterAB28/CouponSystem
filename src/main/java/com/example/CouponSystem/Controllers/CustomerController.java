package com.example.CouponSystem.Controllers;

import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.facade.ClientFacade;
import com.example.CouponSystem.facade.CustomerFacade;
import com.example.CouponSystem.login.LoginParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HashMap<String, LoginParameters> sessions;


    private CustomerFacade getCustomerFacade(){
        String token=request.getHeader("authorization");
        token = token.replace("Bearer ","");
        return (CustomerFacade) sessions.get(token).getClientFacade();
    }

    @PostMapping("/coupon")
    public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon ){
        try {
            getCustomerFacade().purchaseCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/coupon")
    public ResponseEntity<?>deletePurchaseCoupon(@RequestBody Coupon coupon){
        try {
            getCustomerFacade().deletePurchaseCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon")
    public ResponseEntity<?>getCustomerCoupons(){
        try {
            return ResponseEntity.ok().body(getCustomerFacade().getCustomerCoupons());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon/category")
    public ResponseEntity<?>getCustomerCouponsByCategory(@RequestBody Category category){
        try {
            return ResponseEntity.ok().body(getCustomerFacade().getCustomerCoupons(category));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon/{maxPrice}")
    public ResponseEntity<?>getCustomerCouponsByCategory(@PathVariable double maxPrice){
        try {
            return ResponseEntity.ok().body(getCustomerFacade().getCustomerCoupons(maxPrice));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/coupons")
    public List<Coupon> getAllCoupons(){
        return getCustomerFacade().getAllCoupons();
    }
    @GetMapping("/details")
    public ResponseEntity<?> getCustomerDetails(){
        try {
            return ResponseEntity.ok().body(getCustomerFacade().getCustomerDetails());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

