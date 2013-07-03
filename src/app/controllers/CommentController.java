package app.controllers;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.service.IdEntityService;

import app.models.Comment;

@IocBean(fields = "dao")
@At("/weibo/?")
public class CommentController extends IdEntityService<Comment> {

    @At("/comment/save")
    @POST
    @Ok(">>:/weibo/${obj.weiboId}/show")
    public Comment save(@Param("::comment.") Comment comment) {
        Comment createdComment = dao().insert(comment);
        return createdComment;
    }
}
