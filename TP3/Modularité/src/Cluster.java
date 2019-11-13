import java.util.ArrayList;

/**
 *
 * @author Oussama, Lilya
 */
public class Cluster {
    
    ArrayList<Integer> sommets;
    
    public Cluster(){
        this.sommets = new ArrayList<>();
    }
    
    /**
     * Add a node to the cluster
     * @param sommet 
     */
    public void addSommet(int sommet){
        this.sommets.add(sommet);
    }
    
    
}
