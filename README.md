# Queue Management System

## Description
The Queue Management System simulates clients arriving for service, entering queues, waiting, being served, and finally leaving the queues. The system computes key metrics such as average waiting time, average service time, and peak hours. It features a graphical user interface (GUI) that allows users to configure simulation parameters and visualize the queue dynamics in real time.

## Features
- **Simulation of Queues**: Simulate the arrival, queuing, service, and departure of clients.
- **Dynamic Queue Management**: Supports different queue management strategies.
- **Real-time Monitoring**: Visual representation of queue status and dynamics during the simulation.
- **Event Logging**: Logs events and statistics for analysis.
- **Intuitive GUI**: User-friendly interface for setting up and starting simulations.

## Usage
- Launch the application to access the main GUI.
- Input simulation parameters such as the number of clients, number of queues, simulation time, arrival time distribution, and service time distribution.
- Click the "Start Simulation" button to initiate the process.
- Monitor real-time updates of queue dynamics and performance metrics.

## Functional Requirements
- Simulate arrival, queuing, service, and departure of clients.
- Allow users to input simulation parameters.
- Dynamically manage queues based on selected strategies.
- Log events with timestamps for analysis.

## Non-functional Requirements
- Provide an intuitive user interface.
- Handle large loads efficiently.
- Ensure robustness and data integrity.
- Allow easy adjustment and expansion.
- Produce precise simulation results.

## Design Principles
- **Abstraction**: Encapsulates real-world concepts through classes like Task, Server, and Strategy.
- **Encapsulation**: Bundles data and behavior within well-defined class boundaries.
- **Polymorphism**: Supports interchangeable use of different queueing strategies via interfaces.
- **Modularity**: Organized into packages, separating distinct functionalities and promoting maintainability.

## Classes Overview
- **Task**: Represents a client task with attributes like arrival time and service time.
- **Server**: Simulates processing tasks and tracking waiting times.
- **TimeManager**: Manages the simulation time and controls time progression.
- **SimulationFrame**: GUI for configuring and starting the simulation.
- **EventDisplay**: Visual representation of queue dynamics during simulation.
- **LogOfEvents**: Handles logging of simulation events and statistics.
- **RandomTaskGenerator**: Generates random tasks for simulation variability.
- **SimulationManager**: Coordinates the simulation process and manages event logging.

## Implementation
The application is developed in Java, utilizing Java Swing for the GUI. The logic for managing queues and processing tasks is encapsulated in several classes that interact to simulate the queuing process.

### Thread Management
Thread management is a crucial aspect of the Queue Management System, enabling the concurrent processing of tasks and enhancing the overall performance of the simulation. The application employs multithreading to manage the execution of tasks across multiple servers, allowing for simultaneous handling of client requests.
Each server instance can operate on a separate thread, facilitating efficient service delivery and minimizing wait times for clients in the queue. The system leverages Java's built-in concurrency utilities to manage thread synchronization and communication, ensuring that data integrity is maintained while threads operate concurrently.

## Testing
Testing strategies include:
- Unit tests for individual components.
- Integration tests for system functionality.
- User acceptance testing to validate usability.
