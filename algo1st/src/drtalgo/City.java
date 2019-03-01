package drtalgo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class City {
    // all stops of the city
    private ArrayList<BusStop> stops;
    private ArrayList<Passenger> passengers;
    private ArrayList<Vehicle> vehicles;

    /**
     * Constructor
     */
    public City(){
        stops = new ArrayList<>();
        passengers = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

    ArrayList<BusStop> getStops(){return stops;}
    ArrayList<Passenger> getPassengers(){return passengers;}
    ArrayList<Vehicle> getVehicles() {return vehicles;}

    /**
     * Property of adding stop to the city
     * @param stop
     */
    void addStop(BusStop stop){
        stops.add(stop);
    }

    void addPassenger(Passenger passenger) {passengers.add(passenger);}

    void addVehicle(Vehicle vehicle) {vehicles.add(vehicle);}

    /**
     * Property of connecting 2 stops
     * @param s1
     * @param s2
     * @param dist
     */
    void addRoad(BusStop s1, BusStop s2, double dist){
        s1.addNeighbour(s2, dist);
        s2.addNeighbour(s1, dist);
    }

    void countDistances(){
        for(BusStop s: stops){
            s.countDistances(this);
        }
    }

    HashMap<Vehicle, Pair<Double,ArrayList<Passenger>>> makeBets(){
        HashMap<Vehicle, Pair<Double,ArrayList<Passenger>>> bets = new HashMap<>();
        double min_dist;
        int index;
        for(Vehicle vehicle: vehicles){
            min_dist = Double.MAX_VALUE;
            index = -1;
            for(int i=0; i<passengers.size(); i++){
                double t=vehicle.getCurstop().getDistance(passengers.get(i).startPoint);
                if(t < min_dist){
                    min_dist = t;
                    index = i;
                }
            }
            bets.put(vehicle, vehicle.makeSetOfPassengers(index));
        }
        return bets;
    }

    BetTree makeBetTree(){
        HashMap<Vehicle, Pair<Double,ArrayList<Passenger>>> bets = makeBets();

        LinkedList<BetTree> leaves = new LinkedList<>();
        ArrayList<BetTree> bets_to_remove = new ArrayList<>();
        BetTree root = new BetTree(null, null, null);
        leaves.add(root);
        for (Passenger passenger: passengers) {
            for (Vehicle vehicle : vehicles) {
                if (bets.get(vehicle).getValue().contains(passenger)) {
                    for (BetTree leaf : leaves) {
                        if (!leaf.isBetsCross(bets.get(vehicle).getValue())) {
                            leaf.addChildren(new BetTree(bets.get(vehicle), leaf, vehicle));
                        }
                    }
                }
            }
            for (BetTree leaf : leaves) {
                if (!leaf.containsPassengerInSubtree(passenger)) {
                    leaf.addChildren(new BetTree(passenger, leaf));
                }
            }

            
            for (BetTree leaf: leaves){
                if(leaf.isUsed()){
                   bets_to_remove.add(leaf);
                }
            }
            for (BetTree leaf: bets_to_remove){
                leaves.remove(leaf);
                leaves.addAll(leaf.getChildren());
            }
            bets_to_remove.clear();
        }
        return root;
    }

    Pair<Double,ArrayList<Vehicle>> chooseWorkingVehicles(){
        BetTree root = makeBetTree();
        ArrayList<Vehicle> result = new ArrayList<>();
        double resulting_profit = Double.MIN_VALUE;
        double temp_profit = 0;

        Stack<BetTree> stack = new Stack<>();
        BetTree result_leaf = null;
        stack.push(root);
        while(!stack.empty()){
            BetTree temp = stack.peek();
            if(temp.getVehicle() != null && !temp.isVisited()){
                temp_profit += temp.getProfit();
            }
            temp.setVisitedTrue();

            if(temp.getChildren().isEmpty()){
                if(temp_profit > resulting_profit){
                    resulting_profit = temp_profit;
                    result_leaf = temp;
                }
                stack.pop();
                if(temp.getVehicle() != null){
                    temp_profit -= temp.getProfit();
                }
            }
            else{
                int k = 0;
                for (BetTree child: temp.getChildren()){
                    if(!child.isVisited()){
                        stack.push(child);
                        break;
                    }
                    k++;
                }
                if(k==temp.getChildren().size()){
                    stack.pop();
                    if(temp.getVehicle() != null){
                        temp_profit -= temp.getProfit();
                    }
                }
            }

        }
        while (result_leaf.getParent() != null){
            if(result_leaf.getVehicle() != null) {
                result.add(result_leaf.getVehicle());
                result_leaf.getVehicle().addPassengers(result_leaf.getPassengers());
            }
            else{
                result_leaf.getNotUsedPassenger().increasePriority(1.1);
            }
            result_leaf = result_leaf.getParent();
        }
        return new Pair<>(resulting_profit, result);
    }


}
