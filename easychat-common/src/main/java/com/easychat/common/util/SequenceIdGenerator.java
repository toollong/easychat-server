package com.easychat.common.util;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequenceIdGenerator {

    private static final AtomicInteger ID = new AtomicInteger();

    public static int nextId() {
        return ID.incrementAndGet();
    }
}
