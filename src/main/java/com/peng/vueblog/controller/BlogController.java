package com.peng.vueblog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peng.vueblog.common.lang.Result;
import com.peng.vueblog.entity.Blog;
import com.peng.vueblog.service.BlogService;
import com.peng.vueblog.util.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author peng.lei
 * @since 2021-05-30
 */
@Api(tags = "博客管理接口")
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @ApiOperation("获取博客列表")
    // Get传参：@RequestParam ---> /list?currentPage=currentPage
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        // MybatisPlus封装了Page
        Page page = new Page(currentPage, 5);
        // 查询分页数据
        IPage pageData = blogService.page(page,new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageData);
    }

    @ApiOperation("删除博客")
    // Get传参：@PathVariable ---> /detail/id
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);
    }

    @ApiOperation("删除博客")
    // 注意POST接收的都是JSON格式
    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String,Long> params){
        Long blogId = params.get("id");
        boolean isSuccess = blogService.removeById(blogId);
        if (isSuccess) {
            return Result.succ(null);
        } else {
            return Result.fail("删除博客失败");
        }
    }

    @ApiOperation("编辑博客")
    @RequiresAuthentication
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        Blog tempBlog = null;
        if (blog.getId() != null) {
            tempBlog = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(tempBlog.getUserId().longValue() == ShiroUtils.getProfile().getId().longValue());
        } else {
            tempBlog = new Blog();
            tempBlog.setUserId(ShiroUtils.getProfile().getId());
            tempBlog.setCreated(LocalDateTime.now());
            tempBlog.setStatus(0);
        }
        BeanUtil.copyProperties(blog,tempBlog,"id","userId","created","status");
        // MybatisPlus 新增或更新数据库
        blogService.saveOrUpdate(tempBlog);
        return Result.succ(null);
    }

}
