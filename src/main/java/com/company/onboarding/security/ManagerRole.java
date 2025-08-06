package com.company.onboarding.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "Manager Role", code = ManagerRole.CODE, scope = "UI")
public interface ManagerRole {
    String CODE = "manager-role";

    @EntityPolicy(entityName = "User", actions = {
            EntityPolicyAction.CREATE,
            EntityPolicyAction.READ,
            EntityPolicyAction.UPDATE,
            EntityPolicyAction.DELETE
    })
    @EntityAttributePolicy(entityName = "User", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void userAccess();

    @EntityPolicy(entityName = "UserStep", actions = {
            EntityPolicyAction.CREATE,
            EntityPolicyAction.READ,
            EntityPolicyAction.UPDATE,
            EntityPolicyAction.DELETE
    })
    @EntityAttributePolicy(entityName = "UserStep", attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    void userStepAccess();
}