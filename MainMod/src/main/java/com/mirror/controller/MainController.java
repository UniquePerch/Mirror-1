package com.mirror.controller;


import com.common.entity.Status;
import com.mirror.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class MainController {
    @Resource
    MainService service;

    @PostMapping("/get_ticket")
    @ResponseBody
    public Status getTicket(@RequestBody Map<String, Integer> map){
        int tid = map.get("tid");
        int uid = map.get("uid");
        return service.saveInfo(uid,tid);
    }
}
