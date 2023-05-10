package com.example.CouponSystem.Threads;

import com.example.CouponSystem.CouponSystemApplication;
import com.example.CouponSystem.login.LoginParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TokensThread implements Runnable {
    @Autowired
    private ApplicationContext ctx;

    @Override
    public void run() {
        try{
        checkTokens();
        Thread.sleep(1000*60);
    } catch (InterruptedException ignored) {
        }
    }
    public void checkTokens(){
        CouponSystemApplication spa=ctx.getBean(CouponSystemApplication.class);
        Calendar calendar=Calendar.getInstance();
        for (LoginParameters s : spa.sessions().values()) {
            if (calendar.getTimeInMillis() - s.getDate().getTimeInMillis()<=0){
                spa.sessions().remove(s);
            }
        }
    }
}
