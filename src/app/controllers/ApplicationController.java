package app.controllers;

import org.nutz.lang.Lang;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@IocBy(type = ComboIocProvider.class, args = {"*org.nutz.ioc.loader.json.JsonLoader",
                                              "dao.js",
                                              "*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
                                              "app"})
@Modules(scanPackage = true)
@Fail("jsp:503")
@Localization(value="msg", defaultLocalizationKey="en_US")
public class ApplicationController {

    @At("/home/index")
    @Ok("raw")
    public static String index() {
        return "Hello Nutz.";
    }

    @At("/weibo/change/?")
    @Ok(">>:/weibo/index")
    public void changeLocal(String lang) {
        lang = Lang.list("zh_CN", "en_US").contains(lang)? lang : "en_US";
        Mvcs.setLocalizationKey(lang);
    }
}
