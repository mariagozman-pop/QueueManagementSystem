package logic;

import models.Task;

import java.util.concurrent.BlockingQueue;

public class ShortestTimeStrategy implements Strategy {
    @Override
    public int selectQueue(BlockingQueue<Task>[] queues) {
        int shortestQueueIndex = 0;
        long shortestQueueTime = Long.MAX_VALUE;

        for (int i = 0; i < queues.length; i++) {
            long queueTime = calculateQueueTime(queues[i]);
            if (queueTime < shortestQueueTime) {
                shortestQueueTime = queueTime;
                shortestQueueIndex = i;
            }
        }

        return shortestQueueIndex;
    }

    private long calculateQueueTime(BlockingQueue<Task> queue) {
        long totalQueueTime = 0;
        for (Task task : queue) {
            totalQueueTime += task.getServiceTime();
        }
        return totalQueueTime;
    }
}