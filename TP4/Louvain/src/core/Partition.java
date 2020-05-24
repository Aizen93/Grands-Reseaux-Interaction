package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author Oussama, Lilya
 */
public class Partition {
    
    public ArrayList<Cluster> partition;
    ArrayList<ArrayList<Integer>> matrix_Mij;//a remplacer par une matrice creuse (HashMap)
    PriorityQueue<Paire> paires_modularite;
    double modularite;
    String info = "";

    public Partition(){
        this.partition = new ArrayList<>();
        this.modularite = 0.0;
        paires_modularite = new PriorityQueue<>();
    }
    
    /**
     * Adds a cluster to the partition
     * @param c 
     */
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
    
    /**
     * Math.pow(a, 2)
     * @param a
     * @return 
     */
    public double sqr(double a){
        return a * a;
    }
    
    /**
     * Calculate's the modularity of a graph's partition
     * @param graphe
     */
    public void Q(Graphe graphe){
        double m = graphe.nbr_arete;
        double Eii = 0.0, Aii = 0.0;
        for(int i = 0; i < partition.size(); i++){
            partition.get(i).calcul_nb_arete(graphe);
            
            Eii += partition.get(i).nb_arete / m;
            Aii += sqr(partition.get(i).somme_degre) / (4 * sqr(m));
        }
         modularite = Eii - Aii;
    }
    
    /**
     * Merges two clusters of indices i and j
     * and updates the matrix Mij of a cluster 
     * by adding the edge's sum of the second cluster to the first one
     * and recalculates the modularity increment of the new pair
     * then remove's the pairs we don't need from the PriorityQueue
     * @param i indice of Cluster a
     * @param j indice of Cluster b
     * @param graphe 
     */
    public void fusionner(int i, int j, Graphe graphe){
        if(i < 0 || j < 0) return;
        ArrayList<Paire> recalcul = new ArrayList<>();
        for (Paire paire: paires_modularite) {
            if(partition.get(i).equals(paire.j) || partition.get(j).equals(paire.j)) recalcul.add(paire);
        }
        if(i > j) {
            int tmp = i; i = j; j = tmp;
        }
        fusion_cluster(i,j);
        updateMatrix(i,j);
        recalcul.stream().map((paire) -> {
            if(partition.indexOf(paire.i) != -1){
                calculatePaireModularite(partition.indexOf(paire.i),0, graphe);
            }
            return paire;
        }).forEachOrdered((paire) -> {
            paires_modularite.remove(paire);
        });
        calculatePaireModularite(i, 0, graphe);
    }
    
    /**
     * Updates the sum of edges of a pair of clusters in the matrix
     * @param i indice of Cluster a
     * @param j indice of Cluster b
     */
    public void updateMatrix(int i, int j){
        for(int k = 0; k < matrix_Mij.get(i).size(); k++){
            matrix_Mij.get(i).set(k, matrix_Mij.get(i).get(k) + matrix_Mij.get(j).get(k));
        }
        for(int k = 0; k < matrix_Mij.size(); k++){
            matrix_Mij.get(k).set(i, matrix_Mij.get(k).get(j) + matrix_Mij.get(k).get(i));
            matrix_Mij.get(k).remove(j);
        }
        matrix_Mij.remove(j);
    }
    
