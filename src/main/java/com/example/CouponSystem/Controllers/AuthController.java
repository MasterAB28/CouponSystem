package com.example.CouponSystem.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.CouponSystem.CouponSystemApplication;
import com.example.CouponSystem.beans.Company;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.facade.AdminFacade;
import com.example.CouponSystem.facade.ClientFacade;
import com.example.CouponSystem.facade.CompanyFacade;
import com.example.CouponSystem.facade.CustomerFacade;
import com.example.CouponSystem.login.LoginManager;
import com.example.CouponSystem.login.LoginParameters;
import com.example.CouponSystem.login.LoginReq;
import com.example.CouponSystem.repository.CompanyRepository;
import com.example.CouponSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        LoginManager loginManager=ctx.getBean(LoginManager.class);
        CouponSystemApplication spa=ctx.getBean(CouponSystemApplication.class);
        ClientFacade clientFacade = loginManager.login(loginReq.getEmail(), loginReq.getPassword(), loginReq.getClientType());
        switch(loginReq.getClientType()){
            case Administrator:
                AdminController adminController = ctx.getBean(AdminController.class);
                adminController.setAdminFacade((AdminFacade) clientFacade);
                break;
            case Company:
                CompanyController companyController = ctx.getBean(CompanyController.class);
                companyController.setCompanyFacade((CompanyFacade) clientFacade);
                break;
            case Customer:
                CustomerController customerController = ctx.getBean(CustomerController.class);
                customerController.setCustomerFacade((CustomerFacade) clientFacade);
                break;
        }
        if (clientFacade != null) {
            String token=createToken(loginReq);
            spa.sessions().put(token, new LoginParameters(token));
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you are not register!");
    }


    private String createToken(LoginReq loginReq){
        String token=null;
        switch (loginReq.getClientType()){
            case Administrator:
                token = JWT.create()
                        .withClaim("name","Admin")
                        .withClaim("clientType","Administrator")
                        .withClaim("email","admin@gmail.com")
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;

            case Company:
                Company company=companyRepository.findByEmailAndPassword(loginReq.getEmail(),loginReq.getPassword());

                token = JWT.create()
                        .withClaim("name",company.getName())
                        .withClaim("clientType","Company")
                        .withClaim("email",company.getEmail())
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;
            case Customer:
                Customer customer=customerRepository.findByEmailAndPassword(loginReq.getEmail(),loginReq.getPassword());

                token = JWT.create()
                        .withClaim("name",customer.getFirstName()+" "+customer.getLastName())
                        .withClaim("clientType","Customer")
                        .withClaim("email",customer.getEmail())
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;
        }
        return token;

    }

}
