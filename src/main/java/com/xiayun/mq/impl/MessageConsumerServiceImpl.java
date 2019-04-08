package com.xiayun.mq.impl;

import com.alibaba.fastjson.JSON;
import com.xiayun.mq.MessageConsumerService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageConsumerServiceImpl implements MessageConsumerService {

    @JmsListener(destination="xiayun.msg.queue")
    public void receiveMessage(String text) {    // 进行消息接收处理
        System.err.println("【*** 接收消息.queue1 ***】" + text);
    }

    @JmsListener(destination="xiayun.obj.queue")
    public void receiveMessage(Map map) {    // 进行消息接收处理
        System.err.println("【*** 接收消息 ***】" + JSON.toJSONString(map));
    }


    @JmsListener(destination="xiayun.msg.topic" ,containerFactory="topicListenerContainer")
    public void receiveTopicMessage1(String text) {    // 进行消息接收处理
        System.err.println("【*** 接收消息 topic1***】" + text);
    }

    @JmsListener(destination="xiayun.msg.topic",containerFactory="topicListenerContainer")
    public void receiveTopicMessage2(String text) {    // 进行消息接收处理
        System.err.println("【*** 接收消息 topic2***】" + text);
    }
}
