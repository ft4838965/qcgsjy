package com.stylefeng.guns.modular.startrmark.service.impl;

import com.stylefeng.guns.modular.system.model.Comment;
import com.stylefeng.guns.modular.system.dao.CommentMapper;
import com.stylefeng.guns.modular.startrmark.service.ICommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户评论表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
