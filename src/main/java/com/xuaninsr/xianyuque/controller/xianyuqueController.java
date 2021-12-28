package com.xuaninsr.xianyuque.controller;

import com.xuaninsr.xianyuque.pojo.FileInfo;
import com.xuaninsr.xianyuque.pojo.User;
import com.xuaninsr.xianyuque.service.FileInfoService;
import com.xuaninsr.xianyuque.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller // controller 想要一个模板！
public class xianyuqueController {

    private FileInfoService fileInfoService;
    private UserService userService;

    @Autowired
    public xianyuqueController(FileInfoService fileInfoService, UserService userService) {
        this.fileInfoService = fileInfoService;
        this.userService = userService;
    }

    @RequestMapping("/list")
    public String list(Model model){
        System.out.println(model);
        model.addAttribute("files", fileInfoService.selectAllFileInfo());
        System.out.println(model);
        // helloThymeleaf 会找到 templates/list.html，总而成为了模板
        return "list";
    }

    @RequestMapping("/edit")
    public String edit(Model model){
        return "edit";
    }

    @RequestMapping("/login")
    public String login(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("regTips", "");
        return "register";
    }

    @RequestMapping(value = "/processLogin", method=RequestMethod.POST)
    public String handleLogin(@ModelAttribute(value="user") User user) {
        
        return "redirect:/list";
    }

    /*@RequestMapping("/registerFailed")
    public String registerFailed(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("regTips", "用户名已存在。");
        return "register";
    }

    @RequestMapping(value = "/processRegister", method=RequestMethod.POST)
    public String handleRegister(@ModelAttribute(value="user") User user) {
        User existingUser = userService.selectUserByID(user.getID());
        if (existingUser != null) {
            return "redirect:/registerFailed";
        }
        else {
            userService.insertUser(user);
            return "redirect:/list";
        }
    }*/

    @RequestMapping(value = "/register", method=RequestMethod.POST)
    public ModelAndView handleRegister(@ModelAttribute(value="user") User user) {
        User existingUser = userService.selectUserByID(user.getID());
        if (existingUser != null) {
            ModelAndView mv = new ModelAndView("register");
            mv.addObject("regTips", "用户名已存在。");
            return mv;
        }
        else {
            userService.insertUser(user);
            ModelAndView mv = new ModelAndView("list");
            return mv;
        }
    }
}
