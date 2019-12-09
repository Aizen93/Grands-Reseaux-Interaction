import java.util.Comparator;
import java.util.Objects;

public class Paire implements Comparable<Paire>{

    Cluster i, j;
    double modularite;

    public Paire(Cluster i, Cluster j, double modularite){
        this.i = i;
        this.j = j;
        this.modularite = modularite;
    }


    @Override
    public int compareTo(Paire paire) {
        if(paire.modularite > this.modularite) return 1;
        else if(paire.modularite < this.modularite) return -1;
        else return 0;
    }

}
