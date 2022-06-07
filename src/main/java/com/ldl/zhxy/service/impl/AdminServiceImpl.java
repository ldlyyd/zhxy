package com.ldl.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldl.zhxy.mapper.AdminMapper;
import com.ldl.zhxy.pojo.Admin;
import com.ldl.zhxy.pojo.LoginForm;
import com.ldl.zhxy.service.AdminService;
import com.ldl.zhxy.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminService adminService;
    @Override
    public Admin login(LoginForm loginForm) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getName,loginForm.getUsername());
        queryWrapper.eq(Admin::getPassword, MD5.encrypt(loginForm.getPassword()));
        Admin admin = adminService.getOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getId,userId);
        return adminService.getOne(queryWrapper);
    }

    @Override
    public IPage<Admin> getAdminsByOpr(Page<Admin> pageParam, String adminName) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(adminName))
        {
            queryWrapper.like(Admin::getName,adminName);
        }
        queryWrapper.orderByDesc(Admin::getId);
        Page<Admin> page = adminService.page(pageParam,queryWrapper);
        return page;
    }
}
