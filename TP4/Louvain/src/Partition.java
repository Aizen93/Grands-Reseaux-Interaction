import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    LinkedList<Cluster> partition;
    double modularite;
    LinkedList<LinkedList<Integer>> matrix_Mij;
    PriorityQueue<Paire> paires_modularite;
    
    public Partition(){
        this.partition = new LinkedList<>();
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
    
    public void fusionner(int i, int j, Graphe graphe){
        if(i < 0 || j < 0) return;
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);

        clu1.sommets.addAll(clu2.sommets);
        clu1.somme_degre = clu1.somme_degre + clu2.somme_degre;
        clu1.calcul_nb_arete(graphe);
        LinkedList<Cluster> tmp = new LinkedList<>();
        //partition.remove(j);
        for(int k = 0; k < partition.size(); k++){
            if(k != j)    tmp.add(partition.get(k));
        }
        partition = tmp;

        //Instant start = Instant.now();

        for (int k = 0; k < matrix_Mij.get(i).size(); k++) {
            matrix_Mij.get(i).set(k, matrix_Mij.get(i).get(k) + matrix_Mij.get(j).get(k));
        }

        /*for(int k = 0; k < matrix_Mij.get(i).size(); k++){
            if(k == i){
                matrix_Mij.get(i).set(k, 0);
            }else{
                matrix_Mij.get(i).set(k, matrix_Mij.get(i).get(k) + matrix_Mij.get(j).get(k));
            }
        }*/
        matrix_Mij.remove(j);
        for(int k = 0; k < matrix_Mij.size(); k++){
            matrix_Mij.get(k).remove(j);
        }
        //Instant finish = Instant.now();
        //long timeElapsed = Duration.between(start, finish).toMillis();
        //System.out.println("time : "+timeElapsed+" ms");
        /*for(int k = 0; k < matrix_Mij.size(); k++){
            for(int l = 0; l < matrix_Mij.size(); l++){
                System.out.print(matrix_Mij.get(k).get(l) + " ");
            }
            System.out.println("");
        }*/
        calculatePaireModularite(i, graphe);
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
            double modu_max = 0;
            int k = 0;
            Cluster clua = partition.get(i), club;
            double m = graphe.nbr_arete;
            for (int j = i+1; j < partition.size(); j++) {
                    club = partition.get(j);
                    double tmp = ((matrix_Mij.get(i).get(j) / m) - (sqr(clua.somme_degre + club.somme_degre) / (4 * sqr(m))))
                            + (sqr(clua.somme_degre) / (4 * sqr(m))) + (sqr(club.somme_degre) / (4 * sqr(m)));
                    if(tmp > modu_max) {
                        modu_max = tmp;
                        k = j;
                    }
            }
            paires_modularite.add(new Paire(partition.get(i), partition.get(k), modu_max));
            //System.out.println("DEBUG -- i : "+i+", j : "+k+", modularite_max : "+modu_max);
        }
    }

    public void calculatePaireModularite(int i, Graphe graphe) {
        double modu_max = 0;
        int k = 0;
        Cluster clua = partition.get(i), club;
        double m = graphe.nbr_arete;
        for (int j = 0; j < partition.size(); j++) {
            if(i != j){
                club = partition.get(j);
                double tmp = (matrix_Mij.get(i).get(j) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m)))
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                if(tmp > modu_max) {
                    modu_max = tmp;
                    k = j;
                }
            }
        }
        paires_modularite.add(new Paire(partition.get(i), partition.get(k), modu_max));
        System.out.println("DEBUG -- i : "+i+", j : "+k+", modularite_max : "+modu_max);
    }
    
    //Version optimisé
    public double[] calculatePaire(Graphe graphe){

        double m = graphe.nbr_arete;
        double increment = 0;
        LinkedList<Cluster> originalPartition = partition;
        int pair1 = -1, pair2 = -1;
        Paire p = paires_modularite.poll();
        //System.out.println("DEBUG-- Paire : i = "+partition.indexOf(p.i) + " j = "+partition.indexOf(p.j) + " imodul = "+p.modularite);
        if(p.modularite > increment){
            pair1 = partition.indexOf(p.i); pair2 = partition.indexOf(p.j);
            increment = p.modularite;
            partition = originalPartition;
            //System.out.println("i = "+i + " j = "+j + " incre = "+increment);
        }else{
            partition = originalPartition;
        }
        return new double[]{pair1, pair2, increment};
    }

    public void calculateLouvain(String cluster_path, Graphe graphe){
        for (Sommet som : graphe.sommets) {
            if(som.adjacence != null){
                Cluster clu = new Cluster();
                clu.sommets.add(som.ID);
                clu.somme_degre = som.degre;
                this.partition.add(clu);
            }
        }
        //System.out.println("size = "+partition.size() + " last = " + partition.get(partition.size()-1).sommets.get(0));
        initPaireModularite(graphe);
        double[] res;
        //System.out.println("DEBUG - res : "+ res[0] + " ; "+res[1]);
        while(!paires_modularite.isEmpty()){
            res = calculatePaire(graphe);
            //System.out.println("DEBUG - res : "+ res[0] + " ; "+res[1] + " ; increment : "+ res[2]);
            fusionner((int)res[0], (int)res[1], graphe);
            //if(paires_modularite.isEmpty() && res[0] != -1) initPaireModularite(graphe);
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

}