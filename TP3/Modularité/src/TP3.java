
public class TP3 {
    
    public static void main(String[] args) {
	
	if (args.length != 1) {
	    System.out.println("Usage : java TP1 nomFichier.txt");
	    return;
	}
	
	Graphe graphe = new Graphe(); // le graphe sur lequel on travaille
        graphe.generateGraphe(args[0]);
        graphe.printResult();

        
	System.out.println("Mémoire allouée : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");
    }
}
