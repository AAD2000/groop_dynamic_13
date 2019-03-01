package drtalgo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * class of Trip
 */
public class Trip {
    //constructor
    public Trip(){
        trip = new ArrayList<>();
        prefixDistances = new ArrayList<>();
    }

    /** fileds **/
    // trip is a list of a Elements which shows us a trip
    ArrayList<Element> trip;
    // array of distances from first stop
    ArrayList<Integer> prefixDistances;


    /***
     * Property of making start trip
     * @param passengers
     * @param startStop
     */
    void makeSimplestTrip(List<Passenger> passengers, BusStop startStop)
    {
        ArrayList<Element> elements = new ArrayList<>();
        int visitedint = passengers.size()*2 + 1;
        for(Passenger passenger: passengers){
            elements.add(passenger.startPoint);
            passenger.startPoint.order = visitedint;
            elements.add(passenger.endPoint);
            passenger.endPoint.order = visitedint;
        }
        Element location = new Element(startStop, "start point", true);
        trip.add(location);
        int used = 0;
        int usedToBe = 0;
        while(used != elements.size()){
            for(int i=0; i<elements.size(); i++) {
                Element temp = elements.get(i);
                if(!temp.getIsInfinity(trip.size()) && temp.order == visitedint){
                    temp.setOrder(trip.size());
                    trip.add(temp);
                    used++;
                    break;
                }
            }
            usedToBe++;
            if(usedToBe != used){
                throw new IllegalArgumentException("wrong clients!");
            }
        }

    }

    /**
     * Generating trip by annealing algorithm
     */
    void simulateAnnealing(){
        Random rand = new Random();
        int iteration = 0;
        double temperature = 1000;
        double oldDist = getTotalDistance();
        double newDist;
        while (iteration <= 1000){
            iteration ++;

            int it1, it2;
            Element el1, el2;
            do{
                it1 = rand.nextInt(trip.size()-1) + 1;
                it2 = rand.nextInt(trip.size()-1) + 1;
                el1 = trip.get(it1);
                el2 = trip.get(it2);
            }while (el1.getIsInfinity(it2) || el2.getIsInfinity(it1));
            trip.set(it1, el2);
            trip.set(it2, el1);
            newDist = getTotalDistance();
            if(newDist < oldDist){
                trip.get(it1).setOrder(it1);
                trip.get(it2).setOrder(it2);
                oldDist = newDist;
            }
            else {
                if(newDist != oldDist)
                    temperature *= 0.9;
                double flag = rand.nextDouble();
                double prob = Math.pow(Math.E,(oldDist - newDist)/temperature);
                if(newDist == oldDist)
                    prob = 0.5;
                if (flag < prob){
                    trip.get(it1).setOrder(it1);
                    trip.get(it2).setOrder(it2);
                    oldDist = newDist;
                }
                else {
                    trip.set(it1, el1);
                    trip.set(it2, el2);
                }
            }
        }
        setPrefixDistances();
    }

    /**
     * Method of creating simples
     * @param passengers
     * @param startStop
     */
    void createTrip(List<Passenger> passengers, BusStop startStop){
        makeSimplestTrip(passengers, startStop);
        simulateAnnealing();
    }

    /**
     * set field
     */
    void setPrefixDistances(){
        int dist = 0;
        prefixDistances.add(0);
        for (int i=1; i<trip.size(); i++){
            dist += trip.get(i).getDistance(trip.get(i-1));
            prefixDistances.add(dist);
        }
    }

    /**
     * property of total distance
     * @return total Distance of trip
     */
    double getTotalDistance(){
        double result = 0;
        for(int i=0; i<trip.size() - 1; i++) {
            result += trip.get(i).getDistance(trip.get(i+1));
        }
        return result;
    }

    /**
     * Propert of getting average waiting time for every passenger
     * @return waiting time
     */
    double getAverageWaitingTime(){
        double wt = 0;
        double n = 0;
        for(int i=0; i<trip.size(); i++){
            if(trip.get(i).pair != null && trip.get(i).isStartpoint){
                wt += prefixDistances.get(i);
                n++;
            }
        }
        return wt/n;
    }

    /**
     * Propert of getting reward
     * @return reward
     */
    double getReward(){
        double passenger_length = 0;
        for(int i=0; i<trip.size(); i++){
            if(trip.get(i).pair != null && trip.get(i).isStartpoint){
                passenger_length += (trip.get(i).getDistance(trip.get(i).pair))
                        * trip.get(i).passenger.priority;
            }
        }
        return passenger_length - getTotalDistance();
    }

    /**
     * Checking capacity throw the whole trip
     * @param capacity
     * @return
     */
    boolean checkCapacity(int capacity){
        int pass_in = 0;
        for(int i=0; i<trip.size(); i++){
            if(trip.get(i).pair != null && trip.get(i).isStartpoint){
                pass_in ++;
                if(pass_in > capacity){
                    return false;
                }
            }
        }
        return true;
    }
    void clearTrip(){
        trip.clear();
        prefixDistances.clear();
    }

    @Override
    public String toString(){
        if (trip.isEmpty())
            return "empty trip";
        String output=trip.get(0).name;
        for(int i=1;i<trip.size();i++) {
            output += " \n-{" + trip.get(i - 1).getDistance(trip.get(i)) + "}-> (" + trip.get(i).order + ")  " + trip.get(i).name + " " + trip.get(i).stop.toString();
            /*if(prefixDistances.size() != 0){
                output += " " + prefixDistances.get(i);
            }*/
        }
         output += "\nTOTAL DISTANCE: " + getTotalDistance();
        return output;
    }
}
