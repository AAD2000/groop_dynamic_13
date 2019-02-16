package drtalgo;

import java.util.ArrayList;

public class Vehicle {

    City city;
    int capacity;
    int id;
    BusStop curstop;

    public Vehicle(City ct, int cap, int ind, BusStop stop){
        city = ct;
        capacity = cap;
        id = ind;
        curstop = stop;
    }

    double calculateProfit(ArrayList<Passenger> passengers){
        Trip trip = new Trip();
        trip.makeSimplestTrip(passengers, curstop);
        trip.simulateAnnealing();
        double wt = trip.getAverageWaitingTime(this);
        double length = trip.getTotalDistance();
    }

}
