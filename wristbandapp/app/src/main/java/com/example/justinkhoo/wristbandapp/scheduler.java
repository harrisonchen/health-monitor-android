package com.example.justinkhoo.wristbandapp;

/**
 * Created by justinkhoo on 12/6/14.
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

public class scheduler {
    private final ScheduledExecutorService schedulerr = Executors.newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() { System.out.println("beep");
            };
            final ScheduledFuture beeperHandle = schedulerr.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
            schedulerr.schedule(new Runnable() {
                public void run() {
                    beeperHandle.cancel(true);
                }
            }, 60 * 60, SECONDS);
        }
    }
}