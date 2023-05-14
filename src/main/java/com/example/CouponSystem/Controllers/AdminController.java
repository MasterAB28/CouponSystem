package com.example.CouponSystem.Controllers;

import com.example.CouponSystem.beans.Company;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.facade.AdminFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
    private AdminFacade adminFacade;

    public AdminController(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }


    @PostMapping("/company")
    public ResponseEntity<?> addCompany(@RequestBody Company company){
        try{
            adminFacade.addCompany(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(company);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/company")
    public ResponseEntity<?>updateCompany(@RequestBody Company company){
        try {
            adminFacade.updateCompany(company);
            return ResponseEntity.status(HttpStatus.OK).body(company);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<?>deleteCompany(@PathVariable int companyId){
        try {
            adminFacade.deleteCompany(companyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/companies")
    public List<Company> getAllCompanies(){
        return adminFacade.getAllCompanies();
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity <?> getOneCompany(@PathVariable int companyId){
        try {
            return ResponseEntity.ok().body(adminFacade.getOneCompany(companyId));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/customer")
    public ResponseEntity<?>addCustomer(@RequestBody Customer customer){
        try {
            adminFacade.addCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/customer")
    public ResponseEntity<?>updateCustomer(@RequestBody Customer customer){
        try {
            adminFacade.updateCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK).body(customer);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?>deleteCustomer(@PathVariable int customerId){
        try {
            adminFacade.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/customers")
    public List<Customer>getAllCustomers(){
        return adminFacade.getAllCustomers();
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?>getOneCustomer(@PathVariable int customerId){
        try {
            return ResponseEntity.ok().body(adminFacade.getOneCustomer(customerId));
        } catch (ExceptionCoupons e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
