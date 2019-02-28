package drtalgo;

/**
 * Class of Element
 * It can be start or end point of a passenger
 */
class Element {
    /////////////////////////////////////////////////////////////////////////////
    /** constructors **/

    /**
     * Constructor with initialising all fields
     *
     * @param Stop       BusStop
     * @param Name         name+id of the client
     * @param IsStartpoint
     * @param Pair         second pair
     */
    public Element(
            BusStop Stop,
            String Name,
            boolean IsStartpoint,
            Element Pair
    ) {
        stop = Stop;
        name = Name;

        if (IsStartpoint != Pair.isStartpoint)
            isStartpoint = IsStartpoint;
        else
            throw new IllegalArgumentException(
                    "Pair and this have equals fields isStartpoint "
            );
        if (Pair.pair == null) {
            pair = Pair;
            Pair.pair = this;
        } else {
            if (Pair.pair == this)
                pair = Pair;
            else
                throw new IllegalArgumentException("Pair's element isn't this");
        }
    }

    public Element(BusStop Stop, String Name, boolean IsStartpoint) {
        stop = Stop;
        name = Name;
        isStartpoint = IsStartpoint;
        pair = null;
    }

    /////////////////////////////////////////////////////////////////////////////
    /** fields **/

    // Current BusStop
    BusStop stop;

    //Place in queue
    int order;

    //name+id of elem
    String name;


    //check is this startpoint
    boolean isStartpoint;

    //pair of the element, if this is start point then pair is end point
    Element pair;

    Passenger passenger;

    /////////////////////////////////////////////////////////////////////////////
    /** properties **/

    void setPassenger(Passenger pass){passenger = pass;}
    /**
     * check if we can put this element in that place of done trip
     * @param ord
     * @return
     */
    boolean getIsInfinity(int ord) {
        if (isStartpoint) {
            if(pair.order > ord)
                return false;
            else
                return true;
        }
        else {
            if (pair.order < ord)
                return false;
            else
                return true;
        }
    }

    /***
     * property of setting order
     * @param ord
     */
    void setOrder(int ord){
        order = ord;
    }

    /**
     * property of destination from your elem
     *
     * @return double destination
     */
    double getDistance(Element pair) {
        return getDistance(pair.stop);
    }

    double getDistance(BusStop anotherStop){
        return stop.getDistance(anotherStop);
    }


    /////////////////////////////////////////////////////////////////////////////
    /** methods **/

    @Override
    public String toString() {
        String output = "Name: " + name + "\n\tBusStop: " + stop.toString() + "\n\t";
        if (!isStartpoint)
            output += "Start point: ";
        else
            output += "End point: ";
        output += pair.name + "\n";
        return output;
    }
}
