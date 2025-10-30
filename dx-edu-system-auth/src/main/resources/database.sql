-- 租户表
CREATE TABLE IF NOT EXISTS s_tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_code VARCHAR(64) NOT NULL UNIQUE COMMENT '租户编码',
    tenant_name VARCHAR(128) NOT NULL COMMENT '租户名称',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant_code (tenant_code)
    ) COMMENT '租户表';

-- 系统表
CREATE TABLE IF NOT EXISTS s_system (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_code VARCHAR(64) NOT NULL UNIQUE COMMENT '系统编码',
    system_name VARCHAR(128) NOT NULL COMMENT '系统名称',
    description VARCHAR(500) COMMENT '系统描述',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_system_code (system_code)
    ) COMMENT '系统表';

-- 用户表
CREATE TABLE IF NOT EXISTS s_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant_username (tenant_id, username),
    INDEX idx_username (username)
    ) COMMENT '用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS s_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    role_code VARCHAR(64) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(128) NOT NULL COMMENT '角色名称',
    description VARCHAR(500) COMMENT '角色描述',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant_role (tenant_id, role_code)
    ) COMMENT '角色表';

-- 用户角色关系表
CREATE TABLE IF NOT EXISTS s_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
    ) COMMENT '用户角色关系表';

-- 菜单表
CREATE TABLE IF NOT EXISTS s_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id BIGINT NOT NULL COMMENT '系统ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_code VARCHAR(64) NOT NULL COMMENT '菜单编码',
    menu_name VARCHAR(128) NOT NULL COMMENT '菜单名称',
    menu_type TINYINT DEFAULT 1 COMMENT '菜单类型:1-菜单,2-按钮',
    path VARCHAR(256) COMMENT '菜单路径',
    component VARCHAR(256) COMMENT '组件路径',
    icon VARCHAR(128) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    permissions VARCHAR(500) COMMENT '权限标识',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_system_parent (system_id, parent_id),
    INDEX idx_menu_code (menu_code)
    ) COMMENT '菜单表';

-- 角色菜单权限表
CREATE TABLE IF NOT EXISTS s_role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id)
    ) COMMENT '角色菜单权限表';


-- OAuth2授权信息和令牌数据存储表
CREATE TABLE IF NOT EXISTS oauth2_authorization (
     `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授权记录的唯一标识',
    `registered_client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册的客户端ID',
    `principal_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户主体名称',
    `authorization_grant_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '授权类型',
    `authorized_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户授权的作用域',
    `attributes` blob NULL COMMENT '额外的认证属性（二进制存储',
    `state` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'OAuth 2.0 state 参数',
    `authorization_code_value` blob NULL COMMENT '授权码值',
    `authorization_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '授权码签发时间',
    `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata` blob NULL,
    `access_token_value` blob NULL COMMENT '访问令牌值',
    `access_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌签发时间',
    `access_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌过期时间',
    `access_token_metadata` blob NULL,
    `access_token_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '令牌类型（如 Bearer）',
    `access_token_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `oidc_id_token_value` blob NULL COMMENT 'ID Token 值',
    `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL COMMENT 'ID Token 签发时间',
    `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL COMMENT 'ID Token 过期时间',
    `oidc_id_token_metadata` blob NULL,
    `refresh_token_value` blob NULL,
    `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
    `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
    `refresh_token_metadata` blob NULL,
    `user_code_value` blob NULL COMMENT '用户代码（设备流）',
    `user_code_issued_at` timestamp NULL DEFAULT NULL,
    `user_code_expires_at` timestamp NULL DEFAULT NULL,
    `user_code_metadata` blob NULL,
    `device_code_value` blob NULL COMMENT '设备代码（设备流）',
    `device_code_issued_at` timestamp NULL DEFAULT NULL,
    `device_code_expires_at` timestamp NULL DEFAULT NULL,
    `device_code_metadata` blob NULL,
    PRIMARY KEY (`id`) USING BTREE
    );

CREATE TABLE IF NOT EXISTS oauth_client (
    `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部唯一标识',
    `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端标识符',
    `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端密钥（bcrypt加密）',
    `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端认证方法',
    `authorization_grant_types` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支持的授权类型',
    `scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可请求的作用域',
    `redirect_uris` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向URI',
    `client_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端设置',
    `token_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '令牌设置',
    `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    );