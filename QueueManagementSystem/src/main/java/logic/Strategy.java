package logic;

import models.Task;

import java.util.concurrent.BlockingQueue;

public interface Strategy {
    int selectQueue(BlockingQueue<Task>[] queues);
}
