package com.common.entity;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;

import java.io.Serializable;

@Data
@Serialization
public class Ticket implements Serializable {
    int tid;
    String TicketName;
    int Remain;
    int MaxCount;
}
