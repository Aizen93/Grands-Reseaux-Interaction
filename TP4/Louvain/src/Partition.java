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
        }
         modularite = Eii - Aii;
    }
    
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

    public void fusion_cluster(int i, int j) {
        Cluster clu1 = partition.get(i);
        Cluster clu2 = partition.get(j);
        clu1.sommets.addAll(clu2.sommets);
        clu1.somme_degre = clu1.somme_degre + clu2.somme_degre;
        ArrayList<Cluster> tmp = new ArrayList<>();
        for(int k = 0; k < partition.size(); k++){
            if(k != j)    tmp.add(partition.get(k));
        }
        partition = tmp;
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
    }
    
    //Version optimisé
    public double[] calculatePaire(Graphe graphe){
        double m = graphe.nbr_arete;
        double increment = 0, increment2 = 0;
        ArrayList<Cluster> originalPartition = partition;
        int pair1 = -1, pair2 = -1;
        for(int i = 0; i < originalPartition.size(); i++){
            for(int j = i+1; j < originalPartition.size(); j++){
                Cluster clua = originalPartition.get(i);
                Cluster club = originalPartition.get(j);
                increment2 = (m(partition.get(i), partition.get(j), graphe) / m) - (sqr(clua.somme_degre + club.somme_degre)/(4*sqr(m))) 
                        + (sqr(clua.somme_degre)/(4*sqr(m))) + (sqr(club.somme_degre)/(4*sqr(m)));
                if(increment2 > increment){
                    pair1 = i; pair2 = j;
                    increment = increment2;
                    partition = originalPartition;
                    
                }else{
                    partition = originalPartition;
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
        System.out.println("time writng file: "+timeElapsed+" ms");
        
        
        Q(graphe);
        System.out.println("Meilleure modularité : " + modularite);
    }

}