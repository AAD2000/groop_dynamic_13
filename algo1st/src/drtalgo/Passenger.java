package drtalgo;

public class Passenger {
    /** fields **/
    // start point
    Element startPoint;
    // end point
    Element endPoint;
    // name of the passenger
    String name;
    // priority for drivers
    double priority = 1.1;

    /**
     * Constructor
     * @param start starting bus stop
     * @param end ending bus stop
     * @param Name name
     */
    public Passenger(BusStop start, BusStop end, String Name){
        startPoint = new Element(start, Name + "_start", true);
        endPoint = new Element(end, Name + "_end", false, startPoint);
        startPoint.setPassenger(this);
        endPoint.setPassenger(this);
        name = Name;
    }

    /**
     * Propert of increasing priority
     * @param p coefficient
     */
    void increasePriority(double p){priority *= p;}

    @Override
    public String toString() {
        String output = "Passenger: " + name;//+ "\n" + startPoint.toString() + "\n" + endPoint.toString();
        return output;
    }

}
