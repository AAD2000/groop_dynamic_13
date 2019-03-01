package drtalgo;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class BetTree {

    /** fields **/
    // current bet of the vertex
    private Pair<Double,LinkedList<Passenger>> bet;
    // children of the vertex
    private ArrayList<BetTree> children;
    // parent of the vertex
    private BetTree parent;
    // all passengers that was in this vertex or in parents
    private HashSet<Passenger> usedPassengers;
    // field that shows if vertex has a child
    private boolean used;
    // field for dfs
    private boolean visited;
    // vehicle of current bet
    private Vehicle vehicle;


    /** constructors **/

    /**
     * Constructor for a normal bet
     * @param Bet bet
     * @param Parent parent
     * @param veh vehicle
     */
    public BetTree(Pair<Double,LinkedList<Passenger>> Bet, BetTree Parent, Vehicle veh){
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

    /**
     * Constructor for a zero bet. Used for a stable working of an algo
     * @param passenger this passenger
     * @param Parent parent
     */
    public BetTree(Passenger passenger, BetTree Parent){
        bet = new Pair<>(Double.valueOf(0), new LinkedList<>());
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

    /**
     * Method that check if current bet and another one are crossing
     * @param another_bet
     * @return
     */
    boolean isBetsCross(LinkedList<Passenger> another_bet){
        for(Passenger pass1: usedPassengers){
            for(Passenger pass2: another_bet){
                if(pass1 == pass2){
                    return true;
                }
            }
        }
        return false;
    }


    /** properties **/
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
    LinkedList<Passenger> getPassengers(){ return bet.getValue(); }
    Passenger getNotUsedPassenger() {return bet.getValue().get(0);}

    boolean isUsed(){
        return used;
    }
    boolean isVisited(){return visited;}
}
