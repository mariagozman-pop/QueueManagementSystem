package models;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Callable<Long> {
    private final int id;
    private final BlockingQueue<Task> taskQueue;
    private final AtomicBoolean running;
    private final TimeManager timeManager;

    public Server(int id, BlockingQueue<Task> taskQueue, AtomicBoolean running, TimeManager timeManager) {
        this.id = id;
        this.taskQueue = taskQueue;
        this.running = running;
        this.timeManager = timeManager;
    }

    @Override
    public Long call() {
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            Task task;
            try {
                timeManager.awaitNextSecond();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            synchronized (taskQueue) {
                task = taskQueue.peek();
            }
            if (task != null) {
                processTask(task);
                synchronized (taskQueue) {
                    taskQueue.remove();
                }
            }
            if (!running.get()) {
                break;
            }
        }
        return calculateTotalWaitingTime();
    }

    private void processTask(Task task) {
        int startTime = timeManager.getCurrentTime();
        System.out.println("Queue " + id + " processing client " + task.getId() + " for " + task.getServiceTime() + " seconds starting at time " + startTime);
        task.setStartTime(startTime);

        while (task.getServiceTime() > 0) {
            int currentTime = timeManager.getCurrentTime();
            if (currentTime > startTime) {
                task.decrementServiceTime();
                startTime = currentTime;
            }
        }

        System.out.println("Queue " + id + " completed processing client " + task.getId());
    }
    public Task[] getTasks() {
        return taskQueue.toArray(new Task[0]);
    }

    public long calculateTotalWaitingTime() {
        long totalWaitingTime = 0;
        for (Task task : taskQueue) {
            totalWaitingTime += task.getServiceTime();
        }
        return totalWaitingTime;
    }
}