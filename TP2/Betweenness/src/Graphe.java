import java.util.HashMap;

/**
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
    
    /**
     * Constructeur Graphe, permet de construire un graphe vierge
     */
    public Graphe(){
        this.nbr_arrete = 0;
        this.nbr_sommet = 0;
        this.doublons = 0;
        this.boucles = 0;
        this.degreMax = 0;
        this.idSommetMax = 0;
        this.sommets = new HashMap<>();
    }
    
    /**
     * Ajoute les 2 sommets d'une arrete dans le graphe selon certaine condition
     * si un sommet existe deja celui ci est ignoré
     * compte les doublons, boucles, degreMax ansi que le nombre de sommet pendant le remplissage du graphe
     * @param id
     * @param id2 
     */
    public void addSommet(int id, int id2){
        Sommet som0;
        Sommet som1;
        if(id != id2){//on teste si le noeud n'est pas un arc vers lui meme
            if(idSommetMax < id) idSommetMax = id;
            if(idSommetMax < id2) idSommetMax = id2;
                
            if((som0 = getSommetByID(id)) == null){
                som0 = new Sommet(id);
                sommets.put(id, som0);
                nbr_sommet++;
            }
            if((som1 = getSommetByID(id2)) == null){
                som1 = new Sommet(id2);
                sommets.put(id2, som1);
                nbr_sommet++;
            }
            if(som0.contient(som1)){
                doublons++;
                nbr_arrete--;
            }
            som0.addSommetAdjacent(som1);
            som1.addSommetAdjacent(som0);
            nbr_arrete++;
        }else{
            boucles++;
            if(idSommetMax < id) idSommetMax = id;
            if((som0 = getSommetByID(id)) == null){
                som0 = new Sommet(id);
                sommets.put(id, som0);
                nbr_sommet++;
            }   
        }
    }
    
    /**
     * Ajoute les sommets de degré zero qui normalement ne sont pas dans les fichiers du format Stanford
     * @param id
     * @param som 
     */
    public void addSommetDegreZero(int id, Sommet som){
        sommets.put(id, som);
        nbr_sommet++;
    }
    
    /**
     * Parcours le graphe a la recherche du sommet ayant le degre maximum
     * @return le sommet de degre max, null si rien trouvé
     */
    public Sommet getSommetDegreMax(){
        int tmp = 0;
        int id = 0;
        for(int i = 0; i < sommets.size(); i++){
            if(getSommetByID(i).getAdjacence().size() > tmp){
                tmp = getSommetByID(i).getAdjacence().size();
                id = i;
            }
        }
        degreMax = tmp;
        return getSommetByID(id);
    }
    
    /**
     * Calcule la liste de distribution des degrés
     * @param degMax 
     */
    public void listArretesDeDegreX(int degMax){
        int[] tmp = new int[degMax+1];
        for(int i = 0; i < sommets.size(); i++){
            tmp[getSommetByID(i).getAdjacence().size()]+=1;
        }
        for(int i = 0; i < tmp.length; i++){
            System.out.println(i+" "+tmp[i]);
        }
    }
    
    /**
     * Split un String en deux (Format Stanford d'une arrete) et transforme le string en int
     * @param s
     * @param tab 
     */
    public void splitAndParseInt(String s, int [] tab) {
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
    
    /**
     * Fonction d'affichage du resultat
     */
    public void printResult(){
        System.out.println("----------------"+getNom_Graphe()+"-----------------");
        if(this.boucles != 0) System.out.println(this.boucles+" boucles ont ete ignorees");
        System.out.println(this.doublons+" doublons ont ete supprimer");
        System.out.println("Nombre de sommets : "+ this.nbr_sommet);
        System.out.println("Nombre d'arretes : "+this.nbr_arrete);
        
        Sommet som = getSommetDegreMax();
        System.out.println("Sommet de degre max (de numero minimal) : "+som.getId());
        //System.out.println("Sa liste d'adjacence (ligne suivante) : ");
        //som.printSommetAdjacent();
        
        System.out.println("Ditribution des degres : ");
        //listArretesDeDegreX(som.getAdjacence().size());
        
    }
    
    public void printResult2(){
        System.out.println("----------------"+getNom_Graphe()+"-----------------");
        if(this.boucles != 0) System.out.println(this.boucles+" boucles ont ete ignorees");
        System.out.println(this.doublons+" doublons ont ete supprimer");
        System.out.println("Nombre de sommets : "+ this.nbr_sommet);
        System.out.println("Nombre d'arretes : "+this.nbr_arrete);
    }
    
    public void setNom_Graphe(String nom_Graphe) {
        this.nom_Graphe = nom_Graphe;
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
}
