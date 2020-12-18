package icu.epq.minihr.mapper;

import icu.epq.minihr.model.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    Integer getMaxWorkID();

    List<Employee> getEmployeeByPage(@Param("page") Integer page, @Param("size") Integer size, @Param("employee") Employee employee, @Param("dates") Date[] dates);

    Long getTotal(@Param("employee") Employee employee, @Param("dates") Date[] dates);

    Employee getEmployeeById(Integer id);
}