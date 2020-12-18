package icu.epq.minihr.utils;

import icu.epq.minihr.model.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author EPQ
 *
 * 获取当前登录用户的信息
 */
public class HrUtils {
    public static Hr getCurrentHr() {
        return ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
