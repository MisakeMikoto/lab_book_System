package com.yiling.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.controller.exception.Result;
import com.yiling.domain.*;
import com.yiling.service.FeedbackService;
import com.yiling.service.IMailService;
import com.yiling.service.UserRoleService;
import com.yiling.service.UserService;
import com.yiling.utils.EmailUtils;
import com.yiling.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private IMailService iMailService;

    @Autowired
    private FeedbackService feedbackService;

    private HashMap<String,User> userHs = new HashMap<>();

    @Autowired
    private UserRoleService userRoleService;

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;


    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/info")
    public Result getUserInfoByToken(HttpServletRequest request){
        String s = (String) request.getAttribute("userId");
        Integer userId = Integer.valueOf(s);
        User user = userService.getUserById(userId);
        UserInfoResult userInfoResult = new UserInfoResult();
        userInfoResult.setUserId(userId);
        userInfoResult.setAvatar(user.getAvatar());
        userInfoResult.setEmail(user.getEmail());
        userInfoResult.setUsername(user.getUsername());
        userInfoResult.setRealName(user.getRealName());
        return new Result(200,userInfoResult,"获取成功");
    }

    @GetMapping("/notification")
    public Result getNotification(HttpServletRequest request){
        String userIdS = (String) request.getAttribute("userId");
        Integer userId = Integer.valueOf(userIdS);
        List<Feedback> feedbacksByUserId = feedbackService.getFeedbacksByUserId(userId);
        List<NotificationResult> notificationResults = new ArrayList<>();
        for (int i = 0; i < feedbacksByUserId.size(); i++) {
            NotificationResult notificationResult = new NotificationResult();
            Feedback feedback = feedbacksByUserId.get(i);
            notificationResult.setDetail(feedback.getContent());
            notificationResult.setTime(feedback.getFeedbackTime());
            notificationResult.setTitle(feedback.getTitle());
            notificationResults.add(notificationResult);
        }
        return new Result(200,notificationResults,"获取成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        boolean loginStatus = userService.login(user);
        user = userService.getUserByUsername(user.getUsername());
        if(user == null){
            throw new BusinessException(200,"用户不存在");
        }
        TokenUtils tokenUtils = new TokenUtils();
        JSONObject jsonObject = new JSONObject();
        UserRole oneByUserId = userRoleService.getOneByUserId(user.getUserId());
        if(oneByUserId != null){
            jsonObject.put("isAdmin",true);
        }else{
            jsonObject.put("isAdmin",false);
        }
        if(loginStatus){
            String token = tokenUtils.initToken(user);
            jsonObject.put("token",token);
            jsonObject.put("userId",user.getUserId());
            return new Result(200,jsonObject, "登陆成功");
        }
        return new Result(500,null,"账号或者密码错误");
    }

    @PostMapping("/getRegisterCode")
    public Result getRegisterCode(@RequestBody User user) throws MessagingException {
        String code = EmailUtils.generateVerificationCode();
        if(user.getUsername() == null){
            return new Result(500,null,"用户名不为空");
        }
        userHs.put(code,user);
        System.out.println(user);
        System.out.println(code);
        String content = "<div style='background-color: #f9f9f9; padding: 20px;'>" +
                "<h1 style='font-size: 24px; color: #333;'>您的验证码是：</h1>" +
                "<p style='font-size: 36px; color: #007bff;'>" + code + "</p>" +
                "<p style='font-size: 16px; color: #999;'>该验证码将在5分钟后过期。</p>" +
                "</div>";
        iMailService.sendHtmlMail(user.getEmail(),"邮箱验证码", content);
        return new Result(200,null,"邮件发送成功");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) throws MessagingException {
        user.setUserId(null);
        boolean regiserStatus = userService.regiser(user);
        if(regiserStatus){
            return new Result(200,user.getUsername(),"注册成功");
        }
        return new Result(500,user.getUsername(),"注册失败");
    }

    @PostMapping("/uploadAvatar")
    public Result upload(@RequestParam(name = "file", required = false) MultipartFile file, User user,HttpServletRequest request ) {
        String userId = (String) request.getAttribute("userId");
        user.setUserId(Integer.valueOf(userId));
        if (file == null) {
            return new Result(Code.BUSINESS_ERROR,false,"请选择要上传的图片");
        }
        if (file.getSize() > 1024 * 1024 * 10) {
            return new Result(Code.BUSINESS_ERROR,false,"图片过大");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return new Result(Code.BUSINESS_ERROR,false,"请上传jpg,jpeg,gif,png格式的图片");
        }
        String savePath = UPLOAD_FOLDER;
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        //通过UUID生成唯一文件名
        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix;
        try {
            //将文件保存指定目录
            file.transferTo(new File(savePath + filename));
            user.setAvatar("/static/img/" + filename);
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Code.BUSINESS_ERROR,false,"图片保存错误");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url","/static/img/"+filename);
        jsonObject.put("username",user.getUsername());
        //返回文件名称
        return new Result(Code.SAVE_OK,jsonObject,"保存成功");
//        return ResultUtil.success(ResultEnum.SUCCESS, filename, request);
    }

    @PostMapping("/checkRegisterCode")
    public Result checkRegisterCode(@RequestBody User user){

        User user1 = userHs.get(user.getCode());
        if(user1 != null){
            userHs.remove(user.getCode());
            return new Result(200,true,"验证码匹配成功");
        }
        return new Result(500,false,"验证码匹配失败");
    }

    @GetMapping("/getOneByUerId")
    public Result getUserRolesByUserId(Integer userId) {
        User userById = userService.getUserById(userId);
        return new Result(200,userById,"获取成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        boolean b = userService.update(user);
        if(b){
            return new Result(200,b,"更新成功");
        }
        return new Result(500,b,"更新失败");
    }

    @PostMapping("/deleteById")
    public Result delete(@RequestBody User user){
        boolean b = userService.delete(user);
        if(b){
            return new Result(200,b,"删除成功");
        }
        return new Result(500,b,"删除失败");
    }

}
