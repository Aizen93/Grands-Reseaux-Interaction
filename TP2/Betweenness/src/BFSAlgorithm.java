
import java.util.ArrayList;
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
        
    // Method for finding minimum no. of edge 
    // using BFS 
    public int minEdgeBFS(int u, int v, int n) { 
        // visited[n] for keeping track of visited 
        // node in BFS 
        ArrayList<Boolean> visited = new ArrayList<Boolean>(n); 
          
        for (int i = 0; i < n; i++) { 
            visited.add(false); 
        } 
       
        // Initialize distances as 0 
        ArrayList<Integer> distance = new ArrayList<Integer>(n); 
          
        for (int i = 0; i < n; i++) { 
            distance.add(0); 
        } 
       
        // queue to do BFS. 
        Queue<Integer> Q = new LinkedList<>(); 
        //distance.setElementAt(0, u);
        distance.set(u, 0);
       
        Q.add(u); 
        //visited.setElementAt(true, u);
        visited.set(u, true);
        while (!Q.isEmpty()){ 
            int x = Q.peek(); 
            Q.poll(); 
            Sommet som = graphe.getSommetByID(x);
            for (int i=0; i<som.getAdjacence().size(); i++){ 
                if (visited.get(som.getSommetAdjacentByIndex(i).getId())) continue; 
                    
                // update distance for i 
                //distance.setElementAt(distance.get(x) + 1, som.getSommetAdjacentByIndex(i).getId());
                distance.set(som.getSommetAdjacentByIndex(i).getId(), distance.get(x) + 1);
                Q.add(som.getAdjacence().get(i).getId()); 
                //visited.setElementAt(true,som.getSommetAdjacentByIndex(i).getId());
                visited.set(som.getSommetAdjacentByIndex(i).getId(), true);
            } 
        } 
        return distance.get(v); 
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
    distance_matrice = new int[graphe.getSommets().size()][graphe.getSommets().size()];
		for (int i = 1; i < graphe.getSommets().size(); i++) {
	        for(int j = 1; j < i+1; j++) {
	            distance_matrice[i][j] =  minEdgeBFS(i, j, graphe.getSommets().size());
	            System.out.print(distance_matrice[i][j]+" ");
	        }
	        System.out.println();
	    }
}
}