    /**
     * Merges two clusters into one
     * @param i indice of Cluster a
     * @param j indice of Cluster b
     */
    public void fusion_cluster(int i, int j) {
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);
        clu1.sommets.addAll(clu2.sommets);
        clu1.somme_degre = clu1.somme_degre + clu2.somme_degre;
        ArrayList<Cluster> tmp = new ArrayList<>();
        for(int k = 0; k < partition.size(); k++){
            if(k != j) tmp.add(partition.get(k));
        }
        partition = tmp;
    }
    
    /**
     * Naive method of calculating the sum of edges between two clusters
     * @param cluster1
     * @param cluster2
     * @param g graphe
     * @return the sum of edges between the two clusters
     */
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
        return eij;
    }
    
    /**
     * Initiate method of pair calculations
     * @param graphe 
     */
    public void initPaireModularite(Graphe graphe) {
        Instant start = Instant.now();
        for(int i = 0; i < partition.size(); i++) {
            calculatePaireModularite(i, i+1, graphe);
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("PriorityQueue pair init time : "+timeElapsed+" ms");
        info += "- PriorityQueue pair init time :\n"+timeElapsed+" ms\n";
        info += "-------------------------\n";
    }
    
    /**
     * Calculates the best modularity increment if merging the cluster i with 
     * all clusters in partition starting from n indice and ignoring the rest
     * we take the best increment possible out of all pairs
     * (we don't need to recalculate what has been alrady calculated so we start from n and not 0)
     * @param i
     * @param n
     * @param graphe 
     */
    public void calculatePaireModularite(int i, int n, Graphe graphe) {
        double increment_modu_max = 0, tmp;
        int k = -1;
        Cluster clua = partition.get(i), club;
        double m = graphe.nbr_arete;
        for (int j = n; j < partition.size(); j++) {
            if(i != j){
                club = partition.get(j);
                tmp = (matrix_Mij.get(i).get(j) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m)))
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                if(tmp >= increment_modu_max) {
                    increment_modu_max = tmp;
                    k = j;
                }
            }
        }
        if (k == i) return;
        if(k == -1) paires_modularite.add(new Paire(partition.get(i), null, -1));
        else paires_modularite.add(new Paire(partition.get(i), partition.get(k), increment_modu_max));
    }
    
    /**
     * Optimised version of calculating the modularity increment of <paire> command
     * @param graphe
     * @return 
     */
    public double[] calculatePaire(Graphe graphe){
        double m = graphe.nbr_arete;
        double increment = 0, increment2 = 0;
        int pair1 = -1, pair2 = -1;
        for(int i = 0; i < partition.size(); i++){
            for(int j = i+1; j < partition.size(); j++){
                Cluster clua = partition.get(i);
                Cluster club = partition.get(j);
                increment2 = (m(partition.get(i), partition.get(j), graphe) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m))) 
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                if(increment2 > increment){
                    pair1 = i; pair2 = j;
                    increment = increment2;
                    
                }
            }
        }
        return new double[]{pair1, pair2, increment};
    }

    public void calculateLouvain(String cluster_path, Graphe graphe){
        graphe.sommets.stream().filter((som) -> (som.adjacence != null)).map((som) -> {
            Cluster clu = new Cluster();
            clu.sommets.add(som.ID);
            clu.somme_degre = som.degre;
            return clu;
        }).forEachOrdered((clu) -> {
            this.partition.add(clu);
        });
        initPaireModularite(graphe);
        Paire p = paires_modularite.poll();
        while(p.j != null || paires_modularite.isEmpty()){
            if (p.j == null) calculatePaireModularite(partition.indexOf(p.i), 0, graphe);
            if(p.modularite >= 0) fusionner(partition.indexOf(p.i), partition.indexOf(p.j), graphe);
            p = paires_modularite.poll();
        }
        
        /* ----------------------- End of Louvain's Algorithm ----------------------------*/
        
        Instant start = Instant.now();
        FileWriter filewriter = null;
        try {
            File file = new File(cluster_path);
            filewriter = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bufferwriter = new BufferedWriter(filewriter)) {
                for (Cluster c: partition) {
                    if(c.sommets.size() > 1) {
                        for (int i: c.sommets) {
                            bufferwriter.write(i+" ");
                        }
                        bufferwriter.write("\n");
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error, writing on file <" + cluster_path + ">");
        } finally {
            try {
                filewriter.close();
            } catch (IOException ex) {
                System.out.println("Error, closing filewriter !");
            }
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("File <" + cluster_path + "> generated succesfully in : "+timeElapsed+" ms");
        info += "- File <" + cluster_path + "> generated succesfully in :\n"+timeElapsed+" ms\n";
        info += "-------------------------\n";
        
        Q(graphe);
        System.out.println("-------------------------------------------");
        System.out.println("| Meilleure modularité : " + modularite);
        System.out.println("-------------------------------------------");
        info += "- Meilleure modularité : " + modularite;
        info += "\n-------------------------\n";
        info += "- Il y a " + graphe.partition.size() + " Clusters\n";
        
    }
    
    public int getMaxsizeCluster(){
        int max = -1;
        for(Cluster clu : partition){
            if(clu.size() > max) max = clu.size();
        }
        return max;
    }
    
    public double getModularite() {
        return modularite;
    }
    
    public String getInfo(){
        return info;
    }
    
    public Cluster getCluster(int i){
        return this.partition.get(i);
    }
    
    public ArrayList<Cluster> getPartition(){
        return this.partition;
    }

}