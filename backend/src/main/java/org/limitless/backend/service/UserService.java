package org.limitless.backend.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.common.PageRequest;
import org.limitless.backend.common.PageResult;
import org.limitless.backend.dto.UserCreateRequest;
import org.limitless.backend.dto.UserPageRequest;
import org.limitless.backend.dto.UserUpdateRequest;
import org.limitless.backend.entity.SysUser;
import org.limitless.backend.mapper.SysUserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final SysUserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(SysUserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 分页查询用户
     */
    public PageResult<SysUser> selectPage(UserPageRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<SysUser> list = userMapper.selectPage(
                request.getUsername(), request.getRealName(),
                request.getPhone(), request.getRoleId());
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), request.getPageNumber(),
                request.getPageSize(), pageInfo.getTotal());
    }

    /**
     * 根据ID查询用户
     */
    public SysUser selectById(Integer id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    /**
     * 创建用户
     */
    public Integer create(UserCreateRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new BusinessException("手机号不能为空");
        }

        // 检查用户名是否已存在
        SysUser existing = userMapper.selectByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setRoleId(request.getRoleId());
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);

        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 更新用户
     */
    public void update(Integer id, UserUpdateRequest request) {
        SysUser existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }

        SysUser user = new SysUser();
        user.setId(id);
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setRoleId(request.getRoleId());
        user.setStatus(request.getStatus());

        userMapper.updateById(user);
    }

    /**
     * 删除用户
     */
    public void delete(Integer id) {
        SysUser existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    /**
     * 重置密码为默认密码 123456
     */
    public void resetPassword(Integer id) {
        SysUser existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.updatePassword(id, passwordEncoder.encode("123456"));
    }
}
