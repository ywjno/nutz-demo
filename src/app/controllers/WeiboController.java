package app.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.service.IdEntityService;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

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
        Sql sql = Sqls.fetchEntity("SELECT weibos.* FROM weibos WHERE id=@id")
                .setEntity(dao().getEntity(Weibo.class));
        sql.params().set("id", id);
        dao().execute(sql);
        Weibo weibo = sql.getObject(Weibo.class);

        return dao().fetchLinks(weibo, "comments");
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
    public void destroy(@Param("id") final int id) {
        Trans.exec(new Atom() {
            public void run() {
                Sql deleteWeibo = Sqls.create("DELETE FROM weibos WHERE id=@id");
                deleteWeibo.params().set("id", id);
                dao().execute(deleteWeibo);

                Sql deleteComments = Sqls.create("DELETE FROM $table WHERE weibo_id=@weibo_id");
                deleteComments.vars().set("table", "comments");
                deleteComments.params().set("weibo_id", id);
                dao().execute(deleteComments);
            }
        });
    }
}
