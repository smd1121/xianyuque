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
    private Map<Cookie, String> cookies;

    @Autowired
    public xianyuqueController(FileInfoService fileInfoService, UserService userService) {
        this.fileInfoService = fileInfoService;
        this.userService = userService;
        this.cookies = new HashMap<>();
    }

    private String getCookieMapID(Cookie cookie) {
        for (Cookie c : cookies.keySet()) {
            if (c.getValue().equals(cookie.getValue()))
                return cookies.get(c);
        }
        return null;
    }

    private boolean checkIsLogin(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        String id;

        for (Cookie c : requestCookies) {
            id = getCookieMapID(c);
            if (id != null)
                return true;
        }
        return false;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        String id;
        for (Cookie c : requestCookies) {
            id = getCookieMapID(c);
            if (id != null)
                return "redirect:/list/" + id;
        }
        return "redirect:/login";
    }

    @RequestMapping("/list/{id}")
    public String list(Model model, @PathVariable String id, HttpServletRequest request){
        if (!checkIsLogin(request))
            return "redirect:/login";

        model.addAttribute("files", fileInfoService.selectAllFileInfo());
        // helloThymeleaf 会找到 templates/list.html，从而成为了模板
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
            return setCookie(user, response);
        }
    }

    private ModelAndView setCookie(@ModelAttribute("user") User user, HttpServletResponse response) {
        Cookie cookie = new Cookie("loginState", MD5Util.getMD5(user.getID()));
        cookie.setMaxAge(60 * 60 * 24);
        cookies.put(cookie, user.getID());
        response.addCookie(cookie);
        return new ModelAndView("redirect:/list/" + user.getID());
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
            return setCookie(user, response);
        }
    }
}
