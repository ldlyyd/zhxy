package com.ldl.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldl.zhxy.pojo.Admin;
import com.ldl.zhxy.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    public Admin login(LoginForm loginForm);

    public Admin getAdminById(Long userId);

    IPage<Admin> getAdminsByOpr(Page<Admin> pageParam, String adminName);
}
