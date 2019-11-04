
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
	//HashMap<Integer, int[]> distance_matrice;
	int [][] distance_matrice;
    int [][] npcc;
    float []bet;
    Graphe graphe;
        
        
    public BFSAlgorithm(Graphe graphe) {
    	int size = graphe.getSommets().size();
        this.graphe = graphe;
        /*for(int i = 0; i < size; i++) {
        	distance_matrice[i] = new Integer[size];
        	npcc[i] = new Integer[size];
        }*/

    }
    
    public void BFS (int u) {    	
    	String[] color = new String[graphe.getSommets().size()];
    	Deque <Integer> file = new LinkedList<>();
    	for (int i = 0; i<graphe.getSommets().size(); i++)  {
    		color[i] =  "blanc";
    	}
    	file.add(u);
    	color[u] = "gris";
    	int s, dist;
    	while(!file.isEmpty()) {
    		s = file.removeFirst();
    		System.out.println("sommet courant : " + s);
			npcc[u][s] = 0;
			npcc[s][u] = 0;
    		for(Sommet tmp : graphe.getSommetByID(s).getAdjacence()) {
    			String col = color[tmp.getId()];
    			if(col.equals("blanc")) {
    				//si première visite ajouter à la file et changer la couleur
    				if(color[tmp.getId()].equals("blanc")) {
    					color[tmp.getId()] =  "gris";
        				distance_matrice[u][tmp.getId()] = distance_matrice[u][s] + 1;
        				distance_matrice[tmp.getId()][u] = distance_matrice[u][s] + 1;
    					file.add(tmp.getId());
    				}
    			}
    			if(tmp.getId() != s && tmp.getId() != u && u!=s) {
    				dist = distance_matrice[u][tmp.getId()] + distance_matrice[tmp.getId()][s];
    				System.out.println("**********Dist : "+dist);
    				//si dist = 1 alors npcc = 1
    				if(distance_matrice[u][s] == 1) {
    					npcc[u][s] = 1;
    					npcc[s][u] = 1;
    				}
    				if(distance_matrice[u][tmp.getId()] == 1) {
    					npcc[u][tmp.getId()] = 1;
    					npcc[tmp.getId()][u] = 1;
    				}
    				if(distance_matrice[u][s] == dist) {
    					npcc[u][s] += npcc[u][tmp.getId()];
    					npcc[s][u] += npcc[u][tmp.getId()];
    				}
    			}
    		}
    		color[s] = "noir";
    	}
    }
    
    //bet(s,v,t) = npcc(s,v,t) / npcc(s,t)
 	//npcc(s,v,t) = npcc(s,v)*npcc(v,t)
     float bet_svt(int s, int v, int t) {
    	 float svt = 0;
    	 if(npcc[s][t] == 0) {
    		 //System.out.println("-------bet-svt "+s+","+v+","+t+" : "+svt);
    		 return svt;
    	 }
    	 if(distance_matrice[s][t] == distance_matrice[s][v]+distance_matrice[v][t]) {
    		 float npcc_svt = npcc[s][v]*npcc[v][t];
        	 svt = npcc_svt / npcc[s][t];
    	 }
		 //System.out.println("-------bet-svt "+s+","+v+","+t+" : "+svt);
    	 return svt;
     }

    void addBet() {
    	int size = graphe.getSommets().size();
    	
    	float n = (float)((size-1)*(size-2));
    	float bet = 0;
    	for(int v = 0; v<size; v++) {
    		float sum=0;
        	for (int s = 0; s < size; s++) {
        		for(int t = 0;t < size; t++) {
        			if(s!=v && s!=t && v!=t) {
                		sum += bet_svt(s,v,t);
                   	}
        		}
        	}
    		//System.out.println("*****sum = "+sum);
    		//System.out.println("*****n = "+n);
        	bet =(float)(sum)/n;
        	System.out.println(v+" : "+ bet);
    	}
    }

    void addDistance() {
    System.out.println("Dans addDistance");
   distance_matrice = new int[graphe.getSommets().size()][graphe.getSommets().size()];
    npcc = new int[graphe.getSommets().size()][graphe.getSommets().size()];
	for (int i = 0; i < npcc.length; i++) {
			System.out.println("BFS sur : "+i);
			BFS(i);
	 }
	System.out.println("*********Matrice des distances*************");
		for(int i = 0; i<distance_matrice.length; i++) {
	       // System.out.println("col "+i+":");
			for(int j= 0; j<distance_matrice[i].length; j++) {
	            System.out.print(distance_matrice[i][j]+" ");
			}
	        System.out.println();
		}
		System.out.println("*********Matrice des npcc*************");
		for(int i = 0; i<npcc.length; i++) {
	       // System.out.println("col "+i+":");
			for(int j= 0; j<npcc[i].length; j++) {
	            System.out.print(npcc[i][j]+" ");
			}
	        System.out.println();
		}
    }
}
