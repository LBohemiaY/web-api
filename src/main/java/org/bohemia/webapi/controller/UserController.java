package org.bohemia.webapi.controller;


import org.bohemia.webapi.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestBody User user) {
        //TODO 服务器数据库的登录判断
        String username = user.getUsername();
        HashMap<String, Object> tokens = new HashMap<>();
        tokens.put("admin","admin-token");
        tokens.put("editor","editor-token");
        tokens.put("704398960@qq.com","hhhhhh");
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("token",tokens.get(username));
        response.put("code",20000);
        response.put("msg","登录成功");
        response.put("data",responseData);
        return response;
    }

    @GetMapping(value = "/info")
    @ResponseBody
    public Map info(@RequestParam("token")String token) {
        //TODO 服务器数据中的token操作
        HashMap<String, Object> users = new HashMap<>();
        User adminUser = new User();
        User editorUser = new User();
        List<String> adminRole = new ArrayList<String>();
        adminRole.add("admin-token");
        List<String> editorRole = new ArrayList<String>();
        adminRole.add("editor-token");
        adminUser.setRoles(adminRole);
        adminUser.setIntroduction("I am a super administrator");
        adminUser.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        adminUser.setName("Super Admin");
        editorUser.setRoles(editorRole);
        editorUser.setIntroduction("I am an editor");
        editorUser.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        editorUser.setName("Normal Editor");
        users.put("admin-token",adminUser);
        users.put("hhhhhh",editorUser);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("code",20000);
        responseData.put("msg","登录成功");
        responseData.put("data",users.get(token));
        return responseData;
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public Map logout() {
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("code",20000);
        responseData.put("msg","退出成功");
        responseData.put("data","success");
        return responseData;
    }

}
