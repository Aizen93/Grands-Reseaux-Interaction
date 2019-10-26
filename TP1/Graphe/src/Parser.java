import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;

/**
 * @author Aouessar, Melila
*/
public class Parser {
    Graphe graphe;
    
    public Parser(){
        this.graphe = new Graphe();
    }
    
    public void parser(String name) {
        graphe.setNom_Graphe(name);
        
        FileReader f = null;
        BufferedReader br;
        String line;
        int [] tmp = new int[2];
        int compteur = 0;

        try {
            f = new FileReader(name);
        } catch (Exception e) {
            System.out.println("Erreur entree/sortie sur " + name);
            exit(1);
        }

        br = new BufferedReader(f);

        try {
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) != '#') {
                    graphe.stringToTabInt(line, tmp);
                    try {
                        graphe.addSommet(tmp[0], tmp[1]);
                    } catch (Exception e) {
                        System.out.println("Erreur format ligne " + compteur);
                        exit(1);
                    }
                    compteur++;
                }
            }
        } catch (IOException ex) {
            System.err.println("Erreur pendant la lecture du fichier "+ name);
        }

        if (compteur == 0) {
            System.out.println("Erreur: Fichier " + name + " vide");
        }

        try {
            br.close();
            f.close();
        } catch (IOException ex) {
            System.err.println("Erreur lors de la fermeture du BufferReader ou du FileReader !");
        }
        
        for(int i = 0; i < graphe.getIdSommetMax()+1; i++){
            if(graphe.getSommetByID(i) == null){
                graphe.addSommetDegreZero(i, new Sommet(i));
            }
        }
    }
    
    public void printResult(){
         graphe.printResult();
    }
     
}