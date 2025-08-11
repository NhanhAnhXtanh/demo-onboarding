package com.company.onboarding.security;

import com.company.onboarding.entity.Department;
import com.company.onboarding.entity.User;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.security.model.RowLevelBiPredicate;
import io.jmix.security.model.RowLevelPolicyAction;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.PredicateRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;
import org.springframework.context.ApplicationContext;

@RowLevelRole(name = "HR manager's departments and users", code = HRManagerRlRole.CODE)
public interface HRManagerRlRole {
    String CODE = "hr-manager-rl";

    @JpqlRowLevelPolicy(entityClass = Department.class, where = "{E}.hrManager.id = :current_user_id")
    void department();

    @JpqlRowLevelPolicy(entityClass = User.class, where = "{E}.department.hrManager.id = :current_user_id")
    void user();

    @JpqlRowLevelPolicy(
            entityClass = User.class,
            where = "{E}.department.id in :session_departmentIds"
    )
    void usersByDept();

    @JpqlRowLevelPolicy(
            entityClass = Department.class,
            where = "{E}.id in :session_departmentIds"
    )
    void depts();

//    @PredicateRowLevelPolicy(entityClass = Department.class, actions = RowLevelPolicyAction.READ)
//    default RowLevelBiPredicate<Department, ApplicationContext> deptPredicate() {
//        return (dept, ctx) -> {
//            var cus = ctx.getBean(CurrentUserSubstitution.class);
//            var u = cus.getEffectiveUser();
//            return dept.getHrManager() != null && dept.getHrManager().getId().equals(u.getClass());
//        };
//    }
//
//    @PredicateRowLevelPolicy(entityClass = User.class, actions = RowLevelPolicyAction.READ)
//    default RowLevelBiPredicate<User, ApplicationContext> userPredicate() {
//        return (user, ctx) -> {
//            var cus = ctx.getBean(io.jmix.core.usersubstitution.CurrentUserSubstitution.class);
//            var u = cus.getEffectiveUser();
//            return user.getDepartment() != null
//                    && user.getDepartment().getHrManager() != null
//                    && user.getDepartment().getHrManager().getId().equals(u.getClass());
//        };
//    }
}