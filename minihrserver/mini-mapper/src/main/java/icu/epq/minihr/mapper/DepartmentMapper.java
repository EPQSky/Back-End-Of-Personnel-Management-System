package icu.epq.minihr.mapper;

import icu.epq.minihr.model.Department;

import java.util.List;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> getAllDepartmentsByPid(Integer pid);

    Integer addDepartment(Department department);

    void deleteDepartmentById(Department department);

    List<Department> getAllDepartmentsNotList();
}