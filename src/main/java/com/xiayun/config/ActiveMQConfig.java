package com.xiayun.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Bean
    public Queue msgQueue() {
        return new ActiveMQQueue("xiayun.msg.queue") ;
    }

    @Bean
    public Queue objQueue() {
        return new ActiveMQQueue("xiayun.obj.queue") ;
    }

}