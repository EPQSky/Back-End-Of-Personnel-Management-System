package icu.epq.minihr.controller;

import icu.epq.minihr.model.Hr;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author EPQ
 */
@RestController
public class HrCurrentInfoController {

    @Autowired
    HrService hrService;

    @GetMapping("/hr/info")
    public Hr getHrCurrentInfo(Authentication authentication) {
        return (Hr) authentication.getPrincipal();
    }

    @PutMapping("/hr/info")
    public RespBean updateHr(@RequestBody Hr hr, Authentication authentication) {
        if (hrService.updateHr(hr) == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("修改成功!");
        }
        return RespBean.error("修改失败!");
    }
}
