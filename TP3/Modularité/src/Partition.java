import java.util.ArrayList;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    ArrayList<Cluster> partition;
    int modularité;
    
    public Partition(){
        this.partition = new ArrayList<>();
        this.modularité = 9999;
    }
    
    public void addCluster(Cluster c){
        this.partition.add(c);
    }
    
    /**
     * for debug
     * @return size 
     */
    public int size(){
        return partition.size();
    }
    
    /**
     * Calculate modularité of a graphe
     */
    public void Q(){
        
    }
}
