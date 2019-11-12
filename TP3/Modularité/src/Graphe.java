class Graphe { 
    int nbr_sommet;      // nombre de sommets
    int nbr_arete;      // nombre d'aretes
    int degreMax;   // degre max d'un sommet
    int somdmax;   // numero du premier sommet atteignant le degré maximum
    Sommet[] sommets; // tableau des sommets. De taille n+1 normalement
    
    public void generateGraphe(String path){
        Parser parser = new Parser();
	parser.lecture(this, path);
    }
    
    public void printResult(){
        // 2- affichages
	System.out.println("Nombre de sommets : "+(nbr_sommet));
	System.out.println("Nombre d'aretes : "+nbr_arete);
	System.out.println("Sommet de degré max (de numéro minimal) : " + somdmax);
	System.out.println("Sa liste d'adjacence (ligne suivante) :");
        
 	for(int i=0; i< sommets[somdmax].degre; i++) 
	    System.out.print(sommets[somdmax].adjacence[i]+ " ");
        
	System.out.println("\nDitribution des degrés : ");
	// calculs de degré
	int[] dgr = new int[degreMax + 1];
	for(int i = 0; i < sommets.length; i++)
	    dgr[ sommets[i].degre ]++;
	for(int i = 0; i <= degreMax; i++) 
	    System.out.println(i + " " + dgr[i]);
    }
}