package com.ldl.zhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ldl.zhxy.pojo.Admin;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.pojo.Student;
import com.ldl.zhxy.pojo.Teacher;
import com.ldl.zhxy.service.AdminService;
import com.ldl.zhxy.service.StudentService;
import com.ldl.zhxy.service.TeacherService;
import com.ldl.zhxy.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;


    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                LambdaQueryWrapper<Admin> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(Admin::getId,userId.intValue());
                queryWrapper1.eq(Admin::getPassword,oldPwd);
                Admin admin =adminService.getOne(queryWrapper1);
                if (admin != null){
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 2:
                LambdaQueryWrapper<Student> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(Student::getId,userId.intValue());
                queryWrapper2.eq(Student::getPassword,oldPwd);
                Student student =studentService.getOne(queryWrapper2);
                if (student != null){
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 3:
                LambdaQueryWrapper<Teacher> queryWrapper3 = new LambdaQueryWrapper<>();
                queryWrapper3.eq(Teacher::getId,userId.intValue());
                queryWrapper3.eq(Teacher::getPassword,oldPwd);
                Teacher teacher =teacherService.getOne(queryWrapper3);
                if (teacher != null){
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

        }
        return Result.ok();
    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("图像资源") @RequestPart("multipartFile") MultipartFile multipartFile)
            //requestPart对象 requestParam String数组
    {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); //得到文件后缀

        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String fileName = uuid + suffix;

        String portraitPath = "D:\\study\\IDEACode\\zhxy\\target\\classes\\public\\upload\\".concat(fileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String path = "upload/".concat(fileName);

        return Result.ok(path);
    }

    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response)
    {
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(verifiCodeImage,"JPEG",outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation("登录功能实现")
    @PostMapping("/login")
    public Result login(
            @ApiParam("登录时提交的Form表单") @RequestBody LoginForm loginForm,
            HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginFormVerifiCode = loginForm.getVerifiCode();
        if("".equals(sessionVerifiCode) || sessionVerifiCode == null)
        {
            return Result.fail().message("验证码失效，请验证后重试！");
        }
        if(!sessionVerifiCode.equalsIgnoreCase(loginFormVerifiCode))
        {
            return Result.fail().message("验证码输入错误，请重试！");
        }
        session.removeAttribute("verifiCode");
        switch (loginForm.getUserType())
        {
            case 1:
                Map<String,Object> map = new LinkedHashMap<>();
                Admin admin = adminService.login(loginForm);
                try {
                    if(null != admin)
                    {
                        String token = JwtHelper.createToken((long) admin.getId(), 1);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码输入有误！");
                    }
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
                return Result.ok(map);
            case 2:
                Map<String,Object> map1 = new LinkedHashMap<>();
                Student student = studentService.login(loginForm);
                try {
                    if(null != student)
                    {
                        String token = JwtHelper.createToken((long) student.getId(), 2);
                        map1.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码输入有误！");
                    }
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
                return Result.ok(map1);
            case 3:
                Map<String,Object> map2 = new LinkedHashMap<>();
                Teacher teacher = teacherService.login(loginForm);
                try {
                    if(null != teacher)
                    {
                        String token = JwtHelper.createToken((long) teacher.getId(), 3);
                        map2.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码输入有误！");
                    }
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
                return Result.ok(map2);
        }

        return Result.fail().message("查无此人！");
    }

    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("token口令") @RequestHeader("token") String token)
    {
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration)
        {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType)
        {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",1);
                map.put("user",teacher);
                break;
        }

        return Result.ok(map);
    }
}
