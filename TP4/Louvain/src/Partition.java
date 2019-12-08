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
        this.modularite = 0.0;
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
    
    public double sqr(double a){
        return a * a;
    }
    
    /**
     * Calculate modularité of a graph
     * Utilise les arretes externes c'est un peu different de la formule donnée en TP
     * @param graphe
     */
    public void Q(Graphe graphe){
        double m = graphe.nbr_arete;
        double Eii = 0.0, Aii = 0.0, Q = 0.0;
        for(int i = 0; i < partition.size(); i++){
            partition.get(i).calcul_nb_arete(graphe);
            
            Eii += partition.get(i).nb_arete / m;
            Aii += sqr(partition.get(i).somme_degre) / (4 * sqr(m));
            //Q += Eii - Aii;
        }
         modularite = Eii - Aii;
    }
    
    public ArrayList<Cluster> fusionner(int i, int j, Graphe graphe){
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);
        Cluster cluF = new Cluster();
        cluF.sommets.addAll(clu1.sommets);
        cluF.sommets.addAll(clu2.sommets);
        cluF.somme_degre = clu1.somme_degre + clu2.somme_degre;
        ArrayList<Cluster> tmp = new ArrayList<>();
        for(int k = 0; k < partition.size(); k++){
            if(k != i && k != j){
                tmp.add(partition.get(k));
            }
        }
        
        tmp.add(cluF);
        /*for(int k = 0; k < tmp.size(); k++){
            for(int l = 0; l < tmp.get(k).size(); l++){
                System.out.print(tmp.get(k).sommets.get(l)+ " ");
            }
            System.out.println("");
        }
        System.out.println("----------------------------");*/
        return tmp;
    }
    
    public double[] calculatePaire(Graphe graphe){
        Q(graphe);
        double moduInit = modularite;
        double increment = 0;
        ArrayList<Cluster> originalPartition = partition;
        int pair1 = -1, pair2 = -1;
        for(int i = 0; i < originalPartition.size(); i++){
            for(int j = i+1; j < originalPartition.size(); j++){
                partition = fusionner(i, j, graphe);
                Q(graphe);
                //System.out.println("i = "+ i + " j = "+ j + " modu = " + modularite + " increm = " + (modularite - moduInit));
                
                if(modularite - moduInit > increment){
                    pair1 = i; pair2 = j;
                    increment = modularite - moduInit;
                    partition = originalPartition;
                    //System.out.println("i = "+i + " j = "+j + " incre = "+increment);
                }else{
                    partition = originalPartition;
                }
            }
        }
        return new double[]{pair1, pair2, increment};
    }
}
