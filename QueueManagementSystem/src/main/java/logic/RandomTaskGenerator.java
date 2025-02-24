package logic;

import models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTaskGenerator {
    private final int numClients;
    private final int minArrivalTime;
    private final int maxArrivalTime;
    private final int minServiceTime;
    private final int maxServiceTime;

    public RandomTaskGenerator(int numClients, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.numClients = numClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
    }

    public List<Task> generateClients() {
        List<Task> clients = new ArrayList<>();
        Random random = new Random();

        for (int id = 1; id <= numClients; id++) {
            int arrivalTime = minArrivalTime + random.nextInt(maxArrivalTime - minArrivalTime + 1);
            int serviceTime = minServiceTime + random.nextInt(maxServiceTime - minServiceTime + 1);

            Task client = new Task(id, arrivalTime, serviceTime);
            clients.add(client);
        }

        return clients;
    }
}

