package core;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Sommet extends Circle{
    public int ID ; // nom numéro sommet
    public int degre; // son degré.
    public int []adjacence; // tableau d'adjacence. une case = un numero de voisin. sa longueur est degré
    Color default_color;
    double radius;
    Boolean renderer = false;

    public Boolean isRendered() {
        return renderer;
    }
    public void setRendered(Boolean b){
        this.renderer = b;
    }
    
    Sommet(int i) {
	this.ID = i;
	this.degre = 0; 
    }
    
    /**
     * Cherche si un sommet existe deja dans la liste d'adjacence ou pas
     * @param n
     * @return un boolean
     */
    public boolean contient(Sommet n) {
        if(adjacence != null) {
            for (int i = 0; i < adjacence.length; i++) {
                if (adjacence[i] == n.ID)
                    return true;
            }
        }
        return false;
    }
    
    public int getDegre() {
        return degre;
    }

    public int[] getAdjacence() {
        return adjacence;
    }

    public int getID() {
        return ID;
    }
    
    public String getDetails(){
        String res = "- ID : "+ID
                +"\n---------------------------"
                +"\n- degre : "+degre
                +"\n---------------------------"
                +"\n- Adjacence : ";
        if(adjacence != null){
            for(Integer a : adjacence){
                res += a+", ";
            }
            res+="\n---------------------------";
            res+="\n- Il y a "+(adjacence.length - degre)+" doublons dans la liste d'ajacence";
        }
        return res;
    }
    
    /**
     * *************************** GUI ********************************
     */
    
    
    /**
     * Prepare node to be drawn
     * @param x
     * @param y
     * @param radius
     * @param color 
     */
    public void setGUINode(int x, int y, double radius, Color color){
        this.setFill(color);
        this.setRadius(radius);
        this.radius = radius;
        this.setCenterX(x);
        this.setCenterY(y);
        this.default_color = color;
    }
    
    public final void applyhoverEffect(){
        this.setOnMouseEntered((MouseEvent t) -> {
            this.setRadius(15);
        });
        this.setOnMouseExited((MouseEvent t) -> {
            this.setRadius(this.radius);
        });
    }
    
    public void applyHandlers(TitledPane t, AnchorPane p, ScrollPane s, Font font){
        this.setOnMouseClicked((MouseEvent m) -> {
            s.setFitToWidth(true);
            
            String info = getDetails();

            t.setVisible(true);
            t.setText("Node ID = "+getID());
            Text text = new Text(info);
            text.setFont(font);
            
            p.getChildren().add(text);
            s.setContent(text);
                
        });
    }
}