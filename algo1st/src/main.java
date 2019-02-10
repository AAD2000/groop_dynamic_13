 import java.util.ArrayList;
 import java.util.Random;

 /**
 * test class
 */
public class main {

    //starting programm

    public static void main(String[] args) {
        int NowPointX=0;
        int NowPointY=0;
        ArrayList<Element> graph= new ArrayList<Element>();
        Element s1= new Element(1, 0, "s1", true);
        Element e1= new Element(15, 15, "e1", false, s1);

        Element s2= new Element(4, 3, "s2", true);
        Element e2= new Element(6, 5, "e2", false, s2);

        Element s3= new Element(5, 6, "s3", true);
        Element e3= new Element(3, 8, "e3", false, s3);

        Element s4= new Element(10, 4, "s4", true);
        Element e4= new Element(7, 5, "e4", false, s4);

        graph.add(s1);
        graph.add(e1);

        graph.add(s2);
        graph.add(e2);

        graph.add(s3);
        graph.add(e3);

        graph.add(s4);
        graph.add(e4);

        for (Element e: graph) {
            System.out.println(e.toString());
        }
        Trip tr= new Trip();
        //make(tr,  graph,NowPointX ,NowPointY );
        tr.makeSimplestTrip(graph, NowPointX, NowPointY);
        System.out.println(tr.toString());
        tr.simulateAnnealing();
        System.out.println("\n***AFTER ALGORITHM***\n");
        System.out.println(tr.toString());

    }
    public static void make(Trip trip, ArrayList<Element> elements,int x,int y) {
        boolean NONSTOP=true;
        Element e=new Element(x, y, "driver", true);
        while(NONSTOP){
            Element el=null;
            for(int i=0;i<elements.size();i++){
                if(!elements.get(i).getIsInfinity() && !elements.get(i).isVisited) {
                    if(el==null)
                        el=elements.get(i);
                    else {
                        if(elements.get(i).getDestination(e.IndexX,e.IndexY)<
                                el.getDestination(e.IndexX,e.IndexY)) {
                            el=elements.get(i);
                        }

                    }
                }
            }
            el.setVisitedTrue();
            trip.trip.add(el);
            NONSTOP=false;
            for(int i=0;i<elements.size();i++)
                if(!elements.get(i).isVisited)
                    NONSTOP=true;
        }
    }

}

/**
 * class of Trip
 */
class Trip{
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

