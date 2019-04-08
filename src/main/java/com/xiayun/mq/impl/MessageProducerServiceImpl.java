package com.xiayun.mq.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

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

    @Resource
    private Topic msgTopic;

    @Override
    public void sendMessage(String msg) throws JMSException {

        this.jmsMessagingTemplate.convertAndSend(this.msgQueue, msg);
        this.jmsMessagingTemplate.convertAndSend(this.msgTopic, msg);
    }

    @Override
    public void sendObject(Map map) {
        this.jmsMessagingTemplate.convertAndSend(this.objQueue, map);
    }

}