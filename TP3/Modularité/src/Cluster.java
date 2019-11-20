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
    
    public int size(){
        return sommets.size();
    }
    /**
     * Add a node to the cluster
     * @param sommet 
     */
    public void addSommet(int sommet){
        this.sommets.add(sommet);
    }
    
    
}
