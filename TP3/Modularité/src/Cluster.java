import java.util.ArrayList;

/**
 *
 * @author Oussama, Lilya
 */
public class Cluster {
    
    ArrayList<Sommet> sommets;
    int size;
    
    public Cluster(){
        this.sommets = new ArrayList<>();
        this.size = 0;
    }
    
    /**
     * Add a node to the cluster
     * @param sommet 
     */
    public void addSommet(Sommet sommet){
        this.sommets.add(sommet);
        size++;
    }
    
    
}
