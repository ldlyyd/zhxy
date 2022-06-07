package com.ldl.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldl.zhxy.pojo.Student;
import com.ldl.zhxy.service.StudentService;
import com.ldl.zhxy.utils.MD5;
import com.ldl.zhxy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("删除单个或多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("要删除学生编号的JSON数组") @RequestBody List<Integer> ids)
    {
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("添加或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("新增或修改学生的JSON") @RequestBody Student student)
    {
        if (null == student.getId() || 0 == student.getId()) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Student student
            )
    {
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> IPage = studentService.getStudentByOpr(page,student);

        return Result.ok(IPage);
    }



}
