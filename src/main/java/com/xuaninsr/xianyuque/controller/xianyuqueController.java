package com.xuaninsr.xianyuque.controller;

import com.xuaninsr.xianyuque.pojo.User;
import com.xuaninsr.xianyuque.service.FileInfoService;
import com.xuaninsr.xianyuque.service.UserService;
import com.xuaninsr.xianyuque.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller // controller 想要一个模板！
public class xianyuqueController {

    private FileInfoService fileInfoService;
    private UserService userService;
    private List<Cookie> cookies;

    private boolean isInCookie(String loginState) {
        for (Cookie c : cookies) {
            if (c.getValue().equals(loginState))
                return true;
        }
        return false;
    }

    @Autowired
    public xianyuqueController(FileInfoService fileInfoService, UserService userService) {
        this.fileInfoService = fileInfoService;
        this.userService = userService;
        this.cookies = new ArrayList<>() ;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        for (Cookie c : requestCookies) {
            String loginState = c.getValue();
            System.out.println("requestCookie: " + c.getName() + ' ' + loginState);
            if (isInCookie(loginState))
                return "redirect:/list";
        }
        return "redirect:/login";
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

    @RequestMapping(value = "/login", method=RequestMethod.GET)
    public String login(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("loginTips", "");
        return "login";
    }

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("regTips", "");
        return "register";
    }

    @RequestMapping(value = "/login", method=RequestMethod.POST)
    public ModelAndView handleLogin(@ModelAttribute(value="user") User user,
                                    HttpServletResponse response) {
        User existingUser = userService.selectUserByID(user.getID());
        if (existingUser == null) {
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("loginTips", "用户不存在！");
            return mv;
        }
        else if (!MD5Util.getMD5(user.getPassword()).equals(existingUser.getPassword())) {
            System.out.println(user.getPassword() + " " + existingUser.getPassword());
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("loginTips", "用户名或密码错误！");
            return mv;
        } else {
            Cookie cookie = new Cookie("loginState", MD5Util.getMD5(user.getID()));
            cookie.setMaxAge(60 * 60 * 24);
            cookies.add(cookie);
            response.addCookie(cookie);
            return new ModelAndView("redirect:/list");
        }
    }

    @RequestMapping(value = "/register", method=RequestMethod.POST)
    public ModelAndView handleRegister(@ModelAttribute(value="user") User user,
                                       HttpServletResponse response) {
        User existingUser = userService.selectUserByID(user.getID());
        if (existingUser != null) {
            ModelAndView mv = new ModelAndView("register");
            mv.addObject("regTips", "用户名已存在。");
            return mv;
        }
        else {
            userService.insertUser(user);
            Cookie cookie = new Cookie("loginState", MD5Util.getMD5(user.getID()));
            cookie.setMaxAge(60 * 60 * 24);
            cookies.add(cookie);
            response.addCookie(cookie);
            return new ModelAndView("redirect:/list");
        }
    }
}
