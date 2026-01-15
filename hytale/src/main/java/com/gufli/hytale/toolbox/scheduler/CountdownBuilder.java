package com.gufli.hytale.toolbox.scheduler;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CountdownBuilder {

    private final AsyncScheduler asyncScheduler;
    private final Duration duration;

    private Consumer<Long> handler;
    private Set<Countdown.Milestone> milestones = new HashSet<>();

    CountdownBuilder(AsyncScheduler asyncScheduler, Duration duration) {
        this.asyncScheduler = asyncScheduler;
        this.duration = duration;
    }

    //

    public CountdownBuilder handler(Consumer<Long> handler) {
        this.handler = handler;
        return this;
    }

    public CountdownBuilder milestone(Consumer<Long> handler, long... points) {
        for (long seconds : points) {
            milestones.add(new Countdown.Milestone(seconds, handler));
        }
        return this;
    }

    public Countdown build() {
        return new Countdown(asyncScheduler, duration, handler, milestones);
    }

}
