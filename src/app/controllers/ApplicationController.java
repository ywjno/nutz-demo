package app.controllers;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

public class ApplicationController {

    @At("/home/index")
    @Ok("raw")
    public static String index() {
        return "Hello Nutz.";
    }
}
