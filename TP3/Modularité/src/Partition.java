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
    
    public double[] m(int i, int j, Graphe g){
        double eij = 0;
        double aij = 0;
        Cluster cluster1 = partition.get(i);
        Cluster cluster2 = partition.get(j);
        for(int k = 0; k < cluster1.size(); k++){
            Sommet som1 = g.sommets.get(cluster1.sommets.get(k));
            for(int l = 0; l < cluster2.size(); l++){
                Sommet som2 = g.sommets.get(cluster2.sommets.get(l));
                aij += (2*(som1.degre * som2.degre));
                if(som2.contient(som1)){
                    eij++;
               }
            }
        }
        //System.out.println("m("+i+" "+j+") = "+ arrete);
        return (new double[]{eij, aij});
    }
    
    /**
     * Calculate modularité of a graph
     * Utilise les arretes externes c'est un peu different de la formule donnée en TP
     */
    public void Q(Graphe graphe){
        double m = graphe.nbr_arete;
        double Eij = 0;
        double Aij = 0;
        for(int i = 0; i < partition.size(); i++){
            for(int j = i+1; j < partition.size(); j++){
                double[] tmp = m(i, j, graphe);
                Eij += tmp[0] / m;
                Aij += tmp[1] / (4 * sqr(m));
            }
        }
        modularite = Aij - Eij;
    }
}
