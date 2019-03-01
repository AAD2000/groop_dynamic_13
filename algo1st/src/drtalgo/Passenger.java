package drtalgo;

public class Passenger {
    Element startPoint;
    Element endPoint;
    String name;
    double priority = 1.1;

    public Passenger(BusStop start, BusStop end, String Name){
        startPoint = new Element(start, Name + "_start", true);
        endPoint = new Element(end, Name + "_end", false, startPoint);
        startPoint.setPassenger(this);
        endPoint.setPassenger(this);
        name = Name;
    }

    void increasePriority(double p){priority *= p;}

    @Override
    public String toString() {
        String output = "Passenger: " + name;//+ "\n" + startPoint.toString() + "\n" + endPoint.toString();
        return output;
    }

}
