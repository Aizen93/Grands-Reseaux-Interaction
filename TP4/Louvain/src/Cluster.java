import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Oussama, Lilya
 */
public class Cluster {
    
    ArrayList<Integer> sommets;
    double somme_degre, nb_arete;

    public Cluster(){
        this.sommets = new ArrayList<>();
        somme_degre = 0;
        nb_arete = 0;
    }

    public void calcul_nb_arête(Graphe g) {
        if(sommets.size() == 1) {
            nb_arete = 0;
        }else{
            HashSet<Integer> visit = new HashSet<>();
            for(int k : this.sommets) {
                visit.add(k);
                for (int l: g.getSommet(k).adjacence) {
                    if(this.sommets.contains(l) && !visit.contains(l)) {
                        nb_arete++;
                    }
                }
            }
        }
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
