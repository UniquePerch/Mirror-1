package com.mirror.service;


import com.common.entity.User2Ticket;
import org.springframework.stereotype.Service;

@Service
public interface RabbitmqService {
    void Send(User2Ticket user2Ticket);
}
