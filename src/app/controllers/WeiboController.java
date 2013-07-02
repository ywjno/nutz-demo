package app.controllers;

import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.service.IdEntityService;

import app.models.Weibo;

@IocBean(fields = "dao")
@At("/weibo")
public class WeiboController extends IdEntityService<Weibo> {

    @At
    @GET
    @Ok("jsp:jsp.weibo.index")
    public List<?> index() {
        return dao().query(Weibo.class, null);
    }

    @At
    @GET
    @Ok("jsp:jsp.weibo.create")
    public void create() {

    }

    @At("/save")
    @POST
    @Ok("redirect:/weibo/index")
    public void save(@Param("::weibo.") Weibo weibo) {
        dao().insert(weibo);
    }
}
