package icu.epq.minihr.controller.config;

import icu.epq.minihr.model.Menu;
import icu.epq.minihr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author EPQ
 *
 * 提供用户所能显示的菜单栏数据接口
 */
@RestController
@RequestMapping("/system/config")
public class SystemController {

    @Autowired
    MenuService menuService;

    @GetMapping("/menu")
    public List<Menu> getMenusByHrId(){
        return menuService.getMenusByHrId();
    }
}
