package com.hms.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private final String prefix;
    private final AtomicInteger counter;

    public IdGenerator(String prefix, int startFrom) {
        this.prefix = prefix;
        this.counter = new AtomicInteger(startFrom);
    }

    public synchronized String next() {
        return String.format("%s-%04d", prefix, counter.incrementAndGet());
    }
}
