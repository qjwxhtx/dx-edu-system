package com.edu.auth.service;

import com.edu.auth.dto.AuthUser;
import com.edu.auth.entity.SMenuEntity;
import com.edu.auth.entity.SRoleEntity;
import com.edu.auth.entity.SUserEntity;
import com.edu.auth.repository.SMenuRepository;
import com.edu.auth.repository.SRoleRepository;
import com.edu.auth.repository.SUserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 自定义UserDetailsService
 *
 * @author hjw
 * @since 2025-10-27
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SUserRepository userRepository;

    @Autowired
    private SRoleRepository roleRepository;

    @Autowired
    private SMenuRepository menuRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 支持多租户用户名格式: username@tenantCode
        String[] usernameParts = username.split("@");
        String actualUsername = usernameParts[0];
        String tenantCode = usernameParts.length > 1 ? usernameParts[1] : "default";
        String systemCode = "";

        // 根据租户码查询用户
        SUserEntity user = userRepository.findByUsernameAndStatus(actualUsername, 1)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 查询用户角色
        List<SRoleEntity> roles = roleRepository.findByUserId(user.getId());

        // 查询用户权限
        List<SMenuEntity> menus = menuRepository.findMenusByUserId(user.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 添加权限
        for (SMenuEntity menu : menus) {
            if (StringUtils.hasText(menu.getPermissions())) {
                authorities.add(new SimpleGrantedAuthority(menu.getPermissions()));
            }
        }

        // 添加角色
        for (SRoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
        }

        return new AuthUser(user.getId(), user.getTenantId(), systemCode, user.getUsername(), user.getPassword(), user.getEmail(),
            user.getStatus(), authorities, roles.stream().map(SRoleEntity::getRoleCode).collect(Collectors.toList()),
            menus.stream().map(SMenuEntity::getPermissions).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
