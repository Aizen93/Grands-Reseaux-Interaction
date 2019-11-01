
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 *
 * @author Oussama, Lilya
 */
public class BFSAlgorithm {
    int [][] distance_matrice;
    int [][] npcc;
    float []bet;
    Graphe graphe;
        
        
    public BFSAlgorithm(Graphe graphe) {
        this.graphe = graphe;
    }
    
    public void BFS (int u) {    	
    	HashMap <Integer, String> color = new HashMap<Integer, String>();
    	for (int i = 0; i<=graphe.getNbr_sommet(); i++)  color.put(i, "blanc");
    	
    	Deque <Integer> file = new LinkedList<>();
    	file.add(u);
    	color.replace(u, "gris");
    	int s;
    	
    	while(!file.isEmpty()) {
    		s = file.removeFirst();
    		System.out.println("sommet courant : " + s);
    		for(Sommet tmp : graphe.getSommetByID(s).getAdjacence()) {
    			String col = color.get(tmp.getId());
    			if(! (col.equals("noir"))) {
    				//si première visite ajouter à la file et changer la couleur
    				if(color.get(tmp.getId()).equals("blanc")) {
    					color.replace(tmp.getId(), "gris");
        					distance_matrice[u][tmp.getId()] = distance_matrice[u][s] + 1;
    					file.add(tmp.getId());
    				}
    			}
    			if(s == u) {
    				distance_matrice[u][tmp.getId()] = 1;
    				npcc[u][tmp.getId()] = 1;
    			}
    			else if(tmp.getId() != s && tmp.getId() != u) {
    				int dist = distance_matrice[u][s] + npcc[s][tmp.getId()];
    				if(tmp.getAdjacence().size() == 1) npcc[u][tmp.getId()] = npcc[u][s];
    				if(distance_matrice[u][tmp.getId()] == 1) npcc[u][tmp.getId()] = 1;
    					
    				else if(distance_matrice[u][tmp.getId()] == dist) npcc[u][tmp.getId()] += 1;
    				else  npcc[u][tmp.getId()] += npcc[u][s];
    			}
    		}
    		color.replace(s, "noir");
    	}
    }
        
     
    
    void addBet() {
    	int size = graphe.getSommets().size();
    	int n = (size-2)*(size-3);
        bet = new float[size];
        //bet(v)
    	for(int v = 1; v<size; v++) {
            int sum=0;
            
        		//bet(s,v,t) = npcc(s,v,t) / npcc(s,t)
        		//npcc(s,v,t) = npcc(s,v)*npcc(v,t)
        		for (int s = 1; s < size; s++) {
                    for(int t = 1;t < s; t++) {
                    	if(s!=v && s!=t && v!=t) {
                        	int svt = (npcc[s][v])*(npcc[v][t]);
                        	if( (npcc[s][v]!=0) && (npcc[v][t]!=0) && (npcc[s][t]!=0) ) sum += svt / npcc[s][t];
                    	}
                    }
        		}
            

            bet[v] = (float)sum / (float)n;
            System.out.println(v+" : "+bet[v]);
    	}
    }
    
    void addnpcc() {
    	int size = graphe.getSommets().size();
    	int dist;
        npcc = new int[size][size];
    	for (int i = 1; i < size; i++) {
            for(int j = 1; j < i; j++) {
                //distance_matrice[i][j] =  ;
            	//dist = minEdgeBFS(i, j, size);
            	if(distance_matrice[i][j]>1) {
                    for(int k = 1; k < size; k++) {
                    	if(k!=i && k!=j) {
                    		
                        	int tmp = distance_matrice[i][k] + distance_matrice[k][j];
                        	if( tmp ==distance_matrice[i][j])  npcc[i][j]++;
                    	}

                    }
            	}else {
            		npcc[i][j]++;
            	}
                System.out.print(npcc[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    void addDistance() {
    	System.out.println("Dans addDistance");
    distance_matrice = new int[graphe.getSommets().size()][graphe.getSommets().size()];
    npcc = new int[graphe.getSommets().size()][graphe.getSommets().size()];

		for (int i = 0; i <= graphe.getNbr_sommet(); i++) {
			System.out.println("BFS sur : "+i);
			BFS(i);
	    }
		System.out.println("*********Matrice des distances*************");
		for(int i = 0; i<=graphe.getNbr_sommet(); i++) {
	       // System.out.println("col "+i+":");
			for(int j= 0; j<=graphe.getNbr_sommet(); j++) {
	            System.out.print(distance_matrice[i][j]+" ");
			}
	        System.out.println();
		}
		System.out.println("*********Matrice des npcc*************");
		for(int i = 0; i<=graphe.getNbr_sommet(); i++) {
	       // System.out.println("col "+i+":");
			for(int j= 0; j<=graphe.getNbr_sommet(); j++) {
	            System.out.print(npcc[i][j]+" ");
			}
	        System.out.println();
		}
    }
}
