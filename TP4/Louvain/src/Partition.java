import java.util.ArrayList;
import java.util.HashSet;

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
    
    public double[] m(int i, Graphe g){
        double eii = 0;
        double aii = 0;
        Cluster cluster = partition.get(i);
        HashSet<Integer> visite = new HashSet<>();
        for(int k : cluster.sommets) {
            aii += g.getSommet(k).degre;
            visite.add(k);
            for (int l: g.getSommet(k).adjacence) {
                if(cluster.sommets.contains(l) && !visite.contains(l)) {
                    eii++;
                }
            }
        }

        /*for(int k = 0; k < cluster1.size(); k++){
            Sommet som1 = g.sommets.get(cluster1.sommets.get(k));
            for(int l = 0; l < cluster2.size(); l++){
                Sommet som2 = g.sommets.get(cluster2.sommets.get(l));
                aij += (2*(som1.degre * som2.degre));
                if(som2.contient(som1)){
                    eij++;
               }
            }
        }*/
        //System.out.println("m("+i+" "+j+") = "+ arrete);
        return (new double[]{eii, sqr(aii)});
    }
    
    /**
     * Calculate modularité of a graph
     * Utilise les arretes externes c'est un peu different de la formule donnée en TP
     */
    public void Q(Graphe graphe){
        double m = graphe.nbr_arete;
        double Eii = 0;
        double Aii = 0;
        for(int i = 0; i < partition.size(); i++){
            partition.get(i).calcul_nb_arête(graphe);
            if(partition.get(i).size() == 1) {
                Aii += sqr(graphe.getSommet(partition.get(i).sommets.get(0)).degre)/(4 * sqr(m));
            }else{
                Eii += partition.get(i).nb_arete / m;
                Aii += sqr(partition.get(i).somme_degre) / (4 * sqr(m));
            }
        }
        modularite = Eii - Aii;
    }
}
