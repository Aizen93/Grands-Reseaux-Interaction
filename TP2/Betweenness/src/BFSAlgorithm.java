
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
    
    void addDistance() {
        distance_matrice = new int[graphe.getSommets().size()][graphe.getSommets().size()];
    	for (int i = 0; i < graphe.getSommets().size(); i++) {
            for(int j = 0; j < i+1; j++) {
                distance_matrice[i][j] =  minEdgeBFS(i, j, graphe.getSommets().size());
                System.out.print(distance_matrice[i][j]+" ");
            }
            System.out.println();
        }
    }
}
