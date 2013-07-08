package app.controllers;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@IocBy(type = ComboIocProvider.class, args = {"*org.nutz.ioc.loader.json.JsonLoader",
                                              "dao.js",
                                              "*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
                                              "app"})
@Modules(scanPackage = true)
@Fail("jsp:503")
public class ApplicationController {

    @At("/home/index")
    @Ok("raw")
    public static String index() {
        return "Hello Nutz.";
    }
}
