import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Graphe {
    int nbr_sommet;      // nombre de sommets
    int nbr_arete;      // nombre d'aretes
    int degreMax;   // degre max d'un sommet
    int somdmax;   // numero du premier sommet atteignant le degré maximum
    ArrayList<Sommet> sommets; // tableau des sommets. De taille n+1 normalement
    Parser parser;
    Partition partition;
    
    public Graphe(){
        this.parser = new Parser(this);
    }
    
    public Sommet getSommet(int i){
        for(Sommet s : sommets){
            if(s.ID == i) return s;
        }
        return null;
    }
    
    public void generateGraphe(String stanford_path){
	this.parser.parseStanfordFormat(stanford_path);
    }
    
    /**
     * Calculates modularité TP3 
     * option modu
     * @param clusters_path 
     */
    public void calculateModularite(String clusters_path){
        this.partition = new Partition();
        this.parser.parseClusters(clusters_path, this.partition);
        //System.out.println("size partition = " + partition.size());
        this.partition.Q(this);
        System.out.println("-----------------------------------");
        System.out.println("| Modularité = " + this.partition.modularite);
        System.out.println("-----------------------------------");
    }
    
    /**
     * Calculate the best increment when we merge two clusters
     * and prints the indices of the two clusters
     * option paire
     * Algorithme naive (a optimiser) les etapes sont :
     * calcule la modu
     * fusionne 2 clusters et recalcule la modu
     * on compare les 2 modu
     * on remet la partition a son etat initial puis on recommence
     * @param clusters_path 
     */
    public void calculateIncrementModu(String clusters_path){

        this.partition = new Partition();
        this.parser.parseClusters(clusters_path, this.partition);
        //partition.fusionner(0, 1);
        /*for(int k = 0; k < partition.size(); k++){
            for(int l = 0; l < partition.partition.get(k).size(); l++){
                System.out.print(partition.partition.get(k).sommets.get(l)+ " ");
            }
            System.out.println("");
        }*/
        double[] res = this.partition.calculatePaire(this);
        if(res[0] != -1){
            for(int i = 0; i < partition.partition.get((int)res[0]).size(); i++){
                System.out.print(partition.partition.get((int)res[0]).sommets.get(i) + " ");
            }
            System.out.println("");
            for(int i = 0; i < partition.partition.get((int)res[1]).size(); i++){
                System.out.print(partition.partition.get((int)res[1]).sommets.get(i) + " ");
            }
            System.out.println("");
            System.out.println("incrément de modularité : " + res[2]);
        }else{
            System.out.println("La modularité est au max avec le clustering actuelle de <" + clusters_path + ">, plus besoin de fusionner d'avantage !");
        }
    }
    
    public void printResult(){
	System.out.println("Nombre de sommets : "+(nbr_sommet));
	System.out.println("Nombre d'aretes : "+nbr_arete);
	System.out.println("Sommet de degré max (de numéro minimal) : " + somdmax);
	System.out.println("Sa liste d'adjacence (ligne suivante) :");
        
        Sommet somDegreMax = null;
        for(Sommet s : sommets){
            if(s.ID == somdmax){
                somDegreMax = s;
                break;
            }
        }
 	for(int i = 0; i < somDegreMax.degre; i++) 
	    System.out.print(somDegreMax.adjacence[i]+ " ");
        
	System.out.println("\nDitribution des degrés : ");
	// calculs de degré
	int[] dgr = new int[degreMax + 1];
	for(int i = 0; i < sommets.size(); i++)
	    dgr[ sommets.get(i).degre ]++;
	for(int i = 0; i <= degreMax; i++) 
	    System.out.println(i + " " + dgr[i]);
        
    }

    public void calculateLouvain(String clusters_path) {
        this.partition = new Partition();
        this.partition.calculateLouvain(clusters_path, this);
    }
}