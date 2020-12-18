package icu.epq.minihr.mapper;

import icu.epq.minihr.model.HrRole;
import org.apache.ibatis.annotations.Param;

public interface HrRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRole record);

    int insertSelective(HrRole record);

    HrRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HrRole record);

    int updateByPrimaryKey(HrRole record);

    void deleteByHrId(Integer hid);

    Integer addRoles(@Param("hid") Integer hid, @Param("rids") Integer[] rids);
}