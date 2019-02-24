package drtalgo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class City {
    // all stops of the city
    ArrayList<BusStop> stops;
    ArrayList<Passenger> passengers;
    ArrayList<Vehicle> vehicles;

    /**
     * Constructor
     */
    public City(){
        stops = new ArrayList<>();
        passengers = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

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
                double t=vehicle.curstop.getDistance(passengers.get(i).startPoint);
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
        LinkedList<BetTree> leafs = new LinkedList<>();
        BetTree root = new BetTree(null, null);
        leafs.add(root);
        for (Passenger passenger: passengers) {
            for(Vehicle vehicle: vehicles){
                if(bets.get(vehicle).getValue().contains(passenger)){
                    for (BetTree leaf:leafs){
                        if(!leaf.isBetsCross(bets.get(vehicle).getValue())){
                            leaf.addChildren(new BetTree(bets.get(vehicle), leaf));
                        }
                    }
                }
            }
            for (BetTree leaf: leafs){
                if(leaf.isUsed()){
                    leafs.remove(leaf);
                }
            }
        }
        return root;
    }


}
