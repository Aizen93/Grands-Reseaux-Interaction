package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 
 * @author Oussama, Lilya
 */
public class Parser {
    
    Graphe G;
    
    public Parser(Graphe graphe){
        this.G = graphe;
    }
    
    public int parseStanfordFormat(String filename) {
	// lecture du graphe, recopiée du TP1 
	
	// passe 1 : compte les lignes
	int compteur = 0; // sera le nombre de lignes du fichier
	try {
	    BufferedReader read = new BufferedReader(new FileReader(filename));
	    while (read.readLine() != null)
		compteur++;
	    read.close(); 
	}catch (IOException e) {
            return -1;
	}

	// Passe 2 : lit le fichier et construit un tableau
	int l = 0;   // nombre de lignes d'aretes déjà lues
	int[][] lus = new int[compteur][2];
	try {
	    BufferedReader read = new BufferedReader(new FileReader(filename));
	    
	    while(true) {
		String line = read.readLine();
		if(line==null) // end of file
		    break;
		if(line.charAt(0) == '#') {
		    // System.out.println(line); // affichage de la ligne de commentaire
		    continue;
		}
		int a = 0;
		for (int pos = 0; pos < line.length(); pos++) {
                    char c = line.charAt(pos);
                    if(c==' ' || c == '\t'){
                        if(a!=0)
                            lus[l][0]=a;
                        a=0;
                        continue;
                    }
                    if(c < '0' || c > '9'){
                        System.out.println("Erreur format ligne "+l+"c = "+c+" valeur "+(int)c);
                        return -1;
                    }
                    a = 10*a + c - '0';
                }
		lus[l][1]=a;
		if(G.nbr_sommet < 1+lus[l][0]) // au passage calcul du numéro de sommet max
		    G.nbr_sommet = 1+lus[l][0];
		if(G.nbr_sommet < 1+lus[l][1]) // au passage calcul du numéro de sommet max
		    G.nbr_sommet = 1+lus[l][1];
		l++;
	    }
	    read.close(); // dernier close() le fichier d'entrée ne sera plus rouvert
	}catch (IOException e) {
	    System.out.println("Erreur entree/sortie sur "+filename);
	    return -1;
	}

	// passe 3 : alloue les sommets et calcule leur degre (sans tenir compte des doublons)
	int nbloop = 0;
	G.sommets = new ArrayList<>(G.nbr_sommet);
	for(int i=0;i<G.nbr_sommet;i++){
	    Sommet s = new Sommet(i);
            G.sommets.add(s);
            G.arcs.arcs.put(s, new HashSet<>());
        }

	for(int i = 0; i< l; i++){
            int x, y; // juste pour la lisibilité
            x = lus[i][0];
            y = lus[i][1];
            if(x==y ) { // nous ignorons les boucles
                nbloop++;
                continue;
            }

            (G.sommets.get(x).degre)++; // arete x--y augmente le degre de x 
            (G.sommets.get(y).degre)++; // ...et celui de y 
        }
        
	if(nbloop > 0){
            G.boucle = nbloop;
        }

	// tpasse 4 :  ajoute les aretes. 
	// d'abord allouons les tableaux d'adjacance
	for(int i=0;i<G.nbr_sommet;i++) {
	    if(G.sommets.get(i).degre > 0)  
		G.sommets.get(i).adjacence = new int[G.sommets.get(i).degre];
	    G.sommets.get(i).degre=0; // on remet le degre a zero car degre pointe la première place libre où insérer un élément pour la troisième passe
	}

	for(int i = 0; i< l; i++){
            int x, y; // juste pour la lisibilité
            x = lus[i][0];
            y = lus[i][1];
            if(x==y)
                continue;
            G.sommets.get(x).adjacence[G.sommets.get(x).degre++] = y;
            G.sommets.get(y).adjacence[G.sommets.get(y).degre++] = x;
        }
	
	// passe 5 :  deboublonage, calul de m et des degres reels
	int nbdoubl=0;
	G.degreMax = 0;
	G.somdmax = 0; 
	for(int i=0;i<G.nbr_sommet;i++) {
	    if(G.sommets.get(i).degre > 0) { 
		Arrays.sort(G.sommets.get(i).adjacence); 		    // on commence par trier la liste d'adjacance.
		for(int j = G.sommets.get(i).degre-2; j >=0; j--)  
		    if(G.sommets.get(i).adjacence[j] == G.sommets.get(i).adjacence[j+1]) {    // du coup les doublons deviennent consécutifs 
			// oh oh un doublon
			nbdoubl++;
			// on echange le doublon avec le dernier element que l'on supprime
			// boucle de droite a gauche pour eviter de deplacer un autre doublon
			G.sommets.get(i).adjacence[j+1]=G.sommets.get(i).adjacence[G.sommets.get(i).degre-1];
			G.sommets.get(i).degre--;
		    }
	    }
	    // on calcule le degré max maintenant, et le nombre d'arêtes
	    if(G.degreMax < G.sommets.get(i).degre){
                G.degreMax =  G.sommets.get(i).degre;
                G.somdmax = i; // on sait qui atteint le degré max
            }
	    G.nbr_arete += G.sommets.get(i).degre;
	}
	    
	// on a compté chaque arête deux fois et chaqyue doublon aussi
	G.nbr_arete /= 2;
	nbdoubl /= 2;
        G.doublons = nbdoubl;
        
        //supprimer les sommet de degré 0
        /*for (int i = 0; i < G.sommets.size(); i++){
            if(G.sommets.get(i).degre == 0){
                G.sommets.remove(G.sommets.get(i));
                G.nbr_sommet--;
                i -= 1;
            }
        }*/
        
	//if(nbdoubl >0) System.out.println(nbdoubl+" doublons ont ete supprimes");
        return 1;
    }
    
