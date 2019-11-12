
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Parser {
    
    public Parser(){
        
    }
    
    public static void lecture(Graphe G, String filename) {
	// lecture du graphe, recopiée du TP1 
	
	// passe 1 : compte les lignes
	int compteur = 0; // sera le nombre de lignes du fichier
	try {
	    BufferedReader read = new BufferedReader(new FileReader(filename));
	    while (read.readLine() != null)
		compteur++;
	    read.close(); 
	}catch (IOException e) {
            System.out.println("Erreur entree/sortie sur "+filename);
            System.exit(1);
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
                        System.exit(1);
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
	    System.exit(1);
	}

	// passe 3 : alloue les sommets et calcule leur degre (sans tenir compte des doublons)
	int nbloop = 0;
	G.sommets = new Sommet[G.nbr_sommet];
	for(int i=0;i<G.nbr_sommet;i++)
	    G.sommets[i]=new Sommet(i);

	for(int i = 0; i< l; i++){
            int x, y; // juste pour la lisibilité
            x = lus[i][0];
            y = lus[i][1];
            if(x==y ) { // nous ignorons les boucles
                nbloop++;
                continue;
            }

            (G.sommets[x].degre)++; // arete x--y augmente le degre de x 
            (G.sommets[y].degre)++; // ...et celui de y 
        }
        
	if(nbloop > 0)
	    System.out.println(nbloop + " boucles ont été ignorées");

	// tpasse 4 :  ajoute les aretes. 
	// d'abord allouons les tableaux d'adjacance
	for(int i=0;i<G.nbr_sommet;i++) {
	    if(G.sommets[i].degre>0)  
		G.sommets[i].adjacence = new int[G.sommets[i].degre];
	    G.sommets[i].degre=0; // on remet le degre a zero car degre pointe la première place libre où insérer un élément pour la troisième passe
	}

	for(int i = 0; i< l; i++){
            int x, y; // juste pour la lisibilité
            x = lus[i][0];
            y = lus[i][1];
            if(x==y)
                continue;
            G.sommets[x].adjacence[G.sommets[x].degre++] = y;
            G.sommets[y].adjacence[G.sommets[y].degre++] = x;
        }
	
	// passe 5 :  deboublonage, calul de m et des degres reels
	int nbdoubl=0;
	G.degreMax = 0;
	G.somdmax = 0; 
	for(int i=0;i<G.nbr_sommet;i++) {
	    if(G.sommets[i].degre>0) { 
		Arrays.sort(G.sommets[i].adjacence); 		    // on commence par trier la liste d'adjacance.
		for(int j= G.sommets[i].degre-2;j>=0;j--)  
		    if(G.sommets[i].adjacence[j]==G.sommets[i].adjacence[j+1]) {    // du coup les doublons deviennent consécutifs 
			// oh oh un doublon
			nbdoubl++;
			// on echange le doublon avec le dernier element que l'on supprime
			// boucle de droite a gauche pour eviter de deplacer un autre doublon
			G.sommets[i].adjacence[j+1]=G.sommets[i].adjacence[G.sommets[i].degre-1];
			G.sommets[i].degre--;
		    }
	    }
	    // on calcule le degré max maintenant, et le nombre d'arêtes
	    if(G.degreMax < G.sommets[i].degre){
                G.degreMax =  G.sommets[i].degre;
                G.somdmax = i; // on sait qui atteint le degré max
            }
	    G.nbr_arete += G.sommets[i].degre;
	}
	    
	// on a compté chaque arête deux fois et chaqyue doublon aussi
	G.nbr_arete /= 2;
	nbdoubl /= 2;
	
	if(nbdoubl >0) System.out.println(nbdoubl+" doublons ont ete supprimes");
    }
    
}