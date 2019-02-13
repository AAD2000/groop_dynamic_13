import java.util.HashSet;

public class City {
    HashSet<BusStop> stops;
    public City(){
        stops = new HashSet<>();
    }

    void addCity(BusStop stop){
        stops.add(stop);
    }

    void makeRoad(BusStop s1, BusStop s2, double dist){
        s1.addNeighbour(s2, dist);
        s2.addNeighbour(s1, dist);
    }
}
