package com.ftp.deploy.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author ZERO
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * session 会话过期处理策略
     */
    @Resource
    private MySessionExpiredStrategy mySessionExpiredStrategy;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()//
                .loginPage("/login").failureUrl("/login?error=true").permitAll()
                .and().logout().permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/static/**","/webSocket/**","/webSocket").permitAll()
                .anyRequest()               // 任何请求,登录后可以访问
                .authenticated()
                .and().csrf().disable()
                .httpBasic().and().cors().disable()
                .headers().frameOptions().sameOrigin()
                // 添加 Session管理器
                .and().sessionManagement()
                // Session失效后跳转到这个链接
                .invalidSessionUrl("/")
                //最大session并发数量，超过定义数量前一个session就会失效 配置一个账户的登录用户数量
                .maximumSessions(1)
                .expiredSessionStrategy(mySessionExpiredStrategy);
    }
}
