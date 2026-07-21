package org.limitless.backend.service;

import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.LoginRequest;
import org.limitless.backend.dto.LoginResponse;
import org.limitless.backend.entity.SysUser;
import org.limitless.backend.mapper.SysUserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SysUserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final org.limitless.backend.util.JwtUtil jwtUtil;

    public AuthService(SysUserMapper userMapper,
                       BCryptPasswordEncoder passwordEncoder,
                       org.limitless.backend.util.JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }

        SysUser user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 更新最后登录时间
        userMapper.updateLastLogin(user.getId());

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleId());

        // 构建响应
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(), user.getUsername(), user.getRealName(),
                user.getPhone(), user.getGender(), user.getRoleId(), user.getRoleName()
        );

        return new LoginResponse(token, userInfo);
    }
}
