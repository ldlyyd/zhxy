package com.ldl.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.pojo.Student;

public interface StudentService extends IService<Student> {
    public Student login(LoginForm loginForm);

    public Student getStudentById(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
