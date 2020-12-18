package icu.epq.minihr.service;

import icu.epq.minihr.mapper.HrMapper;
import icu.epq.minihr.mapper.HrRoleMapper;
import icu.epq.minihr.model.Hr;
import icu.epq.minihr.model.HrRole;
import icu.epq.minihr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author EPQ
 * <p>
 * 校验用户登陆信息是否和数据库一致
 */
@Service
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Autowired
    HrRoleMapper hrRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        hr.setRoles(hrMapper.getHrRoleById(hr.getId()));
        return hr;
    }

    public List<Hr> getAllHrs(String keyWords) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(), keyWords);
    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    @Transactional
    public Integer updateHrRoles(Integer hid, Integer[] rids) {
        hrRoleMapper.deleteByHrId(hid);
        return hrRoleMapper.addRoles(hid, rids);
    }

    @Transactional
    public Integer deleteHrById(Integer id) {
        hrRoleMapper.deleteByHrId(id);
        return hrMapper.deleteByPrimaryKey(id);
    }
}
