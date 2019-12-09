import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    ArrayList<Cluster> partition;
    double modularite;
    int[][] matrix_Mij;
    PriorityQueue<Paire> paires_modularite;
    
    public Partition(){
        this.partition = new ArrayList<>();
        this.modularite = 0.0;
        paires_modularite = new PriorityQueue<>();
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
        if(i < 0 || j < 0) return partition;
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);
        clu1.sommets.addAll(clu2.sommets);
        clu1.somme_degre = clu1.somme_degre + clu2.somme_degre;
        ArrayList<Cluster> tmp = new ArrayList<>();
        for(int k = 0; k < partition.size(); k++){
            if(k != j)    tmp.add(partition.get(k));
        }
        paires_modularite.removeIf((Paire p) ->
                (p.i).equals(clu1) || (p.i).equals(clu2) || (p.j).equals(clu1) || (p.j).equals(clu2)
        );

        for(int k = 0; k < partition.size(); k++) calculatePaireModularite(clu1, partition.get(k), graphe);
        return tmp;
    }
    
    public double m(Cluster cluster1, Cluster cluster2, Graphe g){
        double eij = 0;
        for(int k = 0; k < cluster1.size(); k++){
            Sommet som1 = g.sommets.get(cluster1.sommets.get(k));
            for(int l = 0; l < cluster2.size(); l++){
                Sommet som2 = g.sommets.get(cluster2.sommets.get(l));
                if(som2.contient(som1)){
                    eij++;
               }
            }
        }
        //System.out.println("m("+i+" "+j+") = "+ arrete);
        return eij;
    }

    public void initPaireModularite(Graphe graphe) {
        for(int i = 0; i < partition.size(); i++) {
            for (int j = i + 1; j < partition.size(); j++) {
                calculatePaireModularite(partition.get(i),partition.get(j),graphe);
            }
        }
    }

    public void calculatePaireModularite(Cluster i, Cluster j, Graphe graphe) {
        double m = graphe.nbr_arete;
        double modul = (m(i, j, graphe) / m) - (sqr(i.somme_degre + j.somme_degre)/(4*sqr(m)))
                + (sqr(i.somme_degre)/(4*sqr(m))) + (sqr(j.somme_degre)/(4*sqr(m)));
        if(!i.equals(j)) paires_modularite.add(new Paire(i, j, modul));
    }
    
    //Version optimisé
    public double[] calculatePaire(Graphe graphe){
        double m = graphe.nbr_arete;
        double increment = 0, increment2 = 0;
        ArrayList<Cluster> originalPartition = partition;
        int pair1 = -1, pair2 = -1;
        Paire p = paires_modularite.poll();
        if(p.modularite > increment){
            pair1 = partition.indexOf(p.i); pair2 = partition.indexOf(p.j);
            increment = p.modularite;
            partition = originalPartition;
            //System.out.println("i = "+i + " j = "+j + " incre = "+increment);
        }else{
            partition = originalPartition;
        }
        /*for(int i = 0; i < originalPartition.size(); i++){
            for(int j = i+1; j < originalPartition.size(); j++){
                //System.out.println("i = "+ i + " j = "+ j + " modu = " + modularite + " increm = " + (modularite - moduInit));
                Cluster clua = originalPartition.get(i);
                Cluster club = originalPartition.get(j);
                increment2 = (m(i, j, graphe) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m))) 
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                //increment2 = (double)Math.round(increment2 * 1000d) / 1000d;
                //System.out.println("incre = "+ increment2);
                if(increment2 > increment){
                    pair1 = i; pair2 = j;
                    increment = increment2;
                    partition = originalPartition;
                    //System.out.println("i = "+i + " j = "+j + " incre = "+increment);
                }else{
                    partition = originalPartition;
                }
            }
        }*/
        return new double[]{pair1, pair2, increment};
    }

    public void calculateLouvain(String cluster_path, Graphe graphe){
        for (Sommet som : graphe.sommets) {
            Cluster clu = new Cluster();
            clu.sommets.add(som.ID);
            clu.somme_degre = som.degre;
            this.partition.add(clu);
        }
        initPaireModularite(graphe);
        double[] res = calculatePaire(graphe);
        //System.out.println("DEBUG - res : "+ res[0] + " ; "+res[1]);
        while(res[0] != -1 ){
            //System.out.println("DEBUG - res : "+ res[0] + " ; "+res[1] + " ; increment : "+ res[2]);
            this.partition = fusionner((int)res[0], (int)res[1], graphe);
            res = calculatePaire(graphe);
        }
        /*for (Cluster c: partition) {
            if(c.sommets.size() > 1) {
                for (int i: c.sommets) {
                    System.out.print(i+" ");
                }
                System.out.println();
            }
        }*/
        Q(graphe);
        System.out.println("Meilleure modularité : " + modularite);
    }

    
    public double[] calculatePaire2(Graphe graphe){
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