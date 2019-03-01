package drtalgo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class City {

    /** fields **/
    // all stops of the city
    private ArrayList<BusStop> stops;
    // all passengers thar were delivered or delivering at the moment
    private ArrayList<Passenger> used_passengers;
    // all passengers that are waiting for vehicle
    private ArrayList<Passenger> nottaken_passengers;
    // all vehicles of the city
    private ArrayList<Vehicle> vehicles;

    /**
     * Constructor
     */
    public City(){
        stops = new ArrayList<>();
        used_passengers = new ArrayList<>();
        nottaken_passengers = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

    /** properties**/

    ArrayList<BusStop> getStops(){return stops;}
    ArrayList<Passenger> getNTPassengers(){return nottaken_passengers;}
    ArrayList<Vehicle> getVehicles() {return vehicles;}

    void addStop(BusStop stop){
        stops.add(stop);
    }
    void addPassenger(Passenger passenger) {nottaken_passengers.add(passenger);}
    void addVehicle(Vehicle vehicle) {vehicles.add(vehicle);}

    boolean isEmpty(){
        if(vehicles.isEmpty() && stops.isEmpty()){
            return true;
        }
        return false;
    }

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

    /**
     * Setting one passenger to used
     * @param pass passenger to change
     */
    void pickUpPassenger(Passenger pass){
        nottaken_passengers.remove(pass);
        used_passengers.add(pass);
    }

    /**
     * Precondition of distances
     */
    void countDistances(){
        for(BusStop s: stops){
            s.countDistances(this);
        }
    }

    /**
     * Method of making bets of evety vehicle
     * @return Map with bets from every vehicle and its profit
     */
    private HashMap<Vehicle, Pair<Double,LinkedList<Passenger>>> makeBets(){
        HashMap<Vehicle, Pair<Double,LinkedList<Passenger>>> bets = new HashMap<>();
        double min_dist;
        int index;
        for(Vehicle vehicle: vehicles){
            min_dist = Double.MAX_VALUE;
            index = -1;
            for(int i=0; i<nottaken_passengers.size(); i++){
                double t=vehicle.getCurstop().getDistance(nottaken_passengers.get(i).startPoint);
                if(t < min_dist){
                    min_dist = t;
                    index = i;
                }
            }
            bets.put(vehicle, vehicle.makeSetOfPassengers(index));
        }
        return bets;
    }

    /***
     * Method of creating bet tree
     * @return root of the tree
     */
    private BetTree makeBetTree(){
        HashMap<Vehicle, Pair<Double,LinkedList<Passenger>>> bets = makeBets();

        LinkedList<BetTree> leaves = new LinkedList<>();
        ArrayList<BetTree> bets_to_remove = new ArrayList<>();
        BetTree root = new BetTree(null, null, null);
        leaves.add(root);
        for (Passenger passenger: nottaken_passengers) {
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

    /**
     * Choosing winners from bet tree
     * @return general profit and vehicles who won the auction
     */
    Pair<Double,ArrayList<Vehicle>> chooseWorkingVehicles(){
        if(isEmpty()){
            return new Pair<>((double) 0, null);
        }
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
