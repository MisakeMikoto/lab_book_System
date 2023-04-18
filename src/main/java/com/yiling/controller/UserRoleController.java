package com.yiling.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.controller.exception.Result;
import com.yiling.domain.UserRole;
import com.yiling.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:27
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/getOneByUserId")
    public Result getUserRolesByUserId(Integer userId) {
        UserRole oneByUserId = userRoleService.getOneByUserId(userId);
        return new Result(200,oneByUserId,"获取成功");
    }

    @PostMapping("/updateRole")
    public Result updateRole(@RequestBody UserRole userRole){
        boolean b = userRoleService.updateRole(userRole);
        if(b){
            return new Result(200,b,"更新成功");
        }
        return new Result(500,b,"更新失败");
    }

    @PostMapping("/removeRole")
    public Result removeRole(@RequestBody UserRole userRole){
        boolean b = userRoleService.removeRole(userRole);
        if(b){
            return new Result(200,b,"删除成功");
        }
        return new Result(500,b,"删除失败");
    }

    @PostMapping("/addRole")
    public Result addRole(@RequestBody UserRole userRole){
        boolean b = userRoleService.addRole(userRole);
        if(b){
            return new Result(200,b,"添加成功");
        }
        return new Result(500,b,"添加失败");
    }

    @GetMapping("/getAll")
    public Result getAll(){
        List<UserRole> allRole = userRoleService.getAllRole();
        return new Result(200,allRole,"获取成功");
    }
}
