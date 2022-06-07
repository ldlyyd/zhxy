package com.ldl.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldl.zhxy.mapper.TeacherMapper;
import com.ldl.zhxy.pojo.Admin;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.pojo.Teacher;

import com.ldl.zhxy.service.TeacherService;
import com.ldl.zhxy.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Autowired
    private TeacherService teacherService;
    @Override
    public Teacher login(LoginForm loginForm) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getName,loginForm.getUsername());
        queryWrapper.eq(Teacher::getPassword, MD5.encrypt(loginForm.getPassword()));
        return teacherService.getOne(queryWrapper);
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getId,userId);
        return teacherService.getOne(queryWrapper);
    }

    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher) {
        LambdaQueryWrapper<Teacher> queryWrapper =new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getName())){
            queryWrapper.like(Teacher::getName,teacher.getName());
        }
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.eq(Teacher::getClazzName,teacher.getClazzName());
        }
        queryWrapper.orderByDesc(Teacher::getId);

        Page<Teacher> page = teacherService.page(pageParam,queryWrapper);
        return page;
    }
}
