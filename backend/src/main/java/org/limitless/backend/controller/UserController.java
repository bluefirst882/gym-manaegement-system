package org.limitless.backend.controller;

import org.limitless.backend.common.PageResult;
import org.limitless.backend.common.Result;
import org.limitless.backend.dto.UserCreateRequest;
import org.limitless.backend.dto.UserPageRequest;
import org.limitless.backend.dto.UserUpdateRequest;
import org.limitless.backend.entity.SysUser;
import org.limitless.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户
     * POST /api/users
     */
    @PostMapping("/users")
    public Result<PageResult<SysUser>> listUsers(@RequestBody UserPageRequest request) {
        PageResult<SysUser> pageResult = userService.selectPage(request);
        return Result.success("查询成功", pageResult);
    }

    /**
     * 新增用户
     * POST /api/user
     */
    @PostMapping("/user")
    public Result<Integer> createUser(@RequestBody UserCreateRequest request) {
        Integer id = userService.create(request);
        return Result.success("新增成功", id);
    }

    /**
     * 修改用户
     * PUT /api/user/{id}
     */
    @PutMapping("/user/{id}")
    public Result<Void> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequest request) {
        userService.update(id, request);
        return Result.success("修改成功");
    }

    /**
     * 删除用户
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 重置密码
     * PUT /api/user/{id}/reset-password
     */
    @PutMapping("/user/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Integer id) {
        userService.resetPassword(id);
        return Result.success("密码重置成功");
    }
}
