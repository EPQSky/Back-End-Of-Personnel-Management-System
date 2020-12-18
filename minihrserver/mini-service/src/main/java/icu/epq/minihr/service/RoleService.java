package icu.epq.minihr.service;

import icu.epq.minihr.mapper.MenuMapper;
import icu.epq.minihr.mapper.MenuRoleMapper;
import icu.epq.minihr.mapper.RoleMapper;
import icu.epq.minihr.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    MenuRoleMapper menuRoleMapper;

    public List<Role> getAllRoles() {
        List<Role> roles = roleMapper.getAllRoles();
        for (Role role : roles) {
            role.setMenuIds(menuMapper.getMenusByRoleId(role.getId()));
        }
        return roles;
    }

    public Integer addRole(Role role) {
        if (!role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }
        return roleMapper.insertSelective(role);
    }

    @Transactional
    public Integer deleteRoleById(Integer id) {
        menuRoleMapper.deleteByRid(id);
        return roleMapper.deleteByPrimaryKey(id);
    }

    public List<Role> getAllHrRoles() {
        return roleMapper.getAllRoles();
    }
}
