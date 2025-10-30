package com.edu.auth.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义JWT权限转换器
 * 原理：从JWT令牌中提取权限信息并转换为Spring Security的GrantedAuthority
 *
 * @author hjw
 * @since 2025-10-27
 */
@Component
public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * 转换方法：从JWT中提取权限信息
     * 原理：JWT的payload中包含了用户的角色和权限信息
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 1. 提取权限信息
        List<String> permissions = jwt.getClaim("permissions");
        if (permissions != null) {
            authorities.addAll(permissions.stream().map(SimpleGrantedAuthority::new).toList());
        }

        // 2. 提取角色信息
        List<String> roles = jwt.getClaim("roles");
        if (roles != null) {
            authorities.addAll(
                roles.stream().map(SimpleGrantedAuthority::new).toList());
        }

        // 3. 如果没有明确权限，添加默认权限
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }
}
