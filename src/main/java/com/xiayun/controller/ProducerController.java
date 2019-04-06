package com.xiayun.controller;

import com.xiayun.mq.MessageProducerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProducerController {

    @Resource
    private MessageProducerService messageProducer;

    @RequestMapping("/sendQueueMsg")
    public void sendQueueMsg() throws JMSException {
        messageProducer.sendMessage("hello -activeMq");
    }

    @RequestMapping("/sendQueueObj")
    public void sendQueueObj() {
        Map<String ,String> map = new HashMap<>();
        map.put("data","data");
        map.put("response","response");
        messageProducer.sendObject(map);
    }
}
