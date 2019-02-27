package drtalgo;

import javafx.util.Pair;

import java.util.*;

public class Vehicle {

    private City city;
    private int capacity;
    private int id;
    BusStop curstop;
    private LinkedList<Passenger> passengers;
    private Trip trip;

    public Vehicle(City ct, int cap, int ind, BusStop stop){
        city = ct;
        capacity = cap;
        id = ind;
        curstop = stop;
        passengers = new LinkedList<>();
        trip = new Trip();
    }

    void addPassengers(List<Passenger> pass_collection){
        for(Passenger pass: pass_collection){
            if(!passengers.contains(pass)){
                passengers.add(pass);
            }
        }
        setTrip();
    }

    void setTrip(){
        trip.createTrip(passengers, curstop);
    }

    Double calculateProfit(List<Passenger> passengers){
        Trip trip = new Trip();
        trip.createTrip(passengers, curstop);
        double wt = trip.getAverageWaitingTime();
        double reward = trip.getReward()- 1.5 * wt;
        return reward;
    }


    // TO TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Pair<Double, ArrayList<Passenger>> makeSetOfPassengers(int start){
        ArrayList<Boolean> visited = new ArrayList<>();
        for(int i=0; i<city.passengers.size(); i++){
            visited.add(false);
        }
        visited.set(start, true);
        ArrayList<Passenger> result = new ArrayList<>();
        ArrayList<Integer> added = new ArrayList<>();
        result.add(city.passengers.get(start));
        int iteration = 0;
        double temperature = 100;

        Random rand = new Random();

        double oldProfit = calculateProfit(result);

        double newProfit;
        double rem_or_add;
        int how_many_add = 0;
        int n = 1;
        double prob_of_add = 0.7;
        while (iteration < 1000){
            iteration++;
            int it;
            rem_or_add = rand.nextDouble();
            Passenger tempPass = null;
            if(rem_or_add < prob_of_add || n == 1) {
                how_many_add = rand.nextInt(city.passengers.size()- n);
                for(int i=0; i<how_many_add; i++) {
                    do {
                        it = rand.nextInt(city.passengers.size());
                    } while (visited.get(it));
                    result.add(city.passengers.get(it));
                    visited.set(it, true);
                    added.add(it);
                }
                rem_or_add = 0;
            }
            else{
                it = rand.nextInt(result.size());
                tempPass = result.get(it);
                result.remove(it);
            }
            newProfit = calculateProfit(result);
            double flag = rand.nextDouble();
            double prob = Math.pow(Math.E,(newProfit - oldProfit)/temperature);
            if((oldProfit < newProfit) || (flag < prob)){
                if(rem_or_add < prob_of_add){
                    n += how_many_add;
                }
                else {
                    for(int i=0; i<city.passengers.size(); i++){
                        if(city.passengers.get(i) == tempPass){
                            visited.set(i, false);
                            break;
                        }
                    }
                    n--;
                }
                oldProfit = newProfit;
                if(flag < prob){
                    temperature *= 0.9;
                }

            }
            else {
                if(rem_or_add < prob_of_add){
                    for(int i=0; i<added.size(); i++){
                        visited.set(added.get(i), false);
                        result.remove(result.size()-1);
                    }
                }
                else {
                        result.add(tempPass);
                }

            }
            added.clear();
        }
        return new Pair<>(oldProfit, result);
    }

    @Override
    public String toString() {
        String output = "Vehicle" + id + "\nPassengers:\n";
        for(Passenger pass: passengers){
            output+=pass.name + "\n";
        }
        //output += "Trip:\n" + trip.toString();
        return output;
    }
}
