package icu.epq.minihr.service;

import icu.epq.minihr.mapper.DepartmentMapper;
import icu.epq.minihr.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartmentsByPid(-1);
    }

    public Integer addDepartment(Department department) {
        department.setEnabled(true);
        return departmentMapper.addDepartment(department);
    }

    public void deleteDepartmentById(Department department) {
        departmentMapper.deleteDepartmentById(department);
    }

    public List<Department> getAllDepartmentsNotList() {
        return departmentMapper.getAllDepartmentsNotList();
    }
}
