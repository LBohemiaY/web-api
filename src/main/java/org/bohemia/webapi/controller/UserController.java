package org.bohemia.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bohemia.webapi.entity.User;
import org.bohemia.webapi.service.UserServiceApi;
import org.bohemia.webapi.utils.annotation.LoginToken;
import org.bohemia.webapi.utils.annotation.PassToken;
import org.bohemia.webapi.utils.jwt.TokenUtil;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 用户相关
 */
@Slf4j
@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

    private final UserServiceApi userServiceApi;

    public UserController(UserServiceApi userServiceApi){ this.userServiceApi = userServiceApi; };

    @PassToken
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public String login(@RequestBody JSONObject obj) throws IOException {
        String token=null;
        User user = new User();
        user.setUsername(obj.getString("username"));
        user.setPassword(obj.getString("password"));
        User findUser = userServiceApi.getLoginOne(user);
        if(findUser!=null && findUser.getStatus().equals("normal")){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            log.info(obj.getString("username")+"  尝试登录: " + date);
            token = TokenUtil.sign(findUser);
        }
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("token",token);
        if(token==null&&findUser==null){
            hs.put("code",401);
            hs.put("message","无此用户，请先注册");
        }else if (token==null&&findUser.getStatus().equals("verify")){
            hs.put("code",401);
            hs.put("message","请等待账号审核");
        } else {
            hs.put("code",200);
            hs.put("message","登录成功");
        }
        ObjectMapper objectMapper=new ObjectMapper();
        log.info(objectMapper.writeValueAsString(hs));
        return objectMapper.writeValueAsString(hs);
    }

    @PassToken
    @ApiOperation(value = "登录信息查询接口")
    @GetMapping("/info")
    public String getInfo(@RequestParam("token")String token) throws JsonProcessingException {
        String username = TokenUtil.getClaimUsername(token);
        User findUser = userServiceApi.findUserByUsername(username);
        HashMap<String,Object> hs=new HashMap<>();
        if(findUser==null){
            hs.put("code",400);
            hs.put("message","token验证失败，不存在此用户或token过期");
        }else{
            hs.put("code",200);
            hs.put("name",findUser.getName());
            hs.put("role",findUser.getRole());
            hs.put("introduction",findUser.getIntroduction());
            hs.put("avatar",findUser.getAvatar());
        }
        ObjectMapper objectMapper=new ObjectMapper();
        log.info(objectMapper.writeValueAsString(hs));
        return objectMapper.writeValueAsString(hs);
    }

    @ApiOperation(value = "退出登录接口")
    @PostMapping("/logout")
    public String logout(@RequestParam(value="token")String token) throws JsonProcessingException {
        String username = TokenUtil.getClaimUsername(token);
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("code", 200);
        hs.put("message", username+"退出登录!");
        ObjectMapper objectMapper=new ObjectMapper();
        log.info(objectMapper.writeValueAsString(hs));
        return objectMapper.writeValueAsString(hs);
    }

    @ApiOperation(value = "注册账号接口")
    @PostMapping("/register")
    public String register(@RequestBody JSONObject obj) throws JsonProcessingException {
        int id = userServiceApi.addUser(obj);
        log.info("注册账号id: ",id);
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("code", 200);
        hs.put("message", "注册成功！等待审核");
        ObjectMapper objectMapper=new ObjectMapper();
        log.info(objectMapper.writeValueAsString(hs));
        return objectMapper.writeValueAsString(hs);
    }

    @ApiOperation(value = "检查用户名是否重复")
    @GetMapping("/usernameCheck")
    public String usernameCheck(@RequestParam("username")String username) throws JsonProcessingException {
        int count = userServiceApi.usernameCheck(username);
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("code", 200);
        hs.put("count", count);
        ObjectMapper objectMapper=new ObjectMapper();
        log.info(objectMapper.writeValueAsString(hs));
        return objectMapper.writeValueAsString(hs);
    }



    @LoginToken
    @GetMapping("/getLoginToken")
    public String getLoginToken(){
        return "你已通过验证";
    }

    @PassToken
    @GetMapping("/getPassToken")
    public String getPassToken(){
        return "跳过验证";
    }



}
