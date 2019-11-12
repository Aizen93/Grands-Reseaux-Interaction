
public class TP3 {
    
    public static void main(String[] args) {
	
	if (args.length != 1) {
	    System.out.println("Usage : java TP1 nomFichier.txt");
	    return;
	}
	
	Graphe graphe = new Graphe(); // le graphe sur lequel on travaille 
        Parser parser = new Parser();
	// 1- lecture
	parser.lecture(graphe, args[0]);

	// 2- affichages
	System.out.println("Nombre de sommets : "+(graphe.nbr_sommet));
	System.out.println("Nombre d'aretes : "+graphe.nbr_arete);
	System.out.println("Sommet de degré max (de numéro minimal) : " + graphe.somdmax);
	System.out.println("Sa liste d'adjacence (ligne suivante) :");
        
 	for(int i=0; i<graphe.sommets[graphe.somdmax].degre; i++) 
	    System.out.print(graphe.sommets[graphe.somdmax].adjacence[i]+ " ");
        
	System.out.println("\nDitribution des degrés : ");
	// calculs de degré
	int[] dgr = new int[graphe.degreMax + 1];
	for(int i = 0; i < graphe.sommets.length; i++)
	    dgr[ graphe.sommets[i].degre ]++;
	for(int i = 0; i <= graphe.degreMax; i++) 
	    System.out.println(i + " " + dgr[i]);
        
	System.out.println("Mémoire allouée : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");
    }
}
