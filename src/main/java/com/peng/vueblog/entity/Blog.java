package com.peng.vueblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author peng.lei
 * @since 2021-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("博客id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("博客标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("博客摘要")
    @NotBlank(message = "摘要不能为空")
    private String description;

    @ApiModelProperty("博客内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty("创建时间")
    // 返回的时候时间转为指定格式
    @JsonFormat(pattern = "yyyy-MM-dd hh:MM:ss")
    private LocalDateTime created;

    @ApiModelProperty("状态")
    private Integer status;


}
