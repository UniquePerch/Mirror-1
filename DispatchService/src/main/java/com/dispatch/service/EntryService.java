package com.dispatch.service;

import com.common.entity.Status;

public interface EntryService {
    Status dispatch(int uid, int tid);
}
