package com.ldl.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldl.zhxy.pojo.Teacher;
import com.ldl.zhxy.service.TeacherService;
import com.ldl.zhxy.utils.MD5;
import com.ldl.zhxy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("分页获取教师信息,带搜索条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo ,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize ,
            @ApiParam("查询条件") Teacher teacher
    ){
        Page<Teacher> pageParam =new Page<>(pageNo,pageSize);

        IPage<Teacher> page = teacherService.getTeachersByOpr(pageParam,teacher);

        return Result.ok(page);
    }

    @ApiOperation("添加或者修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("要保存或者修改的JOSN格式的Teacher") @RequestBody Teacher teacher
    ){
        if (teacher.getId() == null || teacher.getId() ==0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        teacherService.saveOrUpdate(teacher);
        return  Result.ok();
    }

    @ApiOperation("删除单个或者多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("要删除的教师信息的id的JOSN集合")  @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
