package com.peng.vueblog.service.impl;

import com.peng.vueblog.entity.Blog;
import com.peng.vueblog.mapper.BlogMapper;
import com.peng.vueblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peng.lei
 * @since 2021-05-30
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
