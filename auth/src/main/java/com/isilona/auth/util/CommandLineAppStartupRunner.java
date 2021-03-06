package com.isilona.auth.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.isilona.auth.model.Account;
import com.isilona.auth.model.Privilege;
import com.isilona.auth.model.Role;
import com.isilona.auth.service.IAccountService;
import com.isilona.auth.service.IPrivilegeService;
import com.isilona.auth.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

import static com.isilona.auth.util.HostmanagerConstants.Privileges;
import static com.isilona.auth.util.HostmanagerConstants.Roles;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPrivilegeService privilegeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (logger.isInfoEnabled()) {
            logger.info("Application started with command-line arguments: {} .", Arrays.toString(args));
            logger.info("To kill this application, press Ctrl + C.");
        }


        logger.info("Executing Setup");

        createPrivileges();
        createRoles();
        createUsers();

        logger.info("Setup Done");
    }

    private void createPrivileges() {

        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_PRIVILEGE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_ROLE_WRITE);

        createPrivilegeIfNotExisting(Privileges.CAN_USER_READ);
        createPrivilegeIfNotExisting(Privileges.CAN_USER_WRITE);

    }

    final void createPrivilegeIfNotExisting(final String name) {
        final Privilege entityByName = privilegeService.findByName(name);
        if (entityByName == null) {
            final Privilege entity = new Privilege(name);
            privilegeService.create(entity);
        }
    }

    // Role

    private void createRoles() {
        final Privilege canPrivilegeRead = privilegeService.findByName(Privileges.CAN_PRIVILEGE_READ);
        final Privilege canPrivilegeWrite = privilegeService.findByName(Privileges.CAN_PRIVILEGE_WRITE);
        final Privilege canRoleRead = privilegeService.findByName(Privileges.CAN_ROLE_READ);
        final Privilege canRoleWrite = privilegeService.findByName(Privileges.CAN_ROLE_WRITE);
        final Privilege canUserRead = privilegeService.findByName(Privileges.CAN_USER_READ);
        final Privilege canUserWrite = privilegeService.findByName(Privileges.CAN_USER_WRITE);

        Preconditions.checkNotNull(canPrivilegeRead);
        Preconditions.checkNotNull(canPrivilegeWrite);
        Preconditions.checkNotNull(canRoleRead);
        Preconditions.checkNotNull(canRoleWrite);
        Preconditions.checkNotNull(canUserRead);
        Preconditions.checkNotNull(canUserWrite);

        createRoleIfNotExisting(Roles.ROLE_USER, Sets.<Privilege>newHashSet(canUserRead, canRoleRead, canPrivilegeRead));
        createRoleIfNotExisting(Roles.ROLE_ADMIN, Sets.<Privilege>newHashSet(canUserRead, canUserWrite, canRoleRead, canRoleWrite, canPrivilegeRead, canPrivilegeWrite));

    }

    final void createRoleIfNotExisting(final String name, final Set<Privilege> privileges) {
        final Role entityByName = roleService.findByName(name);
        if (entityByName == null) {
            final Role entity = new Role(name);
            entity.setPrivileges(privileges);
            roleService.create(entity);
        }
    }

    // Account/Account

    final void createUsers() {
        final Role roleAdmin = roleService.findByName(Roles.ROLE_ADMIN);
        final Role roleUser = roleService.findByName(Roles.ROLE_USER);

        createUserIfNotExisting(
                HostmanagerConstants.ADMIN_USERNAME,
                HostmanagerConstants.ADMIN_EMAIL,
                passwordEncoder.encode(HostmanagerConstants.ADMIN_PASS),
                Sets.<Role>newHashSet(roleAdmin));
        createUserIfNotExisting(
                HostmanagerConstants.USER_USERNAME,
                HostmanagerConstants.USER_EMAIL,
                passwordEncoder.encode(HostmanagerConstants.USER_PASS),
                Sets.<Role>newHashSet(roleUser));
    }

    final void createUserIfNotExisting(final String username, final String loginName, final String pass, final Set<Role> roles) {
        final Account entityByName = accountService.findByName(loginName);
        if (entityByName == null) {
            final Account entity = new Account(username, loginName, pass, roles);
            accountService.create(entity);
        }
    }

}
