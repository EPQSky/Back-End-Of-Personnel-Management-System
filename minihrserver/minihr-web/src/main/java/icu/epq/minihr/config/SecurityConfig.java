package icu.epq.minihr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.epq.minihr.model.Hr;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author EPQ
 * <p>
 * SpringSecurity的登陆配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;

    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;

    /**
     * BCR密码加密
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return o;
                    }
                })
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    /**
                     * 登陆成功并输出相关用户数据等的JSON
                     * @param httpServletRequest
                     * @param httpServletResponse
                     * @param authentication
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();


                        Hr hr = (Hr) authentication.getPrincipal();
                        hr.setPassword(null);
                        RespBean ok = RespBean.ok("登陆成功！", hr);
                        String s = new ObjectMapper().writeValueAsString(ok);

                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    /**
                     * 登陆失败，返回相关的错误信息
                     * @param httpServletRequest
                     * @param httpServletResponse
                     * @param e
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();

                        RespBean error = RespBean.error("登陆失败！");
                        if (e instanceof LockedException) {
                            error.setMsg("账户被锁定，请联系管理员！");
                        } else if (e instanceof CredentialsExpiredException) {
                            error.setMsg("密码过期，请联系管理员！");
                        } else if (e instanceof AccountExpiredException) {
                            error.setMsg("账户过期，请联系管理员！");
                        } else if (e instanceof DisabledException) {
                            error.setMsg("账户被禁用，请联系管理员！");
                        } else if (e instanceof BadCredentialsException) {
                            error.setMsg("用户名或密码输入错误，请重新输入！");
                        }

                        String s = new ObjectMapper().writeValueAsString(error);

                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/doLogout")
                .logoutSuccessUrl("/doLogout/success")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    /**
                     * 退出登陆成功以及反馈成功
                     * @param httpServletRequest
                     * @param httpServletResponse
                     * @param authentication
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();

                        RespBean ok = RespBean.ok("注销成功！");
                        String s = new ObjectMapper().writeValueAsString(ok);

                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable()
                //没有认证时，不重定向，返回错误信息
                .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                httpServletResponse.setStatus(401);
                PrintWriter out = httpServletResponse.getWriter();

                RespBean error = RespBean.error("访问失败！");
                if (e instanceof InsufficientAuthenticationException) {
                    error.setMsg("请求失败，请重新登陆！");
                }

                String s = new ObjectMapper().writeValueAsString(error);

                out.write(s);
                out.flush();
                out.close();
            }
        });

    }
}
