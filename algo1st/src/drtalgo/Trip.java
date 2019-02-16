package drtalgo;
import java.util.ArrayList;
import java.util.Random;

/**
 * class of Trip
 */
public class Trip {
    //constructor
    public Trip(){
        trip = new ArrayList<>();
    }

    //trip is a list of a Elements which shows us a trip
    ArrayList<Element> trip;

    // algo block!

    /***
     * Property of making start trip
     * @param passengers
     * @param startStop
     */
    void makeSimplestTrip(ArrayList<Passenger> passengers, BusStop startStop)
    {
        ArrayList<Element> elements = new ArrayList<>();
        for(Passenger passenger: passengers){
            elements.add(passenger.startPoint);
            elements.add(passenger.endPoint);
        }
        Element location = new Element(startStop, "start point", true);
        trip.add(location);
        int used = 0;
        int usedToBe = 0;
        while(used != elements.size()){
            for(int i=0; i<elements.size(); i++) {
                Element temp = elements.get(i);
                if(!temp.getIsInfinity() && !temp.isVisited){
                    trip.add(temp);
                    temp.setOrder(trip.size()-1);
                    used++;
                    temp.setVisitedTrue();
                    break;
                }
            }
            usedToBe++;
            if(usedToBe != used){
                throw new IllegalArgumentException("wrong clients!");
            }
        }
    }

    void simulateAnnealing(){
        Random rand = new Random();
        int iteration = 0;
        double temperature = 1000;

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
            double oldDist = getTotalDistance();
            trip.set(it1, el2);
            trip.set(it2, el1);
            double newDist = getTotalDistance();
            if(newDist < oldDist){
                trip.get(it1).setOrder(it1);
                trip.get(it2).setOrder(it2);
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
                }
                else {
                    trip.set(it1, el1);
                    trip.set(it2, el2);
                }
            }
        }
    }

    /**
     *
     * @return total Distance of trip
     */
    double getTotalDistance(){
        double result = 0;
        for(int i=0; i<trip.size() - 1; i++) {
            result += trip.get(i).getDistance(trip.get(i+1));
        }
        return result;
    }

    double getAverageWaitingTime(Vehicle car){
        double wt = 0;
        double n = 0;
        for(int i=0; i<trip.size(); i++){
            if(trip.get(i).pair != null && trip.get(i).isStartpoint){
                wt += getWaitingTime(trip.get(i), car);
            }
        }
        return wt/n;
    }

    double getWaitingTime(Element e, Vehicle car){
        if(!e.isStartpoint){
            return -1;
        }
        return e.getDistance(car.curstop)/50;
    }

    double getReward(){

    }

    @Override
    public String toString(){
        if (trip.isEmpty())
            return "empty trip";
        String output=trip.get(0).name;
        for(int i=1;i<trip.size();i++)
            output+=" \n-{"+trip.get(i-1).getDistance(trip.get(i))+"}-> ("+trip.get(i).order+")  "+trip.get(i).name + " " + trip.get(i).stop.toString();
        output += "\nTOTAL DISTANCE: " + getTotalDistance();
        return output;
    }
}
