import java.util.HashMap;
import java.util.Map;

public class BusStop {
    /** fields **/
    String name;

    // all neighboring stops
    HashMap <BusStop, Double> neighbours;
    // distance to all stops
    HashMap <BusStop, Double> distances;
    // how to get to any stop
    HashMap <BusStop, BusStop> path;

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
    void countDistances(){
        HashMap<BusStop, Boolean> visited = new HashMap<>();
        distances.put(this, Double.valueOf(0));
        path.put(this, null);
        visited.put(this, false);
        int visitedStops = 0;
        for (BusStop s:neighbours.keySet()) {
            distances.put(s, Double.MAX_VALUE);
            path.put(s, null);
            visited.put(s, false);
        }
        int size = distances.size();
        while(visitedStops != size){
            BusStop v = null;
            for (BusStop s:distances.keySet()){
                if(!visited.get(s)){
                    if(v.equals(null)){
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
                    if(distances.get(s) > distances.get(v) + v.getDistance(s)){
                        distances.replace(s, distances.get(v) + v.getDistance(s));
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

}