    public int intValueOf(String str){
        int ival = 0, idx = 0, end;
        boolean sign = false;
        char ch;

        if( str == null || ( end = str.length() ) == 0 || ( ( ch = str.charAt( 0 ) ) < '0' || ch > '9' )
              && ( !( sign = ch == '-' ) || ++idx == end || ( ( ch = str.charAt( idx ) ) < '0' || ch > '9' ) ) )
            throw new NumberFormatException( str );

        for(;; ival *= 10 ){
            ival += '0'- ch;
            if( ++idx == end ) return sign ? ival : -ival;
            if( ( ch = str.charAt( idx ) ) < '0' || ch > '9' ) throw new NumberFormatException( str );
        }
    }
    
    /**
     * Split un String en plusieurs morceaux (Format Stanford d'une arrete) et transforme le string en int
     * @param line
     * @return cluster
     */
    public ArrayList<Integer> parseLine(String line){
        String[] s = line.split(" ");
        ArrayList<Integer> l = new ArrayList<>();
        for (String item : s) {
            try{
                l.add(intValueOf(item));
                //l.add(Integer.parseInt(item));
            }catch(NumberFormatException e){
                System.out.println("Bad format .clu file, please check it again !");
                System.exit(0);
            }
        }
        //System.out.println("");
        return l;
    }
    
    public void parseClusters(String path, Partition partition){
        FileReader f = null;
        BufferedReader br;
        String line;
        int cpt = 0;

        try {
            f = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.out.println("Erreur entree/sortie sur " + path);
            exit(1);
        }

        br = new BufferedReader(f);
        try {
            while ((line = br.readLine()) != null) {
                //partition.addCluster(splitAndParseInt(line, new Cluster()));
                Cluster clu = new Cluster();
                clu.sommets = parseLine(line);
                clu.calcul_Som_Degre(G);
                partition.addCluster(clu);
                cpt++;
            }
        } catch (IOException ex) {
            System.err.println("Erreur pendant la lecture du fichier <"+ path + ">");
        }

        if (cpt == 0) {
            System.out.println("Erreur: Fichier <" + path + "> vide");
            System.exit(0);
        }

        try {
            br.close();
            f.close();
        } catch (IOException ex) {
            System.err.println("Erreur lors de la fermeture du BufferReader ou du FileReader !");
        }
        
    }
    
}