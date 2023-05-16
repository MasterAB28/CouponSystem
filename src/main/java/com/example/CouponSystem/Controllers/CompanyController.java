package com.example.CouponSystem.Controllers;

import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.facade.CompanyFacade;
import com.example.CouponSystem.facade.CustomerFacade;
import com.example.CouponSystem.login.LoginParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HashMap<String, LoginParameters> sessions;

    private CompanyFacade getCompanyFacade(){
        String token=request.getHeader("authorization");
        token = token.replace("Bearer ","");
        return (CompanyFacade) sessions.get(token).getClientFacade();
    }

    @PostMapping("/coupon")
    public ResponseEntity<?>addCoupon(@RequestBody Coupon coupon){
        try {
            getCompanyFacade().addCoupon(coupon);
            return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/coupon")
    public ResponseEntity<?>updateCoupon(@RequestBody Coupon coupon){
        try {
            getCompanyFacade().updateCoupon(coupon);
            return ResponseEntity.status(HttpStatus.OK).body(coupon);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/coupon/{couponId}")
    public ResponseEntity<?> deleteCouponById(@PathVariable int couponId){
        try {
            getCompanyFacade().deleteCouponById(couponId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ExceptionCoupons e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupon")
    public List<Coupon> getAllCompanyCoupons(){
        return getCompanyFacade().getCompanyCoupons();
    }
    @GetMapping("/coupon/category")
    public List<Coupon>getAllCompanyCoupons(@RequestBody Category category){
        return getCompanyFacade().getCompanyCoupons(category);
    }
    @GetMapping("/coupon/{maxPrice}")
    public List<Coupon>getAllCompanyCoupons(@PathVariable double maxPrice){
        return getCompanyFacade().getCompanyCoupons(maxPrice);
    }
    @GetMapping("/details")
    public ResponseEntity<?>getCompanyDetails(){
        try {
            return ResponseEntity.ok().body(getCompanyFacade().getCompanyDetails());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
