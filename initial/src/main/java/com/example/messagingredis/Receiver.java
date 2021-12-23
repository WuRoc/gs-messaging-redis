package com.example.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @ClassName Receiver
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-12-23 10:49
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
public class Receiver {
    public static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message){
        LOGGER.info("Receive <" + message+">");
        counter.incrementAndGet();
    }

    public int getCounter(){
        return counter.get();
    }

}
