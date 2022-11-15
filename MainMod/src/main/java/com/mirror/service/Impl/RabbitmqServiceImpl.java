package com.mirror.service.Impl;


import com.common.entity.User2Ticket;
import com.mirror.service.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RabbitmqServiceImpl implements RabbitmqService {
    @Resource
    RabbitTemplate template;

    @Override
    public void Send(User2Ticket user2Ticket) {
        template.convertAndSend("ticketOrder.direct1","my-ticketOrder",user2Ticket);
    }
}
