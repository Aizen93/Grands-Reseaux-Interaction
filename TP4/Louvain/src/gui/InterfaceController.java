package gui;

import core.Sommet;
import core.Graphe;
import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Oussama
 */
public class InterfaceController implements Initializable {
    @FXML
    ProgressIndicator bar;
    @FXML
    TitledPane popup;
    @FXML
    AnchorPane titledpane_pane, anchorPane;
    @FXML
    ScrollPane scroll, scroll2;
    @FXML
    StackedAreaChart<Number, Number> areaChart;
    @FXML
    Text graph_info;
    @FXML
    TableView<DegreDistribution> distribution;
    @FXML
    TableColumn<Integer, Integer> degre_view, total_view;
    
    private final ObservableList<DegreDistribution> distribution_model = FXCollections.observableArrayList();
    private final DialogPopUp dialog = new DialogPopUp();
    Pane pane;
    Font arcade = Font.loadFont("file:src/assets/images/arcade.ttf", 12);
    int lehmer = 1, rangeX = 0, rangeY = 0;
    Random rand = new Random();
    
    private int lehmerRandom(int min, int max, int seed){
        lehmer = seed >> 16 | seed;
        lehmer += 0xe120fc15;
        long tmp;
        tmp = (long)lehmer * 0x4a39b70d;
        long m1 = (tmp << 32) ^ tmp;
        tmp = m1 * 0x12fad5c9;
        long m2 = (tmp << 32) ^ tmp;
        return (Math.abs((int)m2) % (max - min))+ min;
    }
    
    public void buildArcs(Graphe graphe){
        graphe.getListSommets().forEach((sommet) -> {
            try{
                for(int adj : sommet.getAdjacence()){
                    if(!graphe.arcs.isExist(sommet, adj)){
                        graphe.arcs.addArc(sommet, adj);
                        graphe.arcs.addArc(graphe.getSommet(adj), sommet.getID());
                        Sommet B = graphe.getSommet(adj);
                        Line line = new Line(sommet.getCenterX(), sommet.getCenterY(), B.getCenterX(), B.getCenterY());
                        line.setStrokeWidth(0.5);
                        if(sommet.degre == graphe.degreMax || B.degre == graphe.degreMax) line.setStroke(Color.YELLOW);
                        pane.getChildren().add(line);
                    }
                }
            }catch(NullPointerException e){
            }
        });
        graphe.getListSommets().forEach((sommet) -> {
            sommet.toFront();
        });
    }
    
    private void buildNodes(Graphe graphe, Sommet sommet, int x, int y){
        sommet.setGUINode(x, y, (sommet.getDegre() == graphe.degreMax ? 10 : 4), (sommet.getDegre() == graphe.degreMax ? Color.RED : Color.GREEN));
        sommet.applyhoverEffect();
        sommet.applyHandlers(popup, titledpane_pane, scroll, arcade);

        pane.getChildren().add(sommet);
    }
    
    private void fillDistributionView(Graphe g){
        distribution_model.clear();
        
        int[] dgr = new int[g.degreMax + 1];
	for(int i = 0; i < g.sommets.size(); i++)
	    dgr[ g.sommets.get(i).degre ]++;
	for(int i = 0; i <= g.degreMax; i++) 
            distribution_model.add(new DegreDistribution(i, dgr[i]));
        
        degre_view.setCellValueFactory(new PropertyValueFactory<>("Degre"));
        total_view.setCellValueFactory(new PropertyValueFactory<>("Total_node"));
        
        distribution.setItems(distribution_model);
        fillAreaChart(g, dgr);
    }
    
    private void fillAreaChart(Graphe g, int[] dgr){
        areaChart.getData().clear();
        areaChart.setTitle("Suivi de la distribution de degre !");
        XYChart.Series<Number, Number> seriesdegre = new XYChart.Series<>();
        seriesdegre.setName("Total node / degre");
        
        for(int i = 0; i < dgr.length; i++){
            seriesdegre.getData().add(new XYChart.Data(i, dgr[i]));
        }
        areaChart.getData().add(seriesdegre);
    }
    
    private String getPath(){
        String path = "";
        File file = this.dialog.openFile(anchorPane);
        if (file != null) {
            path = file.getPath();
        }
        return path;
    }
    
    @FXML
    private void randomVisual(ActionEvent event) {
        pane.getChildren().clear();
        lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                Graphe graphe = new Graphe();
                graphe.generateGraphe(path);
                succeeded();
                int range = (graphe.nbr_sommet < 700 ? 700 : graphe.nbr_sommet) + graphe.nbr_sommet / 2;
                pane.setPrefSize(range, range);
                Platform.runLater(() -> {
                    graphe.getListSommets().forEach((sommet) -> {
                        int x = lehmerRandom(5, range - 5, rand.nextInt());
                        int y = lehmerRandom(5, range - 5, rand.nextInt());
                        buildNodes(graphe, sommet, x, y);
                    });
                    buildArcs(graphe);
                    fillDistributionView(graphe);
                    bar.setVisible(false);
                });
                graph_info.setText(graphe.getStringResult());
                graph_info.setFill(Color.BLACK);
                graph_info.setFont(arcade);
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    @FXML
    private void gridVisual(ActionEvent event) {
        pane.getChildren().clear();
        lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                Graphe graphe = new Graphe();
                graphe.generateGraphe(path);
                succeeded();
                rangeX = (graphe.nbr_sommet < 200 ? 200 : graphe.nbr_sommet/2);
                
                Platform.runLater(() -> {
                    int x = 10;
                    rangeY = 10;
                    for (Sommet sommet : graphe.getListSommets()) {
                        buildNodes(graphe, sommet, x, rangeY);
                        x += 40;
                        if(x >= rangeX){
                            x = 10;
                            rangeY += 40;
                        }
                    }
                    pane.setPrefSize(rangeX+10, rangeY+10);
                    buildArcs(graphe);
                    fillDistributionView(graphe);
                    bar.setVisible(false);
                });
                graph_info.setText(graphe.getStringResult());
                graph_info.setFill(Color.BLACK);
                graph_info.setFont(arcade);
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    @FXML
    private void clearAll(ActionEvent event) {
        pane.getChildren().clear();
    }
    
    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PauseTransition delay = new PauseTransition(Duration.millis(1000));
        delay.setOnFinished(eventt -> {
            pane = new Pane();
            pane.setPrefSize(1500, 1000);
            pane.setStyle("-fx-background-color: gray;");

            ZoomableScrollPane sc = new ZoomableScrollPane(pane);
            scroll2.setContent(sc.outerNode(sc.zoomNode));
            
        } );
        delay.play();
    }    
    
}
