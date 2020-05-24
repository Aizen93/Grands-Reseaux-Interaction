package core;

import java.util.ArrayList;
import javafx.scene.paint.Color;
/**
 *
 * @author Oussama, Lilya
 */
public class Cluster {
    
    ArrayList<Integer> sommets;
    double somme_degre, nb_arete;
    Color color;

    public Cluster(){
        this.sommets = new ArrayList<>();
        somme_degre = 0;
        nb_arete = 0;
    }
    
    /**
     * Calculates sum of the edges degre of this cluster
     * @param graphe 
     */
    public void calcul_Som_Degre(Graphe graphe){
        for(int k : sommets) {
            somme_degre += graphe.getSommet(k).degre;
        }
    }
    
    /**
     * Calculates sum of the internal edges of this cluster
     * @param g graphe
     */
    public void calcul_nb_arete(Graphe g) {
        nb_arete = 0;
        if(sommets.size() != 1) {
           for (int k = 0; k < sommets.size()-1; k++) {
                for (int l = k+1; l < sommets.size(); l++) {
                    if(g.sommets.get(sommets.get(k)).contient(g.sommets.get(sommets.get(l)))) nb_arete++;
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
    
    public Color getColor(){
        return this.color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public int getNodeID(int i){
        return this.sommets.get(i);
    }
}
