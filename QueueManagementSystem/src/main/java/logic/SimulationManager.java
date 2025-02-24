package logic;

import models.Server;
import models.Task;
import models.TimeManager;
import gui.EventDisplay;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationManager implements Runnable{
    private final int numQueues;
    private final BlockingQueue<Task>[] queues;
    private final List<Task> clients;
    private final Strategy strategy;
    private final int simulationTimeInSeconds;
    private final TimeManager timeManager;
    private final LogOfEvents logOfEvents;
    private int previousTime = -1;
    private final EventDisplay queueEvolutionGUI;

    public SimulationManager(int numQueues, int simulationTimeInSeconds, BlockingQueue<Task>[] queues, List<Task> clients, Strategy strategy, EventDisplay queueEvolutionGUI) {
        this.numQueues = numQueues;
        this.simulationTimeInSeconds = simulationTimeInSeconds;
        this.queues = queues;
        this.clients = clients;
        this.strategy = strategy;
        this.timeManager = new TimeManager(simulationTimeInSeconds);
        this.logOfEvents = new LogOfEvents("log.txt");
        this.queueEvolutionGUI = queueEvolutionGUI;
    }

    @Override
    public void run() {
        go();
    }

    public void go() {
        ExecutorService executor = Executors.newFixedThreadPool(numQueues);
        AtomicBoolean running = new AtomicBoolean(true);

        Thread timeManagerThread = new Thread(timeManager);
        timeManagerThread.start();

        int peakSecond = 0;
        int maxQueueSizeSum = 0;
        List<Task> clientsCopy = new ArrayList<>(clients);

        while (timeManager.isRunning()) {

            int currentTime = timeManager.getCurrentTime();
            if (currentTime >= timeManager.getSimulationTimeInSeconds()) {
                break;
            }
            if (currentTime == previousTime) {
                continue;
            }
            previousTime = currentTime;

            boolean allQueuesEmpty = true;
            for (BlockingQueue<Task> queue : queues) {
                if (!queue.isEmpty()) {
                    allQueuesEmpty = false;
                    break;
                }
            }
            if (clientsCopy.isEmpty() && allQueuesEmpty) {
                break;
            }

            for (Task client : clients) {
                if (client.getArrivalTime() == currentTime) {
                    int selectedQueue = strategy.selectQueue(queues);
                    try {
                        queues[selectedQueue].put(client);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Server[] servers = new Server[numQueues];
            for (int i = 0; i < numQueues; i++) {
                servers[i] = new Server(i + 1, queues[i], running, timeManager);
            }

            for (int i = 0; i < numQueues; i++) {
                executor.submit(servers[i]);
            }
            List<Server> servers2 = new ArrayList<>();
            for (int i = 0; i < queues.length; i++) {
                Server server = new Server(i + 1, queues[i],running,timeManager);
                servers2.add(server);
            }

            logOfEvents.printEvent(currentTime, clients, queues);

            SwingUtilities.invokeLater(() -> queueEvolutionGUI.updateQueueEvolution(timeManager.getCurrentTime(), servers2, clients));

            int queueSizeSum = 0;
            for (BlockingQueue<Task> queue : queues) {
                queueSizeSum += queue.size();
            }
            if (queueSizeSum > maxQueueSizeSum) {
                maxQueueSizeSum = queueSizeSum;
                peakSecond = currentTime;
            }

            Iterator<Task> iterator = clientsCopy.iterator();
            while (iterator.hasNext()) {
                Task client = iterator.next();
                if (client.getArrivalTime() + client.getInitialServiceTime() <= currentTime) {
                    iterator.remove();
                }
            }

            try {
                timeManager.awaitNextSecond();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        int taskTotal = 0;
        int totalServiceTime = 0;
        for (Task client : clients) {
            if(client.getStartTime() != -1) {
                totalServiceTime += client.getInitialServiceTime();
                taskTotal++;
            }
        }
        double averageServiceTime = (double) totalServiceTime / taskTotal;

        double averageWaitingTime = computeAverageWaitingTime(clients);
        logOfEvents.printSimulationEnd(averageWaitingTime, peakSecond, averageServiceTime);
        queueEvolutionGUI.displayEndDetails(averageWaitingTime,peakSecond,averageServiceTime);

        running.set(false);
        executor.shutdown();
    }

    public double computeAverageWaitingTime(List<Task> completedTasks) {
        long totalWaitingTime = 0;
        int taskCount = 0;
        for (Task task : completedTasks) {
            if (task.getStartTime() != -1) {
                totalWaitingTime += (task.getStartTime() - task.getArrivalTime());
                taskCount++;
            }
            else if(task.getArrivalTime() <= simulationTimeInSeconds) {
                totalWaitingTime += (simulationTimeInSeconds - task.getArrivalTime());
                taskCount++;
            }
        }
        if (taskCount == 0) {
            return 0;
        }
        return (double) totalWaitingTime / taskCount;
    }
}