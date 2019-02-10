import java.util.Random;

/**
 * class of element
 */
class Element {
    /////////////////////////////////////////////////////////////////////////////
    /** constructors **/

    /**
     * Constructor with initialising all fields
     *
     * @param indexX       X coord
     * @param indexY       Y coord
     * @param Name         name+id of the client
     * @param IsStartpoint
     * @param Pair         second pair
     */
    public Element(
            int indexX, int indexY,
            String Name,
            boolean IsStartpoint,
            Element Pair
    ) {
        IndexX = indexX;
        IndexY = indexY;
        name = Name;
        isVisited = false;

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

    public Element(int indexX, int indexY, String Name, boolean IsStartpoint) {
        IndexX = indexX;
        IndexY = indexY;
        name = Name;
        isStartpoint = IsStartpoint;
        pair = null;
        isVisited = false;
    }

    /////////////////////////////////////////////////////////////////////////////
    /** fields **/

    //coords of elem
    int IndexX;
    int IndexY;

    //Place in queue
    int order;

    //name+id of elem
    String name;

    //check is this visited
    boolean isVisited;

    //check is this startpoint
    boolean isStartpoint;

    //pair of the element, if this is start point then pair is end point
    Element pair;

    /////////////////////////////////////////////////////////////////////////////
    /** properties **/

    /**
     * Property for checking, could we count destination
     *
     * @return true if we haven't visited start point
     */
    boolean getIsInfinity() {
        if (isStartpoint)
            return false;
        else {
            if (pair.isVisited)
                return false;
            else
                return true;
        }
    }

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
     * property of destination from pair
     *
     * @return double destination
     */
    double getDestination() {
        return getDestination(pair.IndexX, pair.IndexY);
    }

    /**
     * property of destination from your elem
     *
     * @return double destination
     */
    double getDestination(Element pair) {
        if (getIsInfinity())
            throw new IllegalArgumentException("start point haven't visited");

        return getDestination(pair.IndexX, pair.IndexY);
    }

    /**
     * property of destination from your coord
     *
     * @return
     */
    double getDestination(int pairIndexX, int pairIndexY) {
        return Math.sqrt(
                (pairIndexX - IndexX) * (pairIndexX - IndexX) +
                        (pairIndexY - IndexY) * (pairIndexY - IndexY)
        );
    }

    /////////////////////////////////////////////////////////////////////////////
    /** methods **/

    void setVisitedTrue() {
        isVisited = true;
    }

    @Override
    public String toString() {
        String output = "Name: " + name + "\n\tX: " + IndexX + "\n\tY: " + IndexY + "\n\t";
        if (!isStartpoint)
            output += "Start point: ";
        else
            output += "End point: ";
        output += pair.name + "\n";
        return output;
    }
}
