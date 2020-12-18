package icu.epq.minihr.controller.system.basic;

import icu.epq.minihr.model.Department;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EPQ
 * <p>
 * 部门管理接口
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
        if (department.getResult() == 1) {
            return RespBean.ok("添加成功！", department);
        }
        return RespBean.error("添加失败！");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDepartmentById(@PathVariable Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentService.deleteDepartmentById(department);
        if (department.getResult() == -2) {
            return RespBean.error("该部门存在子部门，删除失败！");
        } else if (department.getResult() == -1) {
            return RespBean.error("该部门存在员工，删除失败！");
        } else if (department.getResult() == 1) {
            return RespBean.ok("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
