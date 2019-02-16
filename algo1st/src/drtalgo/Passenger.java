package drtalgo;

public class Passenger {
    Element startPoint;
    Element endPoint;
    String name;

    public Passenger(Element start, Element end, String Name){
        startPoint = start;
        endPoint = end;
        name = Name;
    }

    @Override
    public String toString() {
        String output = "Passenger: " + name + "\n" + startPoint.toString() + "\n" + endPoint.toString();
        return output;
    }
}
