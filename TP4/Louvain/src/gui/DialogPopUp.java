package gui;

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
     * Afiiche un message d'erreur avec l'exception catché a l'utilisateur
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
    
}
