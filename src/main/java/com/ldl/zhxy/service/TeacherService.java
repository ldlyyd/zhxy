package com.ldl.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    public Teacher login(LoginForm loginForm);

    public Teacher getTeacherById(Long userId);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher);
}
