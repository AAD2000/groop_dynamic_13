package drtalgo;

import javafx.util.Pair;

import java.util.*;

public class Vehicle {

    /** fields **/
    // city where vehicle is
    private City city;
    // the amount of passengers that vehicle can accommodate
    private int capacity;
    // id number of vehicle
    private int id;
    // Current Bus Stop of the vehicle
    private BusStop curstop;
    // passengers that are waiting for this vehicle or are in this vehicle
    private LinkedList<Passenger> passengers;
    // fiels for algo
    private LinkedList<Passenger> possible_passengers;
    // current trip of the vehicle
    private Trip trip;
    // trip for algo
    private Trip trip_back;

    /** constructor **/
    /**
     * Constructor
     * @param ct city
     * @param cap capacity
     * @param ind id of the vehicle
     * @param stop bus stop
     */
    public Vehicle(City ct, int cap, int ind, BusStop stop){
        city = ct;
        capacity = cap;
        id = ind;
        curstop = stop;
        passengers = new LinkedList<>();
        possible_passengers = new LinkedList<>();
        trip = new Trip();
        trip_back = new Trip();
    }

    /** properties **/
    BusStop getCurstop() {return curstop;}

    /**
     * Property of adding new passengers to this vehicle
     * @param pass_collection
     */
    void addPassengers(List<Passenger> pass_collection){

        for(Passenger pass: pass_collection){
            if(!passengers.contains(pass)){
                passengers.add(pass);
                city.pickUpPassenger(pass);
            }
        }

        setTrip();
    }

    /**
     * Joining current and possible passengers
     * @param ex_passengers new possible passengers
     * @return
     */
    private LinkedList<Passenger> getAllPossiblePassengers(LinkedList<Passenger> ex_passengers){
        possible_passengers.clear();
        possible_passengers.addAll(passengers);
        possible_passengers.addAll(ex_passengers);
        return possible_passengers;
    }

    /**
     * Property of making trip
     */
    private void setTrip(){
        trip.createTrip(passengers, curstop);
    }

    /**
     * Calculating profit trip on set of passengers
     * @param passengers set of passengers
     * @return
     */
    private Double calculateProfit(List<Passenger> passengers){
        trip_back.clearTrip();
        trip_back.createTrip(passengers, curstop);
        double wt = trip_back.getAverageWaitingTime();
        double reward = trip_back.getReward()- 1.5 * wt;
        if(!trip_back.checkCapacity(capacity)){
            reward = Double.MIN_VALUE;
        }
        return reward;
    }

    /**
     * Method of making set of passenger that is the most profitable
     * @param start
     * @return
     */
    Pair<Double, LinkedList<Passenger>> makeSetOfPassengers(int start){
        ArrayList<Boolean> visited = new ArrayList<>();
        for(int i=0; i<city.getNTPassengers().size(); i++){
            visited.add(false);
        }
        visited.set(start, true);
        LinkedList<Passenger> result = new LinkedList<>();
        ArrayList<Integer> added = new ArrayList<>();
        result.add(city.getNTPassengers().get(start));
        int iteration = 0;
        double temperature = 100;

        Random rand = new Random();

        double oldProfit = calculateProfit(getAllPossiblePassengers(result));

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
                how_many_add = rand.nextInt(city.getNTPassengers().size()- n);
                for(int i=0; i<how_many_add; i++) {
                    do {
                        it = rand.nextInt(city.getNTPassengers().size());
                    } while (visited.get(it));
                    result.add(city.getNTPassengers().get(it));
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
            newProfit = calculateProfit(getAllPossiblePassengers(result));
            double flag = rand.nextDouble();
            double prob = Math.pow(Math.E,(newProfit - oldProfit)/temperature);
            if((oldProfit < newProfit) || (flag < prob)){
                if(rem_or_add < prob_of_add){
                    n += how_many_add;
                }
                else {
                    for(int i=0; i<city.getNTPassengers().size(); i++){
                        if(city.getNTPassengers().get(i) == tempPass){
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
            output+= "\t" + pass.name + "\n";
        }
        output += "Trip:\n" + trip.toString();
        return output;
    }
}
