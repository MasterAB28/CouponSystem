package com.example.CouponSystem.Threads;

import com.example.CouponSystem.CouponSystemApplication;
import com.example.CouponSystem.login.LoginParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.Token;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokensThread implements Runnable {
    @Autowired
    private HashMap<String,LoginParameters> sessions;
    @Override
    public void run() {
        while (true) {
            try {
                checkTokens();
                Thread.sleep(1000*60*30);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void checkTokens(){
        Calendar calendar=Calendar.getInstance();
        for (Map.Entry<String,LoginParameters> s : sessions.entrySet()) {
            if (calendar.getTimeInMillis() - s.getValue().getCalendar().getTimeInMillis()>=(1000*60*30)){
                sessions.remove(s.getKey());
            }
        }
    }
}
