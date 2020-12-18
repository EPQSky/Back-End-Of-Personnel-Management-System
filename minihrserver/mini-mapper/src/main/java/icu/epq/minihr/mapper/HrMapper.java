package icu.epq.minihr.mapper;

import icu.epq.minihr.model.Hr;
import icu.epq.minihr.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadUserByUsername(String username);

    List<Role> getHrRoleById(Integer hrId);

    List<Hr> getAllHrs(@Param("id") Integer id, @Param("keyWords") String keyWords);
}