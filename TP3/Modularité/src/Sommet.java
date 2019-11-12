class Sommet {
    
    int ID ; // nom numéro pour débug seulement
    int degre; // son degré.
    int []adjacence; // tableau d'adjacence. une case = un numero de voisin. sa longueur est degré
    
    Sommet(int i) {
	this.ID = i;
	this.degre =0; 
    }
}