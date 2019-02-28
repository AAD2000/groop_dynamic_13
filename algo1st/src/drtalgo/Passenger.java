package drtalgo;

public class Passenger {
    Element startPoint;
    Element endPoint;
    String name;
    double priority = 1.1;

    public Passenger(Element start, Element end, String Name){
        startPoint = start;
        endPoint = end;
        name = Name;
    }

    void increasePriority(double p){priority *= p;}

    @Override
    public String toString() {
        String output = "Passenger: " + name;//+ "\n" + startPoint.toString() + "\n" + endPoint.toString();
        return output;
    }

}
