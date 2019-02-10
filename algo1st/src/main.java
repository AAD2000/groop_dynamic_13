 import java.util.ArrayList;
 import java.util.Random;

 /**
 * test class
 */
public class main {

    //starting programm

    public static void main(String[] args) {
        int NowPointX=0;
        int NowPointY=0;
        ArrayList<Element> graph= new ArrayList<Element>();
        Element s1= new Element(1, 0, "s1", true);
        Element e1= new Element(15, 15, "e1", false, s1);

        Element s2= new Element(4, 3, "s2", true);
        Element e2= new Element(6, 5, "e2", false, s2);

        Element s3= new Element(5, 6, "s3", true);
        Element e3= new Element(3, 8, "e3", false, s3);

        Element s4= new Element(10, 4, "s4", true);
        Element e4= new Element(7, 5, "e4", false, s4);

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
        //make(tr,  graph,NowPointX ,NowPointY );
        tr.makeSimplestTrip(graph, NowPointX, NowPointY);
        System.out.println(tr.toString());
        tr.simulateAnnealing();
        System.out.println("\n***AFTER ALGORITHM***\n");
        System.out.println(tr.toString());

    }
    public static void make(Trip trip, ArrayList<Element> elements,int x,int y) {
        boolean NONSTOP=true;
        Element e=new Element(x, y, "driver", true);
        while(NONSTOP){
            Element el=null;
            for(int i=0;i<elements.size();i++){
                if(!elements.get(i).getIsInfinity() && !elements.get(i).isVisited) {
                    if(el==null)
                        el=elements.get(i);
                    else {
                        if(elements.get(i).getDestination(e.IndexX,e.IndexY)<
                                el.getDestination(e.IndexX,e.IndexY)) {
                            el=elements.get(i);
                        }

                    }
                }
            }
            el.setVisitedTrue();
            trip.trip.add(el);
            NONSTOP=false;
            for(int i=0;i<elements.size();i++)
                if(!elements.get(i).isVisited)
                    NONSTOP=true;
        }
    }

}

