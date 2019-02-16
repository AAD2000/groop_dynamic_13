package drtalgo;
import java.util.ArrayList;

 /**
 * test class
 */
public class main {

    //starting programm

    public static void main(String[] args) {
        BusStop stop1 = new BusStop("Stop1");
        BusStop stop2 = new BusStop("Stop2");
        BusStop stop3 = new BusStop("Stop3");
        BusStop stop4 = new BusStop("Stop4");
        BusStop stop5 = new BusStop("Stop5");
        City city = new City();

        city.addStop(stop1);
        city.addStop(stop2);
        city.addStop(stop3);
        city.addStop(stop4);
        city.addStop(stop5);

        city.addRoad(stop1, stop2, 5);
        city.addRoad(stop2, stop4, 8);
        city.addRoad(stop1, stop3, 6);
        city.addRoad(stop3, stop4, 7);
        city.addRoad(stop4, stop5, 9);

        city.countDistances();

        ArrayList<Element> graph = new ArrayList<>();

        Element s1= new Element(stop1, "s1", true);
        Element e1= new Element(stop2, "e1", false, s1);

        Element s2= new Element(stop3, "s2", true);
        Element e2= new Element(stop5, "e2", false, s2);

        Element s3= new Element(stop1, "s3", true);
        Element e3= new Element(stop5, "e3", false, s3);

        Element s4= new Element(stop4, "s4", true);
        Element e4= new Element(stop5, "e4", false, s4);

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
        tr.makeSimplestTrip(graph, stop1);
        System.out.println(tr.toString());
        tr.simulateAnnealing();
        System.out.println("\n***AFTER ALGORITHM***\n");
        System.out.println(tr.toString());

    }

}

