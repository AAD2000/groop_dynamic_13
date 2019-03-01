package drtalgo;
import javafx.util.Pair;

import java.util.ArrayList;

 /**
 * test class
 */
public class main {

    //starting programm

    public static void main(String[] args) {

        // making City

        CityFactory cityFactory = new CityFactory();


        cityFactory.makeCityExample1();

        City city = cityFactory.getCity();

        Pair<Double,ArrayList<Vehicle>> result = city.chooseWorkingVehicles();

        System.out.println("PROFIT: "+ result.getKey());
        for(Vehicle vehicle: city.getVehicles()){
            System.out.println(vehicle.toString());
        }

        System.out.println("******************");



    }

}

