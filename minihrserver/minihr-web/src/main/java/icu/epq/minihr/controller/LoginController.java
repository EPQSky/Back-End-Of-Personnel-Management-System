package icu.epq.minihr.controller;

import icu.epq.minihr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author EPQ
 * <p>
 * 登陆提示
 */
@RestController
public class LoginController {

    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("尚未登陆，请登陆！");
    }
}
