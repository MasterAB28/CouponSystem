package com.example.CouponSystem.login;

import com.example.CouponSystem.facade.AdminFacade;
import com.example.CouponSystem.facade.ClientFacade;
import com.example.CouponSystem.facade.CompanyFacade;
import com.example.CouponSystem.facade.CustomerFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
public class LoginManager {
    private ApplicationContext ctx;

    public LoginManager(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * The method receives client's email and password and type and checks if the email and password are correct,
     * by method 'facade.login' from the facades
     * @param email client's email
     * @param password client's password
     * @param clientType client's type
     * @return ClientFacade
     */
    public ClientFacade login(String email, String password, ClientType clientType) {
        switch (clientType){
            case Administrator:
                AdminFacade adminFacade = ctx.getBean(AdminFacade.class);
                if (adminFacade.login(email,password))
                    return adminFacade;
                break;
            case Company:
                CompanyFacade companyFacade =ctx.getBean(CompanyFacade.class);
                if (companyFacade.login(email,password))
                    return companyFacade;
                break;
            case Customer:
                CustomerFacade customerFacade = ctx.getBean(CustomerFacade.class);
                if (customerFacade.login(email,password))
                    return customerFacade;
                break;
        }
        return null;
    }
}
