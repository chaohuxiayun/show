package com.xiayun.mq;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MessageConsumerService {

    void receiveMessage(String text);

    void receiveMessage(Map map);
}