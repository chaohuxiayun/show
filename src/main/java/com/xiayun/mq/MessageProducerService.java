package com.xiayun.mq;

import javax.jms.JMSException;
import java.util.Map;

public interface MessageProducerService {
    void sendMessage(String msg) throws JMSException;
    void sendObject(Map map);
}