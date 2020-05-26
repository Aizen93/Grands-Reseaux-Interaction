package gui;

import core.Graphe;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Permet d'ouvrir des dialogs avec users pour communiquer des erreur ou demander des infos
 * @author Oussama
 */
public class DialogPopUp {
    
    /**
     * Constructeur d'alerte
     * Permet d'ouvrir des dialogs avec users pour communiquer des erreur ou demander des infos
     * @see Dialog & alert java class
     */
    public DialogPopUp() {
        //requires nothing
    }
    
    /**
     * Affiche un message d'erreur avec l'exception catché a l'utilisateur
     * @param ex
     * @param context 
     */
    public void showAlert(Exception ex, String context){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Alert, an exception catched somewhere");
        alert.setContentText(context);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace :");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    /**
     * Affiche un message a l'utilisateur pour l'informer
     * @param m 
     */
    public void showMessage(String m){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(m);

        alert.showAndWait();
    }
    
    /**
     * filechooser configuration
     * @param fileChooser 
     */
    public static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("View home files");
    }
    
    /**
     * ouvre un fichier 
     * @param mainAnchor
     * @return un fichier
     */
    public File openFile(AnchorPane mainAnchor){
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(mainAnchor.getScene().getWindow());
        return file;
    }
    
    /**
     * renvoie les coordonnées d'un cube et sa taille
     * @return tableau de int (n, x,y,z, width)
     */
    public int[] getStartEndNodes(Graphe graphe){
        int[] path = new int[]{-1, -1, -1};
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Shortest Path between node A and a node B !");
        ButtonType obuttonType = new ButtonType("OK");
        ButtonType cbuttonType = new ButtonType("Cancel");
        dialog.getDialogPane().getButtonTypes().addAll(obuttonType, cbuttonType);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(obuttonType);
        final Button btCancel = (Button) dialog.getDialogPane().lookupButton(cbuttonType);
        Group groupe = new Group();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        Text m = new Text();
        m.setFill(Color.RED);
        m.setFont(Font.loadFont("file:src/assets/images/arcade.ttf", 10));
        
        TextField start_node = new TextField();
        start_node.setPromptText("Source node ID...");
        
        TextField stop_node = new TextField();
        stop_node.setPromptText("Stop node ID...");
        
        TextField dest_node = new TextField();
        dest_node.setPromptText("Stop node ID...");
        
        gridPane.add(new Label("From node"), 0, 0);
        gridPane.add(start_node, 1, 0);
        gridPane.add(new Label("Passing by node"), 2, 0);
        gridPane.add(stop_node, 3, 0);
        gridPane.add(new Label("To node"), 4, 0);
        gridPane.add(dest_node, 5, 0);
        
        groupe.getChildren().addAll(m, gridPane);
        
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            try{
                int node1 = Integer.parseInt(start_node.getText());
                int node2 = Integer.parseInt(stop_node.getText());
                int node3 = Integer.parseInt(dest_node.getText());
                if(node1 < 0 || node2 < 0 || node3 < 0){
                    m.setText("One of the textfields are negative value\nUn des Text field est negative!");
                    event.consume();
                } else if(graphe.getSommet(node1) == null){
                    m.setText("Start node doesn't exist !");
                    event.consume();
                } else if(graphe.getSommet(node2) == null){
                    m.setText("Stop node doesn't exist !");
                    event.consume();
                } else if(graphe.getSommet(node3) == null){
                    m.setText("Destination node doesn't exist !");
                    event.consume();
                }else{
                    path[0] = node1;
                    path[1] = node2;
                    path[2] = node3;
                   dialog.close(); 
                }
            }catch(NumberFormatException e){
                m.setText("Bad format of one of the textfields below !\n"
                        + "Un des textFields est mal ecrit !");
                event.consume();
            }
        });
        
        btCancel.addEventFilter(ActionEvent.ACTION, event -> {
            dialog.close();
        });
        
        dialog.getDialogPane().setContent(groupe);
        dialog.showAndWait();
        
        return path;
    }
    
}
