/**
 * 
 * @author Oussama, Lilya
 */
public class TP34 {
    
    public static void main(String[] args) {
	if (args.length != 3) {
	    System.out.println("Usage : java TP3 option nomFichier.txt fichier.clu");
            System.out.println("options: \n- <modu> \n- <paire>\n- <louvain>");
	    return;
	}else if(args[0].equals("modu")){
            
            Graphe graphe = new Graphe();
            graphe.generateGraphe(args[1]);
            //graphe.printResult();
            graphe.calculateModularite(args[2]);
            
        }else if(args[0].equals("paire")){
            
            System.out.println("paire, being implemented not yet finished....");
            Graphe graphe = new Graphe();
            graphe.generateGraphe(args[1]);
            graphe.calculateIncrementModu(args[2]);
            
        }else if(args[0].equals("louvain")){
            
            System.out.println("louvain, not yet implemented...");
            
        }else{
            
            System.out.println("Usage : java TP3 option nomFichier.txt fichier.clu");
            System.out.println("options: \n- <modu> \n- <paire>\n- <louvain>");
            
        }
        
        
	System.out.println("Mémoire allouée : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");
    }
}
