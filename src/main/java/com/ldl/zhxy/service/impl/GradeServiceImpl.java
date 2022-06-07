package com.ldl.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldl.zhxy.mapper.GradeMapper;
import com.ldl.zhxy.pojo.Grade;
import com.ldl.zhxy.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Autowired
    private GradeService gradeService;

    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        LambdaQueryWrapper<Grade> queryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(gradeName))
        {
            queryWrapper.like(Grade::getName,gradeName);
        }
        queryWrapper.orderByDesc(Grade::getId);
        Page<Grade> page1 = gradeService.page(page, queryWrapper);
        return page1;
    }

    @Override
    public List<Grade> getGrades() {
        List<Grade> grades = gradeService.list();
        return grades;
    }
}
