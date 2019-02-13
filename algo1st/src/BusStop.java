import java.util.HashMap;
import java.util.Map;

public class BusStop {
    String name;
    HashMap <BusStop, Double> neighbours;
    HashMap <BusStop, Double> distances;
    HashMap <BusStop, BusStop> path;
    public BusStop(String name){
        this.name = name;
        neighbours = new HashMap<>();
        distances = new HashMap<>();
        path = new HashMap<>();
    }
    void addNeighbour(BusStop anotherStop, double dist){
        neighbours.put(anotherStop, dist);
    }

    // TO TEST!
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
    Double getDistance(BusStop anotherStop){
        return distances.get(anotherStop);
    }

}
