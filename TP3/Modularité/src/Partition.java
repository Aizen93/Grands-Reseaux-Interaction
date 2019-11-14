import java.util.ArrayList;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    ArrayList<Cluster> partition;
    double modularite;
    
    public Partition(){
        this.partition = new ArrayList<>();
        this.modularite = 9999.0;
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
    
    public int[] m(int i, int j, Graphe graphe){
        int eij = 0;
        int aij = 0;
        Cluster cluster1 = partition.get(i);
        Cluster cluster2 = partition.get(j);
        
        for(int k = 0; k < cluster1.size; k++){
            for(int l = 0; l < cluster2.size; l++){
                Sommet som1 = cluster1.sommets.get(k);
                Sommet som2 = cluster2.sommets.get(l);
                aij += (2*(som1.degre * som2.degre));
                if(som2.contient(som1)){
                   eij++;
               }
            }
        }
        //System.out.println("m("+i+" "+j+") = "+ arrete);
        return (new int[]{eij, aij});
    }
    
    /**
     * Calculate modularité of a graph
     */
    public void Q(Graphe graphe){
        double m = graphe.nbr_arete;
        double Eij = 0;
        double Aij = 0;
        for(int i = 0; i < partition.size(); i++){
            for(int j = i+1; j < partition.size(); j++){
                int[] tmp = m(i, j, graphe);
                Eij += (tmp[0] / m);
                Aij += tmp[1] / (4 * Math.pow(m, 2));
            }
        }
        
        //Eij = Eij / graphe.nbr_arete;
        System.out.println("Eij = "+Eij);
        //Aij = (Aij*Aij) / (4 * sqr(graphe.nbr_arete));
        System.out.println("Aij = "+Aij);
        
        modularite = Aij - Eij;
        System.out.println("-----------------------------------");
        System.out.println("| Modularité = " + modularite);
        System.out.println("-----------------------------------");
        
    }
}
