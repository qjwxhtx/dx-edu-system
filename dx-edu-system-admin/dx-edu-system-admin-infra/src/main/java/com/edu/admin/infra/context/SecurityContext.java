package com.edu.admin.infra.context;

import com.edu.admin.infra.bean.dto.LoginUser;

/**
 *
 *
 * @author hjw
 * @since 2025-10-29
 */
public class SecurityContext {

    private static final ThreadLocal<LoginUser> CURRENT_USER = new ThreadLocal<>();

    public static void setUser(LoginUser user) {
        CURRENT_USER.set(user);
    }

    public static LoginUser getUser() {
        return CURRENT_USER.get();
    }

    public static String getTenantId() {
        LoginUser user = getUser();
        return user != null ? user.getTenantId() : null;
    }

    public static String getSystemCode() {
        LoginUser user = getUser();
        return user != null ? user.getSystemCode() : null;
    }

    public static void clear() {
        CURRENT_USER.remove();
    }

}
