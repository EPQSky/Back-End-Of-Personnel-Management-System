package icu.epq.minihr.controller.system.basic;

import icu.epq.minihr.model.Menu;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.model.Role;
import icu.epq.minihr.service.MenuRoleService;
import icu.epq.minihr.service.MenuService;
import icu.epq.minihr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/perMiss")
public class PermissController {

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRoleService menuRoleService;

    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @PutMapping("/")
    public RespBean updateMenuRoleByRid(Integer rid, Integer[] mids) {
        if (menuRoleService.updateMenuRoleByRid(rid, mids) == mids.length) {
            return RespBean.ok("修改成功！");
        }
        return RespBean.error("修改失败！");
    }

    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role) {
        if (roleService.addRole(role) == 1) {
            return RespBean.ok("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteRoleById(@PathVariable Integer id) {
        if (roleService.deleteRoleById(id) == 1) {
            return RespBean.ok("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
