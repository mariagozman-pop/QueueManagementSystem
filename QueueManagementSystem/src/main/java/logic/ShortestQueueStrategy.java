package logic;

import models.Task;

import java.util.concurrent.BlockingQueue;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public int selectQueue(BlockingQueue<Task>[] queues) {
        int shortestQueueIndex = 0;
        int shortestQueueSize = Integer.MAX_VALUE;

        for (int i = 0; i < queues.length; i++) {
            int queueSize = queues[i].size();
            if (queueSize < shortestQueueSize) {
                shortestQueueSize = queueSize;
                shortestQueueIndex = i;
            }
        }

        return shortestQueueIndex;
    }
}