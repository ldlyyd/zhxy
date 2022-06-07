package com.ldl.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldl.zhxy.mapper.StudentMapper;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.pojo.Student;
import com.ldl.zhxy.service.StudentService;
import com.ldl.zhxy.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentService studentService;
    @Override
    public Student login(LoginForm loginForm) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getName,loginForm.getUsername());
        queryWrapper.eq(Student::getPassword, MD5.encrypt(loginForm.getPassword()));
        return studentService.getOne(queryWrapper);
    }

    @Override
    public Student getStudentById(Long userId) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getId,userId);
        return studentService.getOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(student.getName()))
        {
            queryWrapper.like(Student::getName,student.getName());
        }
        if(!StringUtils.isEmpty(student.getClazzName()))
        {
            queryWrapper.like(Student::getClazzName,student.getClazzName());
        }
        queryWrapper.orderByDesc(Student::getId);

        Page<Student> page1 = studentService.page(page,queryWrapper);

        return page1;
    }
}
