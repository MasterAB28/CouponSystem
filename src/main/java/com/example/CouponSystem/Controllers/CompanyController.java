package com.example.CouponSystem.Controllers;

import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.facade.CompanyFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class CompanyController {
    private CompanyFacade companyFacade;

    public CompanyController(CompanyFacade companyFacade) {
        this.companyFacade = companyFacade;
    }

    public void setCompanyFacade(CompanyFacade companyFacade) {
        this.companyFacade = companyFacade;
    }

    @PostMapping("/coupon")
    public ResponseEntity<?>addCoupon(@RequestBody Coupon coupon){
        try {
            companyFacade.addCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/coupon")
    public ResponseEntity<?>updateCoupon(@RequestBody Coupon coupon){
        try {
            companyFacade.updateCoupon(coupon);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/coupon/{couponId}")
    public ResponseEntity<?> deleteCouponById(@PathVariable int couponId){
        try {
            companyFacade.deleteCouponById(couponId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ExceptionCoupons e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/coupons")
    public List<Coupon> getAllCompanyCoupons(){
        return companyFacade.getCompanyCoupons();
    }
    @GetMapping("/coupons/category")
    public List<Coupon>getAllCompanyCoupons(@RequestBody Category category){
        return companyFacade.getCompanyCoupons(category);
    }
    @GetMapping("/coupons/{maxPrice}")
    public List<Coupon>getAllCompanyCoupons(@PathVariable double maxPrice){
        return companyFacade.getCompanyCoupons(maxPrice);
    }
    @GetMapping()
    public ResponseEntity<?>getCompanyDetails(){
        try {
            return ResponseEntity.ok().body(companyFacade.getCompanyDetails());
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
