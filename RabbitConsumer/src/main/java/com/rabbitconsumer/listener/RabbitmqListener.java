package com.rabbitconsumer.listener;

import com.common.entity.User2Ticket;
import com.rabbitconsumer.mapper.MainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import javax.annotation.Resource;

@Slf4j
public class RabbitmqListener {
    @Resource
    MainMapper mapper;

    @RabbitListener(queues = "ticketOrder",  containerFactory = "listenerContainer",messageConverter = "jacksonConverter")
    public void receiver1(User2Ticket data){
        synchronized ("listener1"){//加一把锁，防止数据库连接池里面的连接给不过来
            mapper.insertOrder(data);
        }
    }

    @RabbitListener(queues = "ticketOrder",  containerFactory = "listenerContainer",messageConverter = "jacksonConverter")
    public void receiver2(User2Ticket data){
        synchronized ("listener2"){//加一把锁，防止数据库连接池里面的连接给不过来
            mapper.insertOrder(data);
        }
    }

    @RabbitListener(queues = "ticketOrder",  containerFactory = "listenerContainer",messageConverter = "jacksonConverter")
    public void receiver3(User2Ticket data){
        synchronized ("listener3"){//加一把锁，防止数据库连接池里面的连接给不过来
            mapper.insertOrder(data);
        }
    }
}
