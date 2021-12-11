package com.hanss.assetup.security;

import com.hanss.assetup.models.ERole;
import com.hanss.assetup.models.Role;
import com.hanss.assetup.models.User;
import com.hanss.assetup.repository.RoleRepository;
import com.hanss.assetup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        createRoleIfNotFound(ERole.ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_MODERATOR);
        createRoleIfNotFound(ERole.ROLE_USER);

        String email = "hanss@list.ru";
        if (!userRepository.existsByEmail(email)){
            Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
            Set roles = new HashSet();
            roles.add(adminRole.get());
            User user = new User();
            user.setUsername("hanss");
            user.setPassword(passwordEncoder.encode("privet"));
            user.setEmail(email);
            user.setRoles(roles);
            userRepository.save(user);
        }

        email = "koch@gmail.com";
        if (!userRepository.existsByEmail(email)){
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
            Set roles = new HashSet();
            roles.add(userRole.get());

            User user = new User();
            user.setUsername("irina");
            user.setPassword(passwordEncoder.encode("sdfsdf"));
            user.setEmail(email);
            user.setRoles(roles);
            userRepository.save(user);
        }

        email = "crazyelf12312@mail.ru";
        if (!userRepository.existsByEmail(email)){
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
            Set roles = new HashSet();
            roles.add(userRole.get());
            User user = new User();
            user.setUsername("ivan");
            user.setPassword(passwordEncoder.encode("privet"));
            user.setEmail(email);
            user.setRoles(roles);
            userRepository.save(user);
        }

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(ERole name) {
        Role role = roleRepository.findByName(name).get();
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
}
