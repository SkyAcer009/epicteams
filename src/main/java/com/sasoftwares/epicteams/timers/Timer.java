package com.sasoftwares.epicteams.timers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer {

    private final int max_threads = Runtime.getRuntime().availableProcessors();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(max_threads);

    private int interval;
    private int initialDelay;

    private TimeUnit unit;
    private TaskTimer task;

    private ScheduledFuture future;

    public Timer() {
    }

    public Timer(final int interval, final int initialDelay, final TimeUnit unit, TaskTimer task) {
        this.interval = interval;
        this.initialDelay = initialDelay;
        this.unit = unit;
        this.task = task;
    }

    public Timer start() {
        future = executor.scheduleAtFixedRate(() -> task.run(), initialDelay, interval, unit);
        return this;
    }

    public boolean isRunning() {
        return future != null && !future.isCancelled() && !future.isDone();
    }

    public void terminate() {
        if (isRunning()) {
            future.cancel(true);
        }
    }
}
