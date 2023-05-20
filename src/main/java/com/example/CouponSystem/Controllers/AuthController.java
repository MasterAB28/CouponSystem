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
import com.example.CouponSystem.login.ClientType;
import com.example.CouponSystem.login.LoginManager;
import com.example.CouponSystem.login.LoginParameters;
import com.example.CouponSystem.login.LoginReq;
import com.example.CouponSystem.repository.CompanyRepository;
import com.example.CouponSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("auth")

public class AuthController {
    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private HashMap<String, LoginParameters> sessions;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        LoginManager loginManager = ctx.getBean(LoginManager.class);
        ClientFacade clientFacade = loginManager.login(loginReq.getEmail(), loginReq.getPassword(), loginReq.getClientType());
        if (clientFacade != null) {
            String token = createToken(loginReq);
            sessions.put(token, new LoginParameters(clientFacade));
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email or password incorrect!");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (sessions.get(token.replace("Bearer ", "")) != null){
            sessions.remove(token.replace("Bearer ", ""));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String createToken(LoginReq loginReq) {
        String token = null;
        switch (loginReq.getClientType()) {
            case Administrator:
                token = JWT.create()
                        .withClaim("name", "Admin")
                        .withClaim("clientType", "Administrator")
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;

            case Company:
                Company company = companyRepository.findByEmailAndPassword(loginReq.getEmail(), loginReq.getPassword());

                token = JWT.create()
                        .withClaim("name", company.getName())
                        .withClaim("clientType", "Company")
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;
            case Customer:
                Customer customer = customerRepository.findByEmailAndPassword(loginReq.getEmail(), loginReq.getPassword());

                token = JWT.create()
                        .withClaim("name", customer.getFirstName() + " " + customer.getLastName())
                        .withClaim("clientType", "Customer")
                        .withIssuer("JohnCoupon")
                        .withIssuedAt(new Date())
                        .sign(Algorithm.none());
                break;
        }
        return token;

    }

}
