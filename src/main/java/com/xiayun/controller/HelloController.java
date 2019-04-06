package com.xiayun.controller;

import com.xiayun.entity.PictureEntity;
import com.xiayun.service.PictureService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {

    private Log log = LogFactory.getLog(HelloController.class);

    @Autowired
    private PictureService pictureService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        log.info("hello");
        return "接收和处理请求，来自Spring Boot from Spring Boot!";
    }

    @RequestMapping("/save")
    @ResponseBody
    public String save() {
        PictureEntity entity = new PictureEntity();
        entity.setName("你好");
        pictureService.save(entity);
        return "你好";
    }

    @RequestMapping(value = "/test")
    public String test(Model model) {
        String name = "xiayun";
        String age = "14";
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        return "/home";
    }


}
