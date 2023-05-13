package com.example.CouponSystem.login;

import com.auth0.jwt.JWT;
import com.example.CouponSystem.facade.ClientFacade;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class LoginParameters {
    private Calendar calendar;
    private ClientFacade clientFacade;

    public LoginParameters( ClientFacade clientFacade) {
        this.calendar = Calendar.getInstance();
        this.clientFacade = clientFacade;
    }

    public LoginParameters() {
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public ClientFacade getClientFacade() {
        return clientFacade;
    }
}
