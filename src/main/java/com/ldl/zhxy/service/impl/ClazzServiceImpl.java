package com.ldl.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldl.zhxy.mapper.ClazzMapper;
import com.ldl.zhxy.pojo.Clazz;
import com.ldl.zhxy.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Autowired
    private ClazzService clazzService;
    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {
        LambdaQueryWrapper<Clazz> queryWrapper = new LambdaQueryWrapper<>();
        String gradeName = clazz.getGradeName();
        String Name = clazz.getName();
        if(!StringUtils.isEmpty(gradeName))
        {
            queryWrapper.like(Clazz::getGradeName,gradeName);
        }
        if(!StringUtils.isEmpty(Name))
        {
            queryWrapper.like(Clazz::getName,Name);
        }
        return clazzService.page(page,queryWrapper);
    }

    @Override
    public List<Clazz> getClazzs() {
        List<Clazz> clazzes = clazzService.list();
        return clazzes;
    }
}
