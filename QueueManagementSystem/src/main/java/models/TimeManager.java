package models;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeManager implements Runnable {
    private final AtomicInteger currentTime;
    private final int simulationTimeInSeconds;
    private volatile boolean running;
    private final CountDownLatch secondLatch;

    public TimeManager(int simulationTimeInSeconds) {
        this.currentTime = new AtomicInteger(0);
        this.simulationTimeInSeconds = simulationTimeInSeconds;
        this.running = true;
        this.secondLatch = new CountDownLatch(1);
    }

    public int getCurrentTime() {
        return currentTime.get();
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while (currentTime.get() < simulationTimeInSeconds && running) {
            try {
                TimeUnit.SECONDS.sleep(1);
                currentTime.incrementAndGet();
                secondLatch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        running = false;
    }

    public int getSimulationTimeInSeconds() {
        return simulationTimeInSeconds;
    }

    public void awaitNextSecond() throws InterruptedException {
        secondLatch.await();
    }
}