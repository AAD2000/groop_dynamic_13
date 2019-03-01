package drtalgo;
import java.util.HashMap;

public class BusStop {
    /** fields **/
    String name;

    // all neighboring stops
    private HashMap <BusStop, Double> neighbours;
    // distance to all stops
    private HashMap <BusStop, Double> distances;
    // how to get to any stop
    private HashMap <BusStop, BusStop> path;

    /***
     * Constructor with initialising all fields
     * @param name
     */
    public BusStop(String name){
        this.name = name;
        neighbours = new HashMap<>();
        distances = new HashMap<>();
        path = new HashMap<>();
    }

    /**
     * Property of adding neighbour to BusStop
     * @param anotherStop
     * @param dist
     */
    void addNeighbour(BusStop anotherStop, double dist){
        neighbours.put(anotherStop, dist);
    }

    // TO TEST!

    /**
     * Counting distances to all of the stops
     */
    void countDistances(City city){
        HashMap<BusStop, Boolean> visited = new HashMap<>();
        int visitedStops = 0;
        for (BusStop s:city.getStops()) {
            distances.put(s, Double.MAX_VALUE);
            path.put(s, null);
            visited.put(s, false);
        }
        distances.replace(this, Double.valueOf(0));
        int size = distances.size();
        while(visitedStops != size){
            BusStop v = null;
            for (BusStop s:distances.keySet()){
                if(!visited.get(s)){
                    if(v == null){
                        v = s;
                    }
                    else if (distances.get(s) < distances.get(v)){
                        v = s;
                    }
                }
            }
            visited.replace(v, Boolean.valueOf(true));
            visitedStops++;
            for(BusStop s:v.neighbours.keySet()){
                if(!visited.get(s)){
                    if(distances.get(s) > distances.get(v) + v.neighbours.get(s)){
                        distances.replace(s, distances.get(v) + v.neighbours.get(s));
                        path.replace(s, v);
                    }
                }
            }
        }
    }

    /**
     * Property of getting distance to another stop
     * @param anotherStop
     * @return
     */
    Double getDistance(BusStop anotherStop){
        return distances.get(anotherStop);
    }

    Double getDistance(Element e){
        return distances.get(e.stop);
    }

    @Override
    public String toString() {
        return name;
    }
}
