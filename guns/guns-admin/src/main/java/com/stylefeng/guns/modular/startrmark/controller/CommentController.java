package com.stylefeng.guns.modular.startrmark.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Comment;
import com.stylefeng.guns.modular.startrmark.service.ICommentService;

/**
 * 星评管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-09 01:02:14
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    private String PREFIX = "/startrmark/comment/";

    @Autowired
    private ICommentService commentService;

    /**
     * 跳转到星评管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "comment.html";
    }

    /**
     * 跳转到添加星评管理
     */
    @RequestMapping("/comment_add")
    public String commentAdd() {
        return PREFIX + "comment_add.html";
    }

    /**
     * 跳转到修改星评管理
     */
    @RequestMapping("/comment_update/{commentId}")
    public String commentUpdate(@PathVariable Integer commentId, Model model) {
        Comment comment = commentService.selectById(commentId);
        model.addAttribute("item",comment);
        LogObjectHolder.me().set(comment);
        return PREFIX + "comment_edit.html";
    }

    /**
     * 获取星评管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return commentService.selectList(null);
    }

    /**
     * 新增星评管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Comment comment) {
        commentService.insert(comment);
        return SUCCESS_TIP;
    }

    /**
     * 删除星评管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer commentId) {
        commentService.deleteById(commentId);
        return SUCCESS_TIP;
    }

    /**
     * 修改星评管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Comment comment) {
        commentService.updateById(comment);
        return SUCCESS_TIP;
    }

    /**
     * 星评管理详情
     */
    @RequestMapping(value = "/detail/{commentId}")
    @ResponseBody
    public Object detail(@PathVariable("commentId") Integer commentId) {
        return commentService.selectById(commentId);
    }
}
