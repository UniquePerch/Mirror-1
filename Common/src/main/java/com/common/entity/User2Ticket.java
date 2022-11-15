package com.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User2Ticket implements Serializable {
    int id;
    int uid;
    int tid;
}
