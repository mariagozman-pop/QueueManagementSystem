package logic;

import models.Task;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LogOfEvents {
    private final String logFilePath;

    public LogOfEvents(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void printEvent(int currentTime, List<Task> waitingClients, BlockingQueue<Task>[] queues) {
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write("Time " + currentTime + "\n");

            writer.write("Waiting clients: ");
            for (Task task : waitingClients) {
                if (task.getArrivalTime() > currentTime && task.getServiceTime() != 0) { // Include only clients with arrival time > current time
                    writer.write("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "); ");
                }
            }
            writer.write("\n");

            for (int i = 0; i < queues.length; i++) {
                BlockingQueue<Task> queue = queues[i];
                writer.write("Queue " + (i + 1) + ": ");
                if (queue.isEmpty()) {
                    writer.write("closed\n");
                } else {
                    synchronized (queue) {
                        for (Task task : queue) {
                            writer.write("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "); ");
                        }
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printSimulationEnd(double averageWaitingTime, int peakHour, double averageServiceTime) {
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write("Average waiting time: " + averageWaitingTime + "\n");
            writer.write("Average service time: " + averageServiceTime + "\n");
            writer.write("Peak hour: " + peakHour + "\n");
            writer.write("Simulation ended.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}