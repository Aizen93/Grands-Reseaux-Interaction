
import java.util.ArrayList;

class Graphe { 
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
    
    public void generateGraphe(String stanford_path){
	this.parser.parseStanfordFormat(stanford_path);
        
    }
    
    /**
     * Calculates modularité TP3 option modu
     * @param clusters_path 
     */
    public void calculateModularite(String clusters_path){
        this.partition = new Partition();
        this.parser.parseClusters(clusters_path, this.partition);
        System.out.println("size partition = " + partition.size());
        this.partition.Q();
    }
    
    public void printResult(){
        // 2- affichages
	System.out.println("Nombre de sommets : "+(nbr_sommet));
	System.out.println("Nombre d'aretes : "+nbr_arete);
	System.out.println("Sommet de degré max (de numéro minimal) : " + somdmax);
	System.out.println("Sa liste d'adjacence (ligne suivante) :");
        
 	for(int i=0; i< sommets.get(somdmax).degre; i++) 
	    System.out.print(sommets.get(somdmax).adjacence[i]+ " ");
        
	System.out.println("\nDitribution des degrés : ");
	// calculs de degré
	int[] dgr = new int[degreMax + 1];
	for(int i = 0; i < sommets.size(); i++)
	    dgr[ sommets.get(i).degre ]++;
	for(int i = 0; i <= degreMax; i++) 
	    System.out.println(i + " " + dgr[i]);
    }

}