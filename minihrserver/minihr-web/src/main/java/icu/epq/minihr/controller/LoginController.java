package icu.epq.minihr.controller;

import icu.epq.minihr.config.VerificationCode;
import icu.epq.minihr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    @GetMapping("/verifyCode")
    public void verify(HttpSession session, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String codeText = code.getText();

        session.setAttribute("verify_code", codeText);
        VerificationCode.output(image, response.getOutputStream());
    }
}
