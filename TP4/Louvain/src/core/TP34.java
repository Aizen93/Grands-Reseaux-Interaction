package core;

/**
 * 
 * @author Oussama, Lilya
 */
public class TP34 {
    
    public static void main(String[] args) {
	if (args.length != 3) {
	    System.out.println("Usage : java TP34 option nomFichier.txt fichier.clu");
            System.out.println("options: \n- <modu> \n- <paire>\n- <louvain>");
	    return;
	}else if(args[0].equals("modu")){
            try{
                Graphe graphe = new Graphe();
                graphe.generateGraphe(args[1]);
                //graphe.printResult();
                graphe.calculateModularite(args[2]);
            }catch(NullPointerException e){
                System.out.println("Error: cluster doesn't correspond to the graphe or a negative edge detected!");
            }catch(java.lang.Error | Exception e){
                System.out.println("Something went bad somewhere aborting.....");
            }
        }else if(args[0].equals("paire")){
            try{
                Graphe graphe = new Graphe();
                graphe.generateGraphe(args[1]);
                graphe.calculateIncrementModu(args[2]);
            }catch(NullPointerException e){
                System.out.println("Error: cluster doesn't correspond to the graphe or a negative edge detected!");
            }catch(java.lang.Error | Exception e){
                System.out.println("Something went bad somewhere aborting.....");
            }
            
        }else if(args[0].equals("louvain")){
            try{
                Graphe graphe = new Graphe();
                graphe.generateGraphe(args[1]);
                graphe.calculateLouvain(args[2]);
            }catch(NullPointerException e){
                System.out.println("Error: cluster doesn't correspond to the graphe or a negative edge detected!");
            }catch(java.lang.Error | Exception e){
                System.out.println("Something went bad somewhere aborting.....Probably a Heap space out of "
                        + "memory error due to the matrix length (matrice creuse trop de zero)!");
            }
        }else{
            
            System.out.println("Usage : java TP34 option nomFichier.txt fichier.clu");
            System.out.println("options: \n- <modu> \n- <paire>\n- <louvain>");
            
        }
        
        
	System.out.println("Mémoire allouée : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " octets");
    }
}
