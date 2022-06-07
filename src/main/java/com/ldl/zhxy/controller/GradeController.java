package com.ldl.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ldl.zhxy.pojo.Grade;
import com.ldl.zhxy.service.GradeService;
import com.ldl.zhxy.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades()
    {
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("根据年级名称模糊查询，带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(@ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
                            @ApiParam("分页查询的每页条数") @PathVariable("pageSize") Integer pageSize,
                            @ApiParam("分页查询模糊匹配的名称") @RequestParam("gradeName") String gradeName)
    {
        Page<Grade> page = new Page<>(pageNo,pageSize);
        IPage<Grade> iPage = gradeService.getGradeByOpr(page,gradeName);
        return Result.ok(iPage);
    }

    @ApiOperation("新增或修改Grade,有id属性是修改,反之为增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("JSON的grade对象") @RequestBody Grade grade)
    {
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的所有grade的id的JSON集合") @RequestBody List<Integer> ids)
    {
        gradeService.removeByIds(ids);
        return Result.ok();
    }

}
