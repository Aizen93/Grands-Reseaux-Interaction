package core;

import gui.Arc;
import gui.DialogPopUp;
import java.util.ArrayList;

public class Graphe {
    public int nbr_sommet;      // nombre de sommets
    public int nbr_arete;      // nombre d'aretes
    public int degreMax;   // degre max d'un sommet
    public int boucle;   // nombre de boucle ignorés  
    public int doublons;   //nombre de doublons
    public int somdmax;   // numero du premier sommet atteignant le degré maximum
    public ArrayList<Sommet> sommets; // tableau des sommets. De taille n+1 normalement
    Parser parser;
    public Partition partition;
    public Arc arcs;

    public Graphe(){
        this.parser = new Parser(this);
        this.arcs = new Arc(this);
        this.doublons = 0;
        this.boucle = 0;
    }
    
    public Sommet getSommet(int i){
        for(Sommet s : sommets){
            if(s.ID == i) return s;
        }
        return null;
    }
    
    public int generateGraphe(String stanford_path){
	return this.parser.parseStanfordFormat(stanford_path);
    }
    
    /**
     * Calculates modularity TP3 
     * option modu
     * @param clusters_path 
     */
    public void calculateModularite(String clusters_path){
        this.partition = new Partition();
        this.parser.parseClusters(clusters_path, this.partition);
        //System.out.println("size partition = " + partition.size());
        this.partition.Q(this);
        System.out.println("-----------------------------------");
        System.out.println("| Modularité = " + this.partition.modularite);
        System.out.println("-----------------------------------");
    }
    
    /**
     * Calculate the best increment when we merge two clusters TP34
     * and prints the indices of the two clusters
     * option paire
     * Uses number (3)'s equation to calculate the modularity increment 
     * Steps followed :
     * - Calculate the increment modularity for every pair of clusters
     * - Compare all modularity increment found
     * - Takes the best increment possible
     * @param clusters_path 
     */
    public void calculateIncrementModu(String clusters_path){
        this.partition = new Partition();
        this.parser.parseClusters(clusters_path, this.partition);
        partition.calculatePaire(this);
        
        double[] res = this.partition.calculatePaire(this);
        if(res[0] != -1){
            for(int i = 0; i < partition.getPartition().get((int)res[0]).size(); i++){
                System.out.print(partition.getPartition().get((int)res[0]).sommets.get(i) + " ");
            }
            System.out.println("");
            for(int i = 0; i < partition.getPartition().get((int)res[1]).size(); i++){
                System.out.print(partition.getPartition().get((int)res[1]).sommets.get(i) + " ");
            }
            System.out.println("");
            System.out.println("incrément de modularité : " + res[2]);
        }else{
            System.out.println("La modularité est au max avec le clustering actuelle de <" + clusters_path + ">, plus besoin de fusionner d'avantage !");
        }
    }
    
    /**
     * Uses Louvain's Algorithm to create clusters with the best modularity possible 
     * TP34
     * @param clusters_path 
     */
    public void calculateLouvain(String clusters_path) {
        this.partition = new Partition();
        
        //matrice d'adjacence
        partition.matrix_Mij = new ArrayList<>();
        int y = 0;
        for(Sommet som1 : sommets){
            if(som1.adjacence != null){
                partition.matrix_Mij.add(new ArrayList<>());
                for(Sommet som2 : sommets){
                    if(som2.adjacence != null){
                        if(som1.ID == som2.ID) partition.matrix_Mij.get(y).add(0);
                        else{
                            if(som1.contient(som2)) partition.matrix_Mij.get(y).add(1);
                            else partition.matrix_Mij.get(y).add(0);
                        }
                    }
                }
                y++;
            }
        }
        
        this.partition.calculateLouvain(clusters_path, this);
    }
    
    public void printResult(){
	System.out.println("Nombre de sommets : "+(nbr_sommet));
	System.out.println("Nombre d'aretes : "+nbr_arete);
	System.out.println("Sommet de degré max (de numéro minimal) : " + somdmax);
	System.out.println("Sa liste d'adjacence (ligne suivante) :");
        
        Sommet somDegreMax = null;
        for(Sommet s : sommets){
            if(s.ID == somdmax){
                somDegreMax = s;
                break;
            }
        }
 	for(int i = 0; i < somDegreMax.degre; i++) 
	    System.out.print(somDegreMax.adjacence[i]+ " ");
        
	System.out.println("\nDitribution des degrés : ");
	// calculs de degré
	int[] dgr = new int[degreMax + 1];
	for(int i = 0; i < sommets.size(); i++)
	    dgr[ sommets.get(i).degre ]++;
	for(int i = 0; i <= degreMax; i++) 
	    System.out.println(i + " " + dgr[i]);
        
    }
    
    public String getStringResult(){
        return "Nombre de sommets : \n"+ nbr_sommet + "\n"
            +"---------------------------\n\n"
            +"Nombre d'aretes : \n"+ nbr_arete + "\n"
            +"---------------------------\n\n"
            +"Nombre de doublons : \n"+ doublons + "\n"
            +"---------------------------\n\n"
            +"Nombre de boucles\nignorées : "+ boucle + "\n"
            +"---------------------------\n\n"
            +"Sommet de degré max\n(de numéro minimal) :\n " + somdmax +"\n"
            +"---------------------------\n\n"
            +"Le degré max : "+ degreMax +"\n"
            +"---------------------------\n\n";
    }
    
    public int getNbr_sommet() {
        return nbr_sommet;
    }

    public int getNbr_arete() {
        return nbr_arete;
    }

    public int getDegreMax() {
        return degreMax;
    }

    public int getSomdmax() {
        return somdmax;
    }

    public ArrayList<Sommet> getListSommets() {
        return sommets;
    }

    public Parser getParser() {
        return parser;
    }

    public Partition getPartition() {
        return partition;
    }
}