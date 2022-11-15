package com.mirror.service;


import com.common.entity.Status;
import org.springframework.stereotype.Service;

@Service
public interface MainService {
    Status saveInfo(int uid, int tid);
}
