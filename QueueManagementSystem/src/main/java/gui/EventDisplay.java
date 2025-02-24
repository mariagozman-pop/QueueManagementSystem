package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.Server;
import models.Task;

public class EventDisplay extends JFrame implements Runnable {
    private final JTextArea queueTextArea;

    public EventDisplay() {
        setTitle("Events");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Real Time Display of Events");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Updated font size to 24
        add(titleLabel, BorderLayout.NORTH);

        queueTextArea = new JTextArea();
        queueTextArea.setEditable(false);
        queueTextArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Updated font size to 16
        JScrollPane scrollPane = new JScrollPane(queueTextArea);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 400); // Adjusted size to accommodate larger text
        setLocationRelativeTo(null);
    }

    public void updateQueueEvolution(int currentTime, List<Server> servers, List<Task> remainingTasks) {
        queueTextArea.setText("");
        queueTextArea.append("Current Time: " + currentTime + "\n");
        queueTextArea.append("Waiting clients: ");
        for (int i = 0; i < remainingTasks.size(); i++) {
            Task task = remainingTasks.get(i);
            if (task.getArrivalTime() > currentTime && task.getServiceTime() != 0) {
                queueTextArea.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
                if ((i + 1) % 10 == 0 && i != remainingTasks.size() - 1) {
                    queueTextArea.append("\n");
                } else {
                    queueTextArea.append("; ");
                }
            }
        }
        queueTextArea.append("\n");
        int serverIndex = 1;
        for (Server server : servers) {
            queueTextArea.append("Queue " + serverIndex + ": ");
            Task[] serverQueue = server.getTasks();
            if (serverQueue.length == 0) {
                queueTextArea.append("closed");
            } else {
                for (Task task : serverQueue) {
                    queueTextArea.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "),");
                }
            }
            queueTextArea.append("\n");
            serverIndex++;
        }
        queueTextArea.append("\n");
        setVisible(true);
    }

    public void displayEndDetails(double avgWaitingTime, int peakHour, double avgServiceTime) {
        queueTextArea.setText("");
        queueTextArea.append("Simulation ended.\n");
        queueTextArea.append("Average waiting time: " + avgWaitingTime + "\n");
        queueTextArea.append("Average service time: " + avgServiceTime + "\n");
        queueTextArea.append("Peak hour: " + peakHour + "\n");
    }

    @Override
    public void run() {
        setVisible(true);
    }
}
