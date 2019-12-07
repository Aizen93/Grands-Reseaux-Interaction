class Sommet {
    
    int ID ; // nom numéro pour débug seulement
    int degre; // son degré.
    int []adjacence; // tableau d'adjacence. une case = un numero de voisin. sa longueur est degré
    
    Sommet(int i) {
	this.ID = i;
	this.degre =0; 
    }
    
    /**
     * Cherche si un sommet existe deja dans la liste d'adjacence ou pas
     * @param n
     * @return un boolean
     */
    public boolean contient(Sommet n) {
        for (int i = 0; i < adjacence.length; i++) {
            if (adjacence[i] == n.ID)
                return true;
        }
        return false;
    }
}