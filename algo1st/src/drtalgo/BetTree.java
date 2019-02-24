package drtalgo;

import javafx.util.Pair;
import java.util.ArrayList;

public class BetTree {

    private Pair<Double,ArrayList<Passenger>> bet;
    private ArrayList<BetTree> children;
    private BetTree parent;
    private ArrayList<Passenger> usedPassengers;
    private boolean used;

    public BetTree(Pair<Double,ArrayList<Passenger>> Bet, BetTree Parent){
        bet = Bet;
        children = new ArrayList<>();
        parent = Parent;
        used = false;
        if(parent != null){
            usedPassengers.addAll(parent.usedPassengers);
        }
        if(bet != null){
            usedPassengers.addAll(bet.getValue());
        }
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

    BetTree addChildren(BetTree child){
        children.add(child);
        setUsedTrue();
        return child;
    }

    boolean isUsed(){
        return used;
    }
}
