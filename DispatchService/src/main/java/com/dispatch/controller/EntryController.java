package com.dispatch.controller;

import com.common.entity.Status;
import com.dispatch.service.EntryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class EntryController {
    @Resource
    EntryService service;

    @PostMapping("/order")
    @ResponseBody
    public Status getTicket(int uid, int tid){
        return service.dispatch(uid,tid);
    }
}
