package drtalgo;
import java.util.HashSet;

public class City {
    // all stops of the city
    HashSet<BusStop> stops;
    HashSet<Passenger> passengers;
    HashSet<Vehicle> vehicles;

    /**
     * Constructor
     */
    public City(){

        stops = new HashSet<>();
        passengers = new HashSet<>();
        vehicles = new HashSet<>();
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
}
