package gui;

import logic.*;
import models.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationFrame extends JFrame {
    private final JTextField simulationTimeField;
    private final JTextField numQueuesField;
    private final JTextField numClientsField;
    private final JTextField minArrivalTimeField;
    private final JTextField maxArrivalTimeField;
    private final JTextField maxServiceTimeField;
    private final JTextField minServiceTimeField;
    private final JComboBox<String> strategyComboBox;

    public SimulationFrame(EventDisplay queueEvolutionGUI) {
        setTitle("Simulation Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Number of Clients:"), gbc);
        gbc.gridx = 1;
        numClientsField = new JTextField(10);
        panel.add(numClientsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Number of Queues:"), gbc);
        gbc.gridx = 1;
        numQueuesField = new JTextField(10);
        panel.add(numQueuesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Simulation Time:"), gbc);
        gbc.gridx = 1;
        simulationTimeField = new JTextField(10);
        panel.add(simulationTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Min Arrival Time:"), gbc);
        gbc.gridx = 1;
        minArrivalTimeField = new JTextField(10);
        panel.add(minArrivalTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Max Arrival Time:"), gbc);
        gbc.gridx = 1;
        maxArrivalTimeField = new JTextField(10);
        panel.add(maxArrivalTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Min Processing Time:"), gbc);
        gbc.gridx = 1;
        maxServiceTimeField = new JTextField(10);
        panel.add(maxServiceTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Max Processing Time:"), gbc);
        gbc.gridx = 1;
        minServiceTimeField = new JTextField(10);
        panel.add(minServiceTimeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Selection Policy:"), gbc);
        gbc.gridx = 1;
        String[] strategies = {"Shortest Queue Strategy", "Shortest Time Strategy"};
        strategyComboBox = new JComboBox<>(strategies);
        panel.add(strategyComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton startButton = new JButton("Start Simulation");
        startButton.setBackground(Color.GREEN);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation(queueEvolutionGUI);
            }
        });
        panel.add(startButton, gbc);
        getContentPane().add(panel);

        Dimension dim = new Dimension(400, 400);
        setPreferredSize(dim);
        setSize(dim);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startSimulation(EventDisplay queueEvolutionGUI) {
        if (numClientsField.getText().isEmpty() || numQueuesField.getText().isEmpty() ||
                simulationTimeField.getText().isEmpty() || minArrivalTimeField.getText().isEmpty() ||
                maxArrivalTimeField.getText().isEmpty() || minServiceTimeField.getText().isEmpty() ||
                maxServiceTimeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int simulationTimeInSeconds = Integer.parseInt(simulationTimeField.getText());
        int numQueues = Integer.parseInt(numQueuesField.getText());
        int numClients = Integer.parseInt(numClientsField.getText());
        int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
        int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
        int minServiceTime = Integer.parseInt(maxServiceTimeField.getText());
        int maxServiceTime = Integer.parseInt(minServiceTimeField.getText());
        String selectedStrategy = (String) strategyComboBox.getSelectedItem();
        Strategy strategy;
        assert selectedStrategy != null;
        if (selectedStrategy.equals("Shortest Queue Strategy")) {
            strategy = new ShortestQueueStrategy();
        } else {
            strategy = new ShortestTimeStrategy();
        }
        if(minArrivalTime > maxArrivalTime || minServiceTime > maxServiceTime || simulationTimeInSeconds < maxArrivalTime) {
            JOptionPane.showMessageDialog(this, "Please provide valid input data!", "Invalid data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        RandomTaskGenerator clientGenerator = new RandomTaskGenerator(
                numClients, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        List<Task> clients = clientGenerator.generateClients();
        clients.sort(Comparator.comparingInt(Task::getArrivalTime));

        BlockingQueue<Task>[] queues = new LinkedBlockingQueue[numQueues];
        for (int i = 0; i < numQueues; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }

        SimulationManager simulationManager = new SimulationManager(numQueues, simulationTimeInSeconds, queues, clients, strategy, queueEvolutionGUI);
        Thread simulationManagerThread = new Thread(simulationManager);
        simulationManagerThread.start();

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EventDisplay queueEvolutionGUI = new EventDisplay();
            SwingUtilities.invokeLater(queueEvolutionGUI);
            new SimulationFrame(queueEvolutionGUI);
        });
    }
}