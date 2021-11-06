package com.javamaster.spring_boot.config.handler;

import com.javamaster.spring_boot.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        User user = (User) authentication.getPrincipal();

        if (roles.contains("ADMIN")) {
            httpServletResponse.sendRedirect("/admin/");
        } else if (roles.contains("USER")) {
            httpServletResponse.sendRedirect("/user/" + user.getId());
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }
}