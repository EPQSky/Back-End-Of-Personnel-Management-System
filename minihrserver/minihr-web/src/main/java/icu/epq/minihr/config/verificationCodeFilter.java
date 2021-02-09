package icu.epq.minihr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.epq.minihr.model.RespBean;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author EPQ
 */
@Component
public class verificationCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if ("POST".equals(request.getMethod()) && "/doLogin".equals(request.getServletPath())) {
            String code = request.getParameter("code");
            String verifyCode = (String) request.getSession().getAttribute("verify_code");
            if (code == null || "".equals(code) || !code.toLowerCase().equals(verifyCode.toLowerCase())) {
                //验证码不正确
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码填写错误！")));
                out.flush();
                out.close();
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
