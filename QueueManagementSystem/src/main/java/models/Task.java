package models;

public class Task {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;
    private long startTime;
    private final int initialServiceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.initialServiceTime = serviceTime;
        this.startTime = -1;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
    public int getInitialServiceTime(){ return initialServiceTime; }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }

    public void decrementServiceTime(){
        if(serviceTime>0){
            serviceTime--;
        }
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }
}

