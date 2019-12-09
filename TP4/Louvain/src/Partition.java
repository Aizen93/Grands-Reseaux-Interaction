import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    ArrayList<Cluster> partition;
    double modularite;
    ArrayList<ArrayList<Integer>> matrix_Mij;
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
    
    public void fusionner(int i, int j, Graphe graphe){
        if(i < 0 || j < 0) return;
        ArrayList<Paire> recalcul = new ArrayList<>();
        for (Paire paire: paires_modularite) {
            if(partition.get(i).equals(paire.j) || partition.get(j).equals(paire.j)){
                recalcul.add(paire);
            }
        }
        if(i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);
        /*paires_modularite.removeIf( (Paire p ) ->
                (clu1.equals(p.j)) || clu2.equals(p.j));*/

        clu1.sommets.addAll(clu2.sommets);
        clu1.somme_degre = clu1.somme_degre + clu2.somme_degre;
        //clu1.calcul_nb_arete(graphe);
        ArrayList<Cluster> tmp = new ArrayList<>();
        //partition.remove(j);
        for(int k = 0; k < partition.size(); k++){
            if(k != j)    tmp.add(partition.get(k));
        }
        partition = tmp;

        /*
        for (int k = 0; k < matrix_Mij.get(i).size(); k++) {
            matrix_Mij.get(i).set(k, matrix_Mij.get(i).get(k) + matrix_Mij.get(j).get(k));
        }
*/
        for(int k = 0; k < matrix_Mij.get(i).size(); k++){
            if(k == i){
                matrix_Mij.get(i).set(k, 0);
            }else{
                matrix_Mij.get(i).set(k, matrix_Mij.get(i).get(k) + matrix_Mij.get(j).get(k));
            }
        }
        matrix_Mij.remove(j);
        for(int k = 0; k < matrix_Mij.size(); k++){
            matrix_Mij.get(k).remove(j);
        }


        /*for(int k = 0; k < matrix_Mij.size(); k++){
            for(int l = 0; l < matrix_Mij.size(); l++){
                System.out.print(matrix_Mij.get(k).get(l) + " ");
            }
            System.out.println("");
        }*/
        for (Paire paire:recalcul) {
            //System.out.println("DEBUG -- paire.i : "+paire.i);
            if(partition.indexOf(paire.i) != -1)calculatePaireModularite(partition.indexOf(paire.i),0, graphe);
            paires_modularite.remove(paire);
        }
        calculatePaireModularite(i, 0, graphe);
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
        Instant start = Instant.now();
        for(int i = 0; i < partition.size(); i++) {
            calculatePaireModularite(i, i+1, graphe);
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("time : "+timeElapsed+" ms");
    }

    public void calculatePaireModularite(int i, int j, Graphe graphe) {
        double modu_max = 0, tmp;
        int k = -1;
        Cluster clua = partition.get(i), club;
        double m = graphe.nbr_arete;
        for (j = 0; j < partition.size(); j++) {
            if(i != j){
                club = partition.get(j);
                tmp = (matrix_Mij.get(i).get(j) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m)))
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                if(tmp >= modu_max) {
                    modu_max = tmp;
                    k = j;
                }
            }
        }
        if (k == i) return;
        if(k == -1) paires_modularite.add(new Paire(partition.get(i), null, -1));
        else paires_modularite.add(new Paire(partition.get(i), partition.get(k), modu_max));
        //System.out.println("DEBUG -- i : "+i+", j : "+k+", modularite_max : "+modu_max);
    }
    
    //Version optimisé
    public double[] calculatePaire(Graphe graphe){
        initPaireModularite(graphe);
        double m = graphe.nbr_arete;
        double increment = 0;
        ArrayList<Cluster> originalPartition = partition;
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
        Paire p = paires_modularite.poll();
        while(p.j != null || paires_modularite.isEmpty()){
            if (p.j == null) calculatePaireModularite(partition.indexOf(p.i), 0, graphe);
            if(p.modularite >= 0) fusionner(partition.indexOf(p.i), partition.indexOf(p.j), graphe);
            p = paires_modularite.poll();
            //if(paires_modularite.isEmpty()) initPaireModularite(graphe);
            //res = calculatePaire(graphe);
            //System.out.println("DEBUG - res : "+ res[0] + " ; "+res[1] + " ; increment : "+ res[2]);
            //fusionner((int)res[0], (int)res[1], graphe);
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