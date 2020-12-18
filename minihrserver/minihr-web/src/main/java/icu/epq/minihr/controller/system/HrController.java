package icu.epq.minihr.controller.system;


import icu.epq.minihr.model.Hr;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.model.Role;
import icu.epq.minihr.service.HrService;
import icu.epq.minihr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EPQ
 * <p>
 * Hr账户管理
 */
@RestController
@RequestMapping("/system/hr")
public class HrController {

    @Autowired
    HrService hrService;

    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public List<Hr> getAllHrs(String keyWords) {
        return hrService.getAllHrs(keyWords);
    }

    @PutMapping("/")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @GetMapping("/roles")
    public List<Role> getAllHrRoles() {
        return roleService.getAllHrRoles();
    }

    @PutMapping("/roles")
    public RespBean updateHrRoles(Integer hid, Integer[] rids) {
        if (hrService.updateHrRoles(hid, rids) == rids.length) {
            return RespBean.ok("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteHrById(@PathVariable Integer id) {
        if (hrService.deleteHrById(id) == 1) {
            return RespBean.ok("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
