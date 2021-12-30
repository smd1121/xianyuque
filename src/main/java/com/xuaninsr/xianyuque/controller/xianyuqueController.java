package com.xuaninsr.xianyuque.controller;

import com.xuaninsr.xianyuque.pojo.Article;
import com.xuaninsr.xianyuque.pojo.FileInfo;
import com.xuaninsr.xianyuque.pojo.User;
import com.xuaninsr.xianyuque.service.FileInfoService;
import com.xuaninsr.xianyuque.service.UserService;
import com.xuaninsr.xianyuque.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@Controller // controller 想要一个模板！
public class xianyuqueController {
    private FileInfoService fileInfoService;
    private UserService userService;
    private Map<Cookie, String> cookies;

    @Autowired
    public xianyuqueController(FileInfoService fileInfoService,
                               UserService userService) {
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

    private User getUserByRequest(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        String id;

        for (Cookie c : requestCookies) {
            id = getCookieMapID(c);
            if (id != null)
                return userService.selectUserByID(id);
        }
        return null;
    }

    private ModelAndView setCookie(@ModelAttribute("user") User user,
                                   HttpServletResponse response) {
        Cookie cookie = new Cookie("loginState", MD5Util.getMD5(user.getID()));
        cookie.setMaxAge(60 * 60 * 24);
        cookies.put(cookie, user.getID());
        response.addCookie(cookie);
        return new ModelAndView("redirect:/list/" + user.getID());
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

    @RequestMapping(value = "/login", method=RequestMethod.GET)
    public String login(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("loginTips", "");
        return "login";
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
            System.out.println(MD5Util.getMD5(user.getPassword()) + " " + existingUser.getPassword() + " "
                    + MD5Util.getMD5(user.getPassword()).equals(existingUser.getPassword()));
            ModelAndView mv = new ModelAndView("login");
            mv.addObject("loginTips", "用户名或密码错误！");
            return mv;
        } else {
            return setCookie(user, response);
        }
    }

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("regTips", "");
        return "register";
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

    @RequestMapping(value = "/profile", method=RequestMethod.GET)
    public String profile(Model model, HttpServletRequest request){
        User user = getUserByRequest(request);
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("updateTips", "");
        return "profile";
    }

    @RequestMapping(value = "/profile", method=RequestMethod.POST)
    public ModelAndView handleProfile(@ModelAttribute(value="user") User user,
                                       HttpServletRequest request) {
        User existingUser = getUserByRequest(request);
        if (existingUser == null) {
            return new ModelAndView("redirect:/");
        }
        else {
            if (!StringUtils.isEmptyOrWhitespace(user.getPassword()))
                userService.updatePassword(existingUser.getID(), user.getPassword());

            if (!StringUtils.isEmptyOrWhitespace(user.getName()))
                userService.updateName(existingUser.getID(), user.getName());

            ModelAndView mv = new ModelAndView("profile");
            mv.addObject("user", getUserByRequest(request));
            mv.addObject("updateTips", "更新成功！");
            return mv;
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        for (Cookie c : requestCookies) {
            cookies.keySet().removeIf(key -> key.getValue().equals(c.getValue()));
        }
        return "redirect:/";
    }

    private void fillListWithSon(FileInfo f, List<FileInfo> list,
                                 String userID, int level) {
        for (FileInfo innerF : fileInfoService.selectSonVisibleForUser(f.getID(), userID)) {
            innerF.setTitle(" ".repeat(Math.max(0, level)) + innerF.getTitle());
            list.add(innerF);
            fillListWithSon(innerF, list, userID, level + 1);
        }
    }

    @RequestMapping("/list/{id}")
    public String list(Model model, @PathVariable String id,
                       HttpServletRequest request){
        User loginUser = getUserByRequest(request);

        if (loginUser == null)
            return "redirect:/login";

        if (!loginUser.getID().equals(id))
            return "redirect:/list/" + loginUser.getID();

        List<FileInfo> list = new ArrayList<>();
        for (FileInfo f : fileInfoService.selectAllTopLevelByUser(id)) {
            list.add(f);
            fillListWithSon(f, list, id, 1);
        }

        if (list.isEmpty())
            return "redirect:/start";

        model.addAttribute("files", list);
        // helloThymeleaf 会找到 templates/list.html，从而成为了模板
        return "list";
    }

    @RequestMapping(value = "/delete", method=RequestMethod.POST)
    public String handleDelete(@ModelAttribute(value="fileID") int fileID) {
        fileInfoService.deleteFileByID(fileID);
        return "redirect:/";
    }

    private boolean checkIfNotPrivileged(String userID, int fileID) {
        if (fileID == -1)
            return false;

        List<FileInfo> list = new ArrayList<>();
        for (FileInfo f : fileInfoService.selectAllTopLevelByUser(userID)) {
            list.add(f);
            fillListWithSon(f, list, userID, 1);
        }
        for (FileInfo f : list) {
            if (f.getID() == fileID)
                return false;
        }
        return true;
    }

    private ModelAndView setMVForEditAndRead(@PathVariable int id, ModelAndView mv) {
        FileInfo fileInfo = fileInfoService.selectFileInfoByID(id);
        if (fileInfo == null) {
            return new ModelAndView("/error");
        }
        Article article = new Article();
        article.setTitle(fileInfo.getTitle());
        article.setID(id);
        article.setContent(fileInfo.getLocalName());
        mv.addObject("article", article);
        return mv;
    }

    @RequestMapping("/read/{id}")
    public ModelAndView read(@PathVariable int id,
                             HttpServletRequest request) {
        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), id))
            return new ModelAndView("redirect:/error");

