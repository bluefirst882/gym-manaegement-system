package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.UserCreateRequest;
import org.limitless.backend.dto.UserUpdateRequest;
import org.limitless.backend.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void selectById_shouldReturnExistingUser() {
        System.out.println("\n========== UserServiceTest.selectById ==========");
        SysUser user = userService.selectById(3);
        System.out.println("id: " + user.getId());
        System.out.println("username: " + user.getUsername());
        System.out.println("realName: " + user.getRealName());
        System.out.println("phone: " + user.getPhone());
        System.out.println("roleName: " + user.getRoleName());

        Assertions.assertEquals("张晓", user.getRealName());
        Assertions.assertNotNull(user.getRoleName());
    }

    @Test
    @Order(2)
    void create_shouldInsertNewUser() {
        System.out.println("\n========== UserServiceTest.create ==========");
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("newuser_" + System.currentTimeMillis());
        request.setPhone("13800000099");
        request.setRealName("新用户");
        request.setGender("男");
        request.setRoleId(2);
        request.setStatus(1);

        Integer id = userService.create(request);
        System.out.println("created userId: " + id);

        Assertions.assertNotNull(id);
        Assertions.assertTrue(id > 6); // 已有6条数据，新ID大于6

        // 验证可查询到
        SysUser created = userService.selectById(id);
        System.out.println("查询验证 - realName: " + created.getRealName());
        Assertions.assertEquals("新用户", created.getRealName());
    }

    @Test
    @Order(3)
    void update_shouldModifyUser() {
        System.out.println("\n========== UserServiceTest.update ==========");
        UserUpdateRequest request = new UserUpdateRequest();
        request.setRealName("修改后的名字");
        request.setPhone("13800000088");

        userService.update(3, request);
        System.out.println("用户 ID 3 更新成功");

        SysUser updated = userService.selectById(3);
        System.out.println("更新后 realName: " + updated.getRealName());
        System.out.println("更新后 phone: " + updated.getPhone());
        Assertions.assertEquals("修改后的名字", updated.getRealName());
        Assertions.assertEquals("13800000088", updated.getPhone());
    }

    @Test
    @Order(4)
    void delete_shouldRemoveUser() {
        System.out.println("\n========== UserServiceTest.delete ==========");
        // 先创建一个用户用于删除
        UserCreateRequest createReq = new UserCreateRequest();
        createReq.setUsername("todelete_" + System.currentTimeMillis());
        createReq.setPhone("13800000001");
        createReq.setRealName("待删除");
        createReq.setRoleId(2);
        createReq.setStatus(1);
        Integer newId = userService.create(createReq);
        System.out.println("创建待删除用户 ID: " + newId);

        userService.delete(newId);
        System.out.println("用户 " + newId + " 已删除");

        Assertions.assertThrows(BusinessException.class, () -> userService.selectById(newId));
        System.out.println("验证: 删除后查询抛出 BusinessException");
    }

    @Test
    @Order(5)
    void resetPassword_shouldSucceed() {
        System.out.println("\n========== UserServiceTest.resetPassword ==========");
        userService.resetPassword(5);
        System.out.println("用户 5 密码已重置为 123456 (加密后存储)");
    }
}
