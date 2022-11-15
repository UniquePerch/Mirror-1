package com.mirror.mapper;


import com.common.entity.User2Ticket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
    @Insert("insert into t_user_ticket (uid,tid) values(#{uid},#{tid})")
    int insertOrder(User2Ticket ticket);
}
