package org.limitless.backend.service;

import org.junit.jupiter.api.*;
import org.limitless.backend.common.BusinessException;
import org.limitless.backend.dto.AuditRequest;
import org.limitless.backend.dto.RegistrationCreateRequest;
import org.limitless.backend.entity.ActivityRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityRegistrationServiceTest {

    @Autowired
    private ActivityRegistrationService registrationService;

    @Test
    @Order(1)
    void create_shouldInsertRegistration() {
        System.out.println("\n========== ActivityRegistrationServiceTest.create ==========");
        RegistrationCreateRequest request = new RegistrationCreateRequest();
        request.setActivityId(1);
        request.setUserId(6);
        request.setActivityType("个人参赛");
        request.setRemark("请多关照");

        ActivityRegistration result = registrationService.create(request);
        System.out.println("id: " + result.getId());
        System.out.println("registrationNo: " + result.getRegistrationNo());
        System.out.println("auditStatus: " + result.getAuditStatus());

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("PENDING", result.getAuditStatus());
    }

    @Test
    @Order(2)
    void audit_shouldApproveRegistration() {
        System.out.println("\n========== ActivityRegistrationServiceTest.audit_审批通过 ==========");
        // 先创建一个待审核的记录
        RegistrationCreateRequest createReq = new RegistrationCreateRequest();
        createReq.setActivityId(1);
        createReq.setUserId(4);
        createReq.setActivityType("团队参赛");
        ActivityRegistration pending = registrationService.create(createReq);
        System.out.println("创建待审核报名 ID: " + pending.getId() + ", No: " + pending.getRegistrationNo());

        AuditRequest auditReq = new AuditRequest();
        auditReq.setAuditStatus("APPROVED");
        auditReq.setAuditComment("审核通过，欢迎参赛");

        registrationService.audit(pending.getId(), auditReq);
        System.out.println("报名 " + pending.getId() + " 审核通过");
    }

    @Test
    @Order(3)
    void audit_shouldRejectRegistration() {
        System.out.println("\n========== ActivityRegistrationServiceTest.audit_驳回 ==========");
        RegistrationCreateRequest createReq = new RegistrationCreateRequest();
        createReq.setActivityId(1);
        createReq.setUserId(5);
        createReq.setActivityType("个人参赛");
        ActivityRegistration pending = registrationService.create(createReq);
        System.out.println("创建待审核报名 ID: " + pending.getId());

        AuditRequest auditReq = new AuditRequest();
        auditReq.setAuditStatus("REJECTED");
        auditReq.setAuditComment("报名信息不完整");

        registrationService.audit(pending.getId(), auditReq);
        System.out.println("报名 " + pending.getId() + " 已驳回");
    }

    @Test
    @Order(4)
    void delete_shouldRemoveRegistration() {
        System.out.println("\n========== ActivityRegistrationServiceTest.delete ==========");
        RegistrationCreateRequest createReq = new RegistrationCreateRequest();
        createReq.setActivityId(1);
        createReq.setUserId(6);
        createReq.setActivityType("个人参赛");
        ActivityRegistration pending = registrationService.create(createReq);
        System.out.println("创建待删除报名 ID: " + pending.getId());

        registrationService.delete(pending.getId());
        System.out.println("报名 " + pending.getId() + " 已删除");
    }
}
