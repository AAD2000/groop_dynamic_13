package drtalgo;
import java.util.ArrayList;

 /**
 * test class
 */
public class main {

    //starting programm

    public static void main(String[] args) {

        // making City

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

        ArrayList<Passenger> passengers = new ArrayList<>();
        ArrayList<Passenger> passengers1 = new ArrayList<>();
        ArrayList<Passenger> passengers2 = new ArrayList<>();
        ArrayList<Passenger> passengers3 = new ArrayList<>();

        Element s1= new Element(stop1, "s1", true);
        Element e1= new Element(stop2, "e1", false, s1);

        Element s2= new Element(stop3, "s2", true);
        Element e2= new Element(stop5, "e2", false, s2);

        Element s3= new Element(stop1, "s3", true);
        Element e3= new Element(stop5, "e3", false, s3);

        Element s4= new Element(stop4, "s4", true);
        Element e4= new Element(stop5, "e4", false, s4);

        Passenger pass1 = new Passenger(s1, e1, "Passenger1");
        Passenger pass2 = new Passenger(s2, e2, "Passenger2");
        Passenger pass3 = new Passenger(s3, e3, "Passenger3");
        Passenger pass4 = new Passenger(s4, e4, "Passenger4");

        city.addPassenger(pass1);
        city.addPassenger(pass2);
        city.addPassenger(pass3);
        city.addPassenger(pass4);

        s1.setPassenger(pass1);
        s2.setPassenger(pass2);
        s3.setPassenger(pass3);
        s4.setPassenger(pass4);
        e1.setPassenger(pass1);
        e2.setPassenger(pass2);
        e3.setPassenger(pass3);
        e4.setPassenger(pass4);

        passengers.add(pass1);
        passengers.add(pass2);
        passengers.add(pass3);
        passengers.add(pass4);

        passengers1.add(pass1);
        passengers1.add(pass2);

        passengers2.add(pass1);
        passengers2.add(pass3);
        passengers2.add(pass4);

        passengers3.add(pass2);
        passengers3.add(pass3);
        passengers3.add(pass4);

        for (Passenger p: passengers) {
            System.out.println(p.toString());
        }


        System.out.println("******************");
        Vehicle vehicle1 = new Vehicle(city, 20, 0, stop1);

       /* ArrayList<Passenger> arr1 = vehicle1.makeSetOfPassengers(0);
        for (Passenger pass: arr1) {
            System.out.println(pass.name);
        }*/
/*        System.out.println(vehicle1.calculateProfit(passengers));
        System.out.println(vehicle1.calculateProfit(passengers1));
        System.out.println(vehicle1.calculateProfit(passengers2));
        System.out.println(vehicle1.calculateProfit(passengers3));*/


    }

}

