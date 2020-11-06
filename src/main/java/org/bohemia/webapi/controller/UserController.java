package org.bohemia.webapi.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bohemia.webapi.entity.User;
import org.bohemia.webapi.service.UserServiceApi;
import org.bohemia.webapi.utils.jwt.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户相关
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceApi userServiceApi;

    public UserController(UserServiceApi userServiceApi){ this.userServiceApi = userServiceApi; };


    @PostMapping("/login")
    public String login(HttpServletResponse response, @RequestBody JSONObject obj) throws JsonProcessingException {
        String token=null;
        User user = new User();
        user.setUsername(obj.getString("username"));
        user.setPassword(obj.getString("password"));
        User findUser = this.userServiceApi.getLoginOne(user);
        if(findUser!=null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            log.info(obj+"  尝试登录: " + date);
            token = TokenUtil.sign(findUser);
        }
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("token",token);
        if(token==null){
            hs.put("code","401");
            hs.put("msg","无该用户");
        }else {
            hs.put("code","200");
            hs.put("msg","登录成功");
        }
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(hs);
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public String test(@RequestBody Map<String,Object> para) throws JsonProcessingException {
        HashMap<String,Object> hs=new HashMap<>();
        hs.put("testdata","data");
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(hs);
    }

}
