package com.xuaninsr.xianyuque.controller;

import com.xuaninsr.xianyuque.pojo.FileInfo;
import com.xuaninsr.xianyuque.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller // controller 想要一个模板！
public class xianyuqueController {
    private FileInfoService fileInfoService;

    @Autowired
    public xianyuqueController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    @RequestMapping("/hello")
    @ResponseBody   // 将返回的 String 插入到 HTML body 中给 controller
    public String hello() {
        return "hello";
    }

    @RequestMapping("/list2")
    public String list2(Model model){
        System.out.println(model);
        model.addAttribute("hello", "hello world 2");
        System.out.println(model);
        // helloThymeleaf 会找到 templates/list.html，总而成为了模板
        return "list";
    }

    @RequestMapping("/list")
    public String list(Model model){
        System.out.println(model);
        model.addAttribute("files", fileInfoService.selectAllFileInfo());
        System.out.println(model);
        // helloThymeleaf 会找到 templates/list.html，总而成为了模板
        return "list";
    }
}
