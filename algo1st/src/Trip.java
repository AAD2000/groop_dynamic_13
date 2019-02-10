import java.util.ArrayList;
import java.util.Random;

/**
 * class of Trip
 */
public class Trip {
    //constructor
    public Trip(){
        trip= new ArrayList<Element>();
    }

    //trip is a list of a Elements which shows us a trip
    ArrayList<Element> trip;

    // algo block!

    /***
     * Property of making start trip
     * @param elements
     * @param x
     * @param y
     */
    void makeSimplestTrip(ArrayList<Element> elements, int x, int y)
    {
        Element location = new Element(x, y, "start point", true);
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
            else if(newDist == oldDist){
                double flag = rand.nextDouble();
                double prob = 0.5;
                if (flag < prob){
                    trip.get(it1).setOrder(it1);
                    trip.get(it2).setOrder(it2);
                }
                else {
                    trip.set(it1, el1);
                    trip.set(it2, el2);
                }
            }
            else {
                temperature *= 0.9;
                double flag = rand.nextDouble();
                double prob = Math.pow(Math.E,(oldDist - newDist)/temperature);
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
            result += trip.get(i).getDestination(trip.get(i+1));
        }
        return result;
    }

    @Override
    public String toString(){
        if (trip.isEmpty())
            return "empty trip";
        String output=trip.get(0).name;
        for(int i=1;i<trip.size();i++)
            output+=" \n-{"+trip.get(i-1).getDestination(trip.get(i).IndexX, trip.get(i).IndexY)+"}-> ("+trip.get(i).order+")  "+trip.get(i).name;
        output += "\nTOTAL DISTANCE: " + getTotalDistance();
        return output;
    }
}
