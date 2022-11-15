package com.mirror.service.Impl;


import com.common.entity.Status;
import com.common.entity.Ticket;
import com.common.entity.User2Ticket;
import com.mirror.mapper.MainMapper;
import com.mirror.service.MainService;
import com.mirror.service.RabbitmqService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MainServiceImpl implements MainService {

    @Resource
    RabbitmqService rabbitmqService;

    @Resource
    StringRedisTemplate template;

    @Resource
    RedissonClient redissonClient;

    @Resource
    MainMapper mapper;

    private final static DefaultRedisScript<Long> lock;


    static {
        lock = new DefaultRedisScript<>();
        // 指定脚本文件
        lock.setLocation(new ClassPathResource("./lock.lua"));
        // 指定返回值
        lock.setResultType(Long.class);
    }


    @Transactional
    public Ticket findTicket(int tid){
        String key = "{ticket}"+tid+":remain";
        Ticket ticket = new Ticket();
        String remain = template.opsForValue().get(key);
        ticket.setTid(tid);
        if(remain != null)
            ticket.setRemain(Integer.parseInt(remain));
        else
            System.out.println(key);
        return ticket;
    }

    @Override
    @Transactional
    public Status saveInfo(int uid, int tid) {
        Status status = new Status();
        String key = "{ticket}:" + uid + "-" + tid;
        String value = template.opsForValue().get(key);
        RLock rLock = redissonClient.getLock("qiangpiao");
        if (value != null) {//如果这个人已经抢到票了
            status.setStatusDescribe("你已经抢到票辣!");
        } else {
            try {
                if(rLock.tryLock(20,30,TimeUnit.SECONDS)){
                    Ticket ticket = findTicket(tid);
                    if (ticket.getRemain() > 0) {   //如果票还有余量
                        List<String> list = Arrays.asList(key, "{ticket}" + tid + ":remain");
                        template.opsForValue().getOperations().execute(lock, list, String.valueOf(new Date()));
                        User2Ticket user2Ticket = new User2Ticket();
                        user2Ticket.setTid(ticket.getTid());
                        user2Ticket.setUid(uid);
                        int res = mapper.insertOrder(user2Ticket);
                        if(res == 1) status.setStatusDescribe("抢票成功辣!");
                        else status.setStatusDescribe("出现未知错误辣!");
                    } else {
                        status.setStatusDescribe("票已经被抢光辣!");
                    }
                    rLock.unlock();
                }
            }catch (InterruptedException e){
                log.error("线程锁异常");
            }
        }
        return status;
    }
}
