 package gui.simulation3d;

import core.Sommet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author Dub
 */
public class MathUtils {
    public static final double DBL_EPSILON = 2.220446049250313E-16d; 
    public static final double ZERO_TOLERANCE = 0.0001d;
    public static final double ONE_THIRD = 1d / 3d;
    public static final double TAU =  (Math.PI * 2.0);
    public static final double HALF_TAU =  Math.PI;
    public static final double QUARTER_TAU =   (Math.PI / 2.0);
    public static final double INVERSE_TAU =   (1.0 / Math.PI);
    public static final double PI =   Math.PI;
    public static final double TWO_PI = 2.0d * PI;
    public static final double HALF_PI = 0.5d * PI;
    public static final double QUARTER_PI = 0.25d * PI;
    public static final double INV_PI = 1.0d / PI;
    public static final double INV_TWO_PI = 1.0d / TWO_PI;
    public static final double DEG_TO_RAD = PI / 180.0d;
    public static final double RAD_TO_DEG = 180.0d / PI;
    public static int lehmer = 1, rangeX = 0, rangeY = 0;
    public static Random rand = new Random();
    
    public static boolean isWithinEpsilon(double a, double b, double epsilon) {
        return Math.abs(a - b) <= epsilon;
    }

    public static boolean isWithinEpsilon(double a, double b) {
        return isWithinEpsilon(a, b, ZERO_TOLERANCE);
    }

    public static boolean isPowerOfTwo(int number) {
        return (number > 0) && (number & (number - 1)) == 0;
    }

    public static int nearestPowerOfTwo(int number) {
        return (int) Math.pow(2, Math.ceil(Math.log(number) / Math.log(2)));
    }
    
        
    public static javafx.geometry.Point3D computeNormal(javafx.geometry.Point3D v1, javafx.geometry.Point3D v2, javafx.geometry.Point3D v3) {
        javafx.geometry.Point3D a1 = v1.subtract(v2);
        javafx.geometry.Point3D a2 = v3.subtract(v2);
        return a2.crossProduct(a1).normalize();
    }
    
    public static javafx.geometry.Point3D sphericalToCartesian(javafx.geometry.Point3D sphereCoords) {
        double a, x, y, z;
        y = sphereCoords.getX() * Math.sin(sphereCoords.getZ());
        a = sphereCoords.getX() * Math.cos(sphereCoords.getZ());
        x = a * Math.cos(sphereCoords.getY());
        z = a * Math.sin(sphereCoords.getY());
        return new javafx.geometry.Point3D(x, y, z);
    }
    
    public static javafx.geometry.Point3D cartesianToSpherical(javafx.geometry.Point3D cartCoords) {
        double x = cartCoords.getX();
        double storex, storey, storez;
        if (x == 0) {
            x = DBL_EPSILON;
        }
        storex = Math.sqrt((x * x)
                + (cartCoords.getY() * cartCoords.getY())
                + (cartCoords.getZ() * cartCoords.getZ()));
        storey = Math.atan(cartCoords.getZ() / x);
        if (x < 0) {
            storey += PI;
        }
        storez = Math.asin(cartCoords.getY() / storex);
        return new javafx.geometry.Point3D(storex, storey, storez);
    }
    public static float clamp(float input, float min, float max) {
        return (input < min) ? min : (input > max) ? max : input;
    }
    public static double clamp(double input, double min, double max) {
        return (input < min) ? min : (input > max) ? max : input;
    }
    
    public static int lehmerRandom(int min, int max, int seed){
        lehmer = seed >> 16 | seed;
        lehmer += 0xe120fc15;
        long tmp;
        tmp = (long)lehmer * 0x4a39b70d;
        long m1 = (tmp << 32) ^ tmp;
        tmp = m1 * 0x12fad5c9;
        long m2 = (tmp << 32) ^ tmp;
        return (Math.abs((int)m2) % (max - min))+ min;
    }
    
    public static Color randomColor(){
        float r = rand.nextFloat() / 2f + 0.2f;
        float g = rand.nextFloat() / 2f + 0.2f;
        float b = rand.nextFloat() / 2f + 0.2f;
        return Color.color(r,g,b);
    }
    
    public static boolean BFS(ArrayList<Sommet> adj, int src, int dest, int v, int pred[], int dist[]) { 
        // a queue to maintain queue of vertices whose 
        // adjacency list is to be scanned as per normal 
        // BFS algorithm using LinkedList of Integer type 
        LinkedList<Integer> queue = new LinkedList<>(); 
  
        // boolean array visited[] which stores the 
        // information whether ith vertex is reached 
        // at least once in the Breadth first search 
        boolean visited[] = new boolean[v]; 
  
        // initially all vertices are unvisited 
        // so v[i] for all i is false 
        // and as no path is yet constructed 
        // dist[i] for all i set to infinity 
        for (int i = 0; i < v; i++) { 
            visited[i] = false; 
            dist[i] = Integer.MAX_VALUE; 
            pred[i] = -1; 
        } 
  
        // now source is first to be visited and 
        // distance from source to itself should be 0 
        visited[src] = true; 
        dist[src] = 0; 
        queue.add(src); 
  
        // bfs Algorithm 
        try{
            while (!queue.isEmpty()) { 
                int u = queue.remove(); 
                for (int i = 0; i < adj.get(u).adjacence.length; i++) { 
                    if (visited[adj.get(u).adjacence[i]] == false) { 
                        visited[adj.get(u).adjacence[i]] = true; 
                        dist[adj.get(u).adjacence[i]] = dist[u] + 1; 
                        pred[adj.get(u).adjacence[i]] = u; 
                        queue.add(adj.get(u).adjacence[i]); 

                        // stopping condition (when we find 
                        // our destination) 
                        if (adj.get(u).adjacence[i] == dest) 
                            return true; 
                    } 
                } 
            }
        }catch(NullPointerException ex){
            return false;
        }
        return false; 
    }
    
    public static LinkedList<Integer> findShortestPath(ArrayList<Sommet> adj, int dest, int s, int v){
        int pred[] = new int[v]; 
        int dist[] = new int[v];
        
        LinkedList<Integer> path = new LinkedList<>();
        if (BFS(adj, s, dest, v, pred, dist) == false) { 
            System.out.println("Given source and destination are not connected"); 
            return path; 
        } 
        // LinkedList to store path
        int crawl = dest; 
        path.add(crawl); 
        while (pred[crawl] != -1) { 
            path.add(pred[crawl]); 
            crawl = pred[crawl]; 
        } 
  
        // Print distance 
        System.out.println("Shortest path length is: " + dist[dest]); 
        return path;
    }
}
