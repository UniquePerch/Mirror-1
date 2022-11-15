package com.dispatch.service.Impl;

import com.common.entity.Status;
import com.dispatch.service.EntryService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

@Service
public class EntryServiceImpl implements EntryService {
    private static final RestTemplate template;
    static {
        template = new RestTemplate();
    }
    @Override
    public Status dispatch(int uid, int tid) {
        long time = new Date().getTime(); //获取当前时间戳
        HashMap<String, Integer> map = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        map.put("tid",tid);
        map.put("uid",uid);
        HttpEntity<HashMap<String, Integer>> request = new HttpEntity<>(map, headers);
        Status status = null;
        if(time % 3 == 0){
            status = template.postForObject("http://localhost:70/get_ticket",request,Status.class);
        }
        if(time % 3 == 1){
            status = template.postForObject("http://localhost:71/get_ticket",request,Status.class);
        }
        if(time % 3 == 2){
            status = template.postForObject("http://localhost:72/get_ticket",request,Status.class);
        }
        return status;
    }
}
