import java.util.ArrayList;

/**
 *
 * @author aouessar, Melila
 */
public class Sommet {
    final private int id;
    private int degre;
    private ArrayList<Sommet> adjacence;
    
    public Sommet(int a, Sommet b) {
        this.id = a;
        this.degre = 1;
        this.adjacence = new ArrayList<>();
        this.adjacence.add(b);
    }
    
    public Sommet(int id){
        this.id = id;
        this.degre = 0;
        this.adjacence = new ArrayList<>();
    }
    
    public void addSommetAdjacent(Sommet s){
        this.adjacence.add(s);
        this.degre++;
    }
    
    public int getId() {
        return id;
    }
    
    public int getDegre() {
        return this.degre;
    }

    public void printSommetAdjacent() {
        for (Sommet s : adjacence) {
            System.out.print(s.getId()+" ");
        }
        System.out.println("");
    }

    public ArrayList<Sommet> getAdjacence() {
        return adjacence;
    }
    
    public boolean contient(int n) {
        for (int i = 0; i < adjacence.size(); i++) {
            if (adjacence.get(i).getId() == n)
                return true;
        }
        return false;
    }

}