        ModelAndView mv = new ModelAndView("/read");
        return setMVForEditAndRead(id, mv);
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable int id,
                             HttpServletRequest request){
        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), id))
            return new ModelAndView("redirect:/error");

        ModelAndView mv = new ModelAndView("/edit");
        id = fileInfoService.getCache(id).getID();
        return setMVForEditAndRead(id, mv);
    }

    private Map<String, Long> newPeriod = new HashMap<>();

    @RequestMapping("/new")
    public ModelAndView handleNew(HttpServletRequest request){
        System.out.println("  new  ");
        User user = getUserByRequest(request);
        if (user == null)
            return new ModelAndView("redirect:/");

        if (newPeriod.get(user.getID()) != null) {
            if (System.currentTimeMillis() - newPeriod.get(user.getID()) <= 1000)
                return new ModelAndView("redirect:/");
            else
                newPeriod.remove(user.getID());
        }

        newPeriod.put(user.getID(), System.currentTimeMillis());

        FileInfo fileInfo = new FileInfo(
                fileInfoService.getLargestFileID() + (int)(Math.random() * 100),
                "未命名", "", false, true,
                new Timestamp(System.currentTimeMillis()), -1, -1);
        fileInfoService.insertFileInfo(fileInfo);
        fileInfoService.insertPrivilege(fileInfo.getID(), user.getID());

        FileInfo cacheFileInfo = new FileInfo(
                fileInfoService.getLargestFileID() + (int)(Math.random() * 100),
                "未命名", "", false, true,
                new Timestamp(System.currentTimeMillis()), fileInfo.getID(), -1);
        fileInfoService.insertFileInfo(cacheFileInfo);
        fileInfoService.insertPrivilege(cacheFileInfo.getID(), user.getID());

        ModelAndView mv = new ModelAndView("/edit");
        return setMVForEditAndRead(cacheFileInfo.getID(), mv);
    }

    @RequestMapping("/start")
    public String start() {
        return "start";
    }

    @RequestMapping("/publish")
    public ModelAndView publish(@ModelAttribute(value="article") Article article,
                                HttpServletRequest request) {
        int actualID = fileInfoService.getActual(article.getID()).getID();

        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), actualID))
            return new ModelAndView("redirect:/error");

        article.setID(actualID);
        fileInfoService.updateFile(article);
        return new ModelAndView("redirect:/read/" + article.getID());
    }

    @RequestMapping("/autoSave/{id}")
    public String autoSave(@ModelAttribute(value="article") Article article,
                           HttpServletRequest request, @PathVariable int id) {

        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), id))
            return "redirect:/error";

        fileInfoService.updateFile(article);
        return "redirect:/edit/" + fileInfoService.getActual(id).getID();
    }

    @RequestMapping("/move/{id}")
    public ModelAndView handleMove(@PathVariable int id,
                                   HttpServletRequest request) {
        System.out.println("Move: " + id);

        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), id))
            return new ModelAndView("redirect:/error");

        List<FileInfo> list = new ArrayList<>();
        for (FileInfo f : fileInfoService.selectAllTopLevelByUser(user.getID())) {
            list.add(f);
            fillListWithSon(f, list, user.getID(), 1);
        }

        ModelAndView mv = new ModelAndView("/move");
        mv.addObject("files", list);
        mv.addObject("thisFile", id);
        return mv;
    }

    private boolean checkIfMoveValid(String userID, int id, int toId) {
        if (toId == -1)
            return true;

        if (id == toId)
            return false;

        List<FileInfo> list = new ArrayList<>();
        fillListWithSon(fileInfoService.selectFileInfoByID(id), list, userID, 1);
        for (FileInfo f : list) {
            System.out.println("Check: " + f.getID() + toId);
            if (toId == f.getID())
                return false;
        }
        return true;
    }

    @RequestMapping("/moveTo/{id}/{toId}")
    public ModelAndView handleMoveTo(@PathVariable int id,
                                     @PathVariable int toId,
                                   HttpServletRequest request) {
        if (toId == 0)
            toId = -1;

        User user = getUserByRequest(request);
        if (user == null || checkIfNotPrivileged(user.getID(), id)
         || checkIfNotPrivileged(user.getID(), toId))
            return new ModelAndView("redirect:/error");

        if (checkIfMoveValid(user.getID(), id, toId))
            fileInfoService.moveFileTo(id, toId);

        return new ModelAndView("redirect:/");
    }
}
