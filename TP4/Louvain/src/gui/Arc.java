package gui;

import core.Graphe;
import core.Sommet;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Oussama
 */
public class Arc {
    public HashMap<Sommet, HashSet<Integer>> arcs;
    
    public Arc(Graphe graphe){
        this.arcs = new HashMap<>();
        /*for(Sommet som : graphe.getListSommets()){
            this.arcs.put(som, new HashSet<>());
        }*/
    }
    /**
     * O(2) Complexity 
     * @param A
     * @param B
     * @return 
     */
    public boolean isExist(Sommet A, int B){
        if(!arcs.get(A).isEmpty()){
            return arcs.get(A).contains(B);
        }else{
            return false;
        }
    }
    
    
    public void addArc(Sommet A, int adj){
        this.arcs.get(A).add(adj);
    }
    
}
