package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.MyUserDetails;
import com.example.DocumentManagement.entity.RolesEntity;
import com.example.DocumentManagement.entity.UserRoleEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.RolesRepository;
import com.example.DocumentManagement.repository.UserRoleRepository;
import com.example.DocumentManagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity usersEntity = usersRepository.findByUsernameOrEmailAndIsDeletedFalse(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("No user name found"));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userRoleRepository.findByUserId(usersEntity.getId()).forEach(role -> authorities.add(new SimpleGrantedAuthority(rolesRepository.findOneById(role.getRoleId()).getRole())));
        return new org.springframework.security.core.userdetails.User(usersEntity.getUsername(), usersEntity.getPass(), authorities);
    }

    private List<RolesEntity> getAllRoleOfUser(UsersEntity user) {
        List<UserRoleEntity> listUserRole = userRoleRepository.findByUserId(user.getId());
        if (listUserRole.isEmpty()) {
            throw new NotFoundException("Role of user is empty");
        }
        List<RolesEntity> listRole = new ArrayList<>();
        for (UserRoleEntity userRole : listUserRole) {
            listRole.add(rolesRepository.findOneById(userRole.getRoleId()));
        }
        return listRole;
    }

    public MyUserDetails createMyUserDetails(UsersEntity usersEntity) {
        return new MyUserDetails(usersEntity, getAllRoleOfUser(usersEntity));
    }
}
