import java.util.HashMap;

/**
 *
 * @author aouessar, Melila
 */
public class Graphe {
    private String nom_Graphe;
    private int nbr_sommet;
    private int nbr_arrete;
    private int doublons;
    private int boucles;
    private int degreMax;
    private int idSommetMax;
    private HashMap<Integer, Sommet> sommets;
    
    public Graphe(){
        this.nbr_arrete = 0;
        this.nbr_sommet = 0;
        this.doublons = 0;
        this.boucles = 0;
        this.degreMax = 0;
        this.idSommetMax = 0;
        this.sommets = new HashMap<>();
    }
    
    public void addSommet(int id, int id2){
        if (id == id2) {
            boucles++;
        } else {
            if (id > id2) {
                if (id > idSommetMax) idSommetMax = id;
            } else {
                if (id2 > idSommetMax) idSommetMax = id2;
            }

            Sommet som0;
            Sommet som1;
            if ((som0 = sommets.get(id)) != null) {
                if ((som1 = sommets.get(id2)) != null) {
                    if (!som0.contient(som1.getId())) {
                        som0.addSommetAdjacent(som1);
                        som1.addSommetAdjacent(som0);
                        nbr_arrete++;
                        if (som0.getDegre() > som1.getDegre()) {
                            if (som0.getDegre() > degreMax) degreMax = som0.getDegre();
                        } else {
                            if (som1.getDegre() > degreMax) degreMax = som1.getDegre();
                        }
                    } else doublons++;
                } else {
                    som1 = new Sommet(id2, som0);
                    sommets.put(id2, som1);
                    som0.addSommetAdjacent(som1);
                    nbr_sommet++;
                    nbr_arrete++;
                    if (som0.getDegre() > degreMax) degreMax = som0.getDegre();
                }
            } else {
                if ((som1 = sommets.get(id2)) != null) {
                    som0 = new Sommet(id, som1);
                    sommets.put(id, som0);
                    som1.addSommetAdjacent(som0);
                    nbr_sommet++;
                    nbr_arrete++;
                    if (som1.getDegre() > degreMax) degreMax = som1.getDegre();
                } else {
                    som0 = new Sommet(id);
                    som1 = new Sommet(id2, som0);
                    som0.addSommetAdjacent(som1);
                    sommets.put(id, som0);
                    sommets.put(id2, som1);
                    nbr_sommet += 2;
                    nbr_arrete++;
                    if (som0.getDegre() > degreMax) degreMax = som0.getDegre();
                }
            }
        }
    }
    
    public void addSommetDegreZero(int id, Sommet som){
        sommets.put(id, som);
    }
 
    public String getNom_Graphe() {
        return nom_Graphe;
    }

    public int getNbr_sommet() {
        return nbr_sommet;
    }

    public int getNbr_arrete() {
        return nbr_arrete;
    }
    
    public int getDegreMax() {
        return degreMax;
    }

    public int getIdSommetMax() {
        return idSommetMax;
    }

    public HashMap<Integer, Sommet> getSommets() {
        return sommets;
    }
    
    public Sommet getSommetByID(int id){
        return this.sommets.get(id);
    }
    
    public Sommet getSommetDegreMax(){
        int tmp = 0;
        int id = 0;
        for(int i = 0; i < sommets.size(); i++){
            if(getSommetByID(i).getAdjacence().size() > tmp){
                tmp = getSommetByID(i).getAdjacence().size();
                id = i;
            }
        }
        return getSommetByID(id);
    }
    
    public void setNom_Graphe(String nom_Graphe) {
        this.nom_Graphe = nom_Graphe;
    }

    public void setNbr_sommet(int nbr_sommet) {
        this.nbr_sommet = nbr_sommet;
    }

    public void setNbr_arrete(int nbr_arrete) {
        this.nbr_arrete = nbr_arrete;
    }
    
    public void listArretesDeDegreX(int degMax){
        int[] tmp = new int[degMax+1];
        for(int i = 0; i < sommets.size(); i++){
            tmp[getSommetByID(i).getAdjacence().size()]+=1;
        }
        for(int i = 0; i < tmp.length; i++){
            System.out.println(i+" "+tmp[i]);
        }
    }
    
    public void stringToTabInt(String s, int [] tab) {
        int a = 0;
        for (int pos = 0; pos < s.length(); pos++) {
            char c = s.charAt(pos);
            if (c == ' ' || c == '\t') {
                if (a != 0)
                    tab[0] = a;
                a = 0;
                continue;
            }
            if (c < '0' || c > '9') {
                System.out.println("Erreur format ligne " + pos + "c = " + c + " valeur " + (int) c);
                System.exit(1);
            }
            a = 10 * a + c - '0';
        }
        tab[1] = a;
    }
    
    public void printResult(){
        System.out.println("----------------"+getNom_Graphe()+"-----------------");
        if(this.boucles != 0) System.out.println(this.boucles+" boucles ont ete ignorees");
        System.out.println(doublons+" doublons ont ete supprimer");
        System.out.println("Nombre de sommets : "+ this.sommets.size());
        System.out.println("Nombre d'arretes : "+this.nbr_arrete);
        
        Sommet som = getSommetDegreMax();
        System.out.println("Sommet de degre max (de numero minimal) : "+som.getId());
        System.out.println("Sa liste d'adjacence (ligne suivante) : ");
        som.printSommetAdjacent();
        
        System.out.println("Ditribution des degres : ");
        listArretesDeDegreX(som.getAdjacence().size());
        
    }
}
