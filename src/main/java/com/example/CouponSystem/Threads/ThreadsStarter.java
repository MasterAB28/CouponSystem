package com.example.CouponSystem.Threads;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ThreadsStarter {
    private ApplicationContext ctx;

    public ThreadsStarter(ApplicationContext ctx) {
        this.ctx = ctx;
    }
    public void ThreadsStarter(){
        CouponExpirationDailyJob job = ctx.getBean(CouponExpirationDailyJob.class);
        Thread t1 = new Thread(job);
        TokensThread tokensThread = ctx.getBean(TokensThread.class);
        Thread t2 = new Thread(tokensThread);
        t1.start();
        t2.start();
    }
}
