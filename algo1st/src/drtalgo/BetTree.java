package drtalgo;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;

public class BetTree {

    private Pair<Double,ArrayList<Passenger>> bet;
    private ArrayList<BetTree> children;
    private BetTree parent;
    private HashSet<Passenger> usedPassengers;
    private boolean used;
    private boolean visited;
    private Vehicle vehicle;

    public BetTree(Pair<Double,ArrayList<Passenger>> Bet, BetTree Parent, Vehicle veh){
        bet = Bet;
        children = new ArrayList<>();
        parent = Parent;
        used = false;
        visited = false;
        vehicle = veh;
        usedPassengers = new HashSet<>();
        if(parent != null){
            usedPassengers.addAll(parent.usedPassengers);
        }
        if(bet != null){
            usedPassengers.addAll(bet.getValue());
        }
    }

    public BetTree(Passenger passenger, BetTree Parent){
        bet = new Pair<>(Double.valueOf(0), new ArrayList<>());
        bet.getValue().add(passenger);
        children = new ArrayList<>();
        parent = Parent;
        used = false;
        visited = false;
        vehicle = null;
        usedPassengers = new HashSet<>();
        if(parent != null){
            usedPassengers.addAll(parent.usedPassengers);
        }
        usedPassengers.add(passenger);

    }

    boolean isBetsCross(ArrayList<Passenger> another_bet){
        for(Passenger pass1: usedPassengers){
            for(Passenger pass2: another_bet){
                if(pass1 == pass2){
                    return true;
                }
            }
        }
        return false;
    }



    void setUsedTrue(){
        used = true;
    }
    void setVisitedTrue(){
        visited = true;
    }

    BetTree addChildren(BetTree child){
        children.add(child);
        setUsedTrue();
        return child;
    }

    ArrayList<BetTree> getChildren(){ return children; }
    Vehicle getVehicle(){ return  vehicle; }
    Double getProfit(){ return bet.getKey();}
    BetTree getParent(){return parent;}
    boolean containsPassengerInSubtree(Passenger passenger){
        return usedPassengers.contains(passenger);
    }
    ArrayList<Passenger> getPassengers(){ return bet.getValue(); }
    Passenger getNotUsedPassenger() {return bet.getValue().get(0);}

    boolean isUsed(){
        return used;
    }
    boolean isVisited(){return visited;}
}
