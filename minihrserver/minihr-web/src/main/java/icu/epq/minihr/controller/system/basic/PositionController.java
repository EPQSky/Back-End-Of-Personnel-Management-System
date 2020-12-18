package icu.epq.minihr.controller.system.basic;

import icu.epq.minihr.model.Position;
import icu.epq.minihr.model.RespBean;
import icu.epq.minihr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EPQ
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    PositionService positionService;

    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.addPosition(position) == 1) {
            return RespBean.ok("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position) {
        if (positionService.updatePosition(position) == 1) {
            return RespBean.ok("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id) {
        if (positionService.deletePositionById(id) == 1) {
            return RespBean.ok("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @DeleteMapping("/")
    public RespBean deletePositionsByIds(@RequestBody Position[] positions) {
        Integer[] ids = new Integer[positions.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = positions[i].getId();
        }
        if (positionService.deletePositionsByIds(ids) == ids.length) {
            return RespBean.ok("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
