package app.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
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

    @At("/?/show")
    @GET
    @Ok("jsp:jsp.weibo.show")
    public Weibo show(int id) {
        Weibo weibo = fetch(id);
        return weibo;
    }

    @At("/?/edit")
    @GET
    @Ok("jsp:jsp.weibo.edit")
    public Weibo edit(int id) {
        Weibo weibo = fetch(id);
        return weibo;
    }

    @At("/?/update")
    @POST
    @Ok(">>:/weibo/${obj.id}/show")
    public Weibo update(@Param("::weibo.") Weibo weibo) {
        weibo.setUpdatedAt(new Timestamp(Times.now().getTime()));
        dao().updateIgnoreNull(weibo);
        return weibo;
    }

    @At("/?/destroy")
    @POST
    @Ok(">>:/weibo/index")
    public void destroy(@Param("id") int id) {
        dao().delete(fetch(id));
    }
}
