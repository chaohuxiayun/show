package com.xiayun.mq.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Queue;

import com.xiayun.mq.MessageProducerService;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageProducerServiceImpl implements MessageProducerService {

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private Queue msgQueue;

    @Resource
    private Queue objQueue;

    @Override
    public void sendMessage(String msg) throws JMSException {
        System.out.println(objQueue.getQueueName());
        System.out.println(msgQueue.getQueueName());
        this.jmsMessagingTemplate.convertAndSend(this.msgQueue, msg);
    }

    @Override
    public void sendObject(Map map) {
        this.jmsMessagingTemplate.convertAndSend(this.objQueue, map);
    }

}