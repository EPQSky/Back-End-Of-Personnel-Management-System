package icu.epq.minihr.service;

import icu.epq.minihr.mapper.MenuRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuRoleService {

    @Autowired
    MenuRoleMapper menuRoleMapper;

    @Transactional
    public Integer updateMenuRoleByRid(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        return menuRoleMapper.insertRecord(rid, mids);
    }
}
