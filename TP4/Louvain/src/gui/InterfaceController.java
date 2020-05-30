package gui;

import core.Cluster;
import core.Sommet;
import core.Graphe;
import gui.simulation3d.MathUtils;
import gui.simulation3d.World;
import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.text.TextFlow;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;

/**
 * FXML Controller class
 *
 * @author Oussama
 */
public class InterfaceController implements Initializable {
    /**
     * FXML variables using the ID's of the FXMLInterface file
     */
    @FXML
    ProgressIndicator bar;
    @FXML
    TitledPane popup;
    @FXML
    AnchorPane titledpane_pane, anchorPane;
    @FXML
    ScrollPane scroll, scroll2, cluster_info;
    @FXML
    StackedAreaChart<Number, Number> areaChart;
    @FXML
    Text graph_info;
    @FXML
    TextField path_text;
    @FXML
    TableView<DegreDistribution> distribution;
    @FXML
    TableColumn<Integer, Integer> degre_view, total_view;
    @FXML
    ToolBar toolbar;
    //-------------------------------------------------------------
    
    /**
     * General variables needed
     */
    private Pane pane;
    private final Font arcade = Font.loadFont("file:src/assets/images/arcade.ttf", 11);
    private final Random rand = new Random();
    private final ObservableList<DegreDistribution> distribution_model = FXCollections.observableArrayList();
    private final DialogPopUp dialog = new DialogPopUp();
    private final Group short_path = new Group();
    private int rangeX = 0, rangeY = 0;
    private Instant start;
    private Graphe graphe;
    //-------------------------------------------------------------
    
    
    
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
                        else line.setStroke(Color.ORANGE);
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
    
    private void buildNodes(Graphe graphe, Sommet sommet, int x, int y, Color color){
        sommet.setGUINode(x, y, (sommet.getDegre() == graphe.degreMax ? 10 : 4), (sommet.getDegre() == graphe.degreMax ? Color.RED : color));
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
    
    private void fillGeneralInfo(Graphe graphe, long time){
        graph_info.setText(graphe.getStringResult()+"\nBuild time : "+time+" ms");
        graph_info.setFill(Color.BLACK);
        graph_info.setFont(arcade);
    }
    
    private void fillClusterInfo(Graphe graphe){
        TextFlow flow = new TextFlow();
        Text info = new Text(graphe.partition.getInfo());
        info.setFill(Color.BLACK);
        info.setFont(arcade);
        flow.getChildren().add(info);
        for(int i = 0; i < graphe.partition.size(); i++){
            Cluster clu = graphe.partition.getCluster(i);
            clu.setColor(MathUtils.randomColor());
            Text tmp = new Text("\n Cluster " + i +" : ->" + clu.size()+"\n");
            tmp.setFill(clu.getColor());
            tmp.setFont(arcade);
            flow.getChildren().add(tmp);
        }
        cluster_info.setContent(flow);
        flow.setPrefSize(cluster_info.getWidth()-5, cluster_info.getHeight());
        path_text.setText("Best modularity : " + graphe.partition.getModularite());
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
        MathUtils.lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                Instant start = Instant.now();
                graphe = new Graphe();
                if(graphe.generateGraphe(path) == -1) this.cancel();
                if(!this.isCancelled()){
                    int range = (graphe.nbr_sommet < 700 ? 700 : graphe.nbr_sommet) + graphe.nbr_sommet / 2;
                    pane.setPrefSize(range, range);
                    Platform.runLater(() -> {
                        graphe.getListSommets().forEach((sommet) -> {
                            int x = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
                            int y = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
                            buildNodes(graphe, sommet, x, y, Color.GREEN);
                        });
                        buildArcs(graphe);
                        Instant finish = Instant.now();
                        long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
                        fillGeneralInfo(graphe, timeElapsed);
                        fillDistributionView(graphe);
                        succeeded();
                    });
                }
                bar.setVisible(false);
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    @FXML
    private void gridVisual(ActionEvent event) {
        pane.getChildren().clear();
        MathUtils.lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                Instant start = Instant.now();
                graphe = new Graphe();
                if(graphe.generateGraphe(path) == -1) this.cancel();
                if(!this.isCancelled()){
                    rangeX = (graphe.nbr_sommet < 200 ? 200 : graphe.nbr_sommet/2);
                    Platform.runLater(() -> {
                        int x = 10;
                        rangeY = 10;
                        for (Sommet sommet : graphe.getListSommets()) {
                            buildNodes(graphe, sommet, x, rangeY, Color.GREEN);
                            x += 40;
                            if(x >= rangeX){
                                x = 10;
                                rangeY += 40;
                            }
                        }
                        pane.setPrefSize(rangeX+10, rangeY+10);
                        buildArcs(graphe);
                        Instant finish = Instant.now();
                        long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
                        fillGeneralInfo(graphe, timeElapsed);
                        fillDistributionView(graphe);
                        succeeded();
                    });
                }
                bar.setVisible(false);
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    @FXML
    private void clusterRandomVisual(ActionEvent event){
        pane.getChildren().clear();
        MathUtils.lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                Instant overall = Instant.now();
                startTime(Instant.now());
                graphe = new Graphe();
                graphe.generateGraphe(path);
                if(graphe.generateGraphe(path) == -1) this.cancel();
                if(!this.isCancelled()){
                    graphe.calculateLouvain("src/assets/ClusterLouvain.clu");
                    finishTime(Instant.now(), "Fini graphe in");
                    int range = (graphe.nbr_sommet < 700 ? 700 : graphe.nbr_sommet) + graphe.nbr_sommet / 2;
                    pane.setPrefSize(range, range);
                    Platform.runLater(() -> {
                        startTime(Instant.now());
                        graphe.partition.getPartition().forEach((clu) -> {
                            for(int i = 0; i < clu.size(); i++){
                                int x = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
                                int y = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
                                buildNodes(graphe, graphe.getSommet(clu.getNodeID(i)), x, y, clu.getColor());
                            }
                        });
                        finishTime(Instant.now(), "Fini buildnodes in");

                        startTime(Instant.now());
                        buildArcs(graphe);
                        finishTime(Instant.now(), "Fini ARCS in");

                        Instant finish = Instant.now();
                        long timeElapsed = Duration.between(overall, finish).toMillis();  //in millis
                        fillGeneralInfo(graphe, timeElapsed);

                        startTime(Instant.now());
                        fillDistributionView(graphe);
                        finishTime(Instant.now(), "Fini TableView in");
                        
                        fillClusterInfo(graphe);
                        succeeded();
                    });
                }
                bar.setVisible(false);
                return null;
            }
        };
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    @FXML
    private void visual3D(ActionEvent event){
        graphe = new Graphe();
        graphe.generateGraphe(getPath());
        World world = new World(graphe);
        world.buildWorld();
        //world.addLight();
    }
    
    public void animateEdgeBetween(Sommet som1, Sommet som2){
        Line line = new Line(som1.getCenterX(), som1.getCenterY(), som2.getCenterX(), som2.getCenterY());
        line.getStrokeDashArray().setAll(25d, 20d, 10d, 20d);
        line.setStrokeWidth(6);
        short_path.getChildren().add(line);
        
        som1.toFront();
        som2.toFront();
        double maxOffset = line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a - b);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        javafx.util.Duration.ZERO, 
                        new KeyValue(
                                line.strokeDashOffsetProperty(), 
                                0, 
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        javafx.util.Duration.seconds(1), 
                        new KeyValue(
                                line.strokeDashOffsetProperty(), 
                                maxOffset, 
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    @FXML
    private void drawShortestPath(ActionEvent event) {
        if(graphe != null){  
            short_path.getChildren().clear();
            pane.getChildren().remove(short_path);
            pane.getChildren().add(short_path);
            if(graphe != null){
                int[] nodes = DialogPopUp.getStartEndNodes(graphe, false);
                if(nodes[0] == -1) return;
                bar.setVisible(true);
                Task task = new Task<Void>() {
                @Override 
                public Void call() {
                    ArrayList<Integer> path = MathUtils.findShortestPath(graphe.sommets, nodes[0], nodes[2], graphe.nbr_sommet);
                    Platform.runLater(() -> {
                        if(!path.isEmpty()){
                            String s = "";
                            for (int i = 0; i < path.size() - 1; i++) {
                                s += path.get(i)+" --> ";
                                animateEdgeBetween(graphe.getSommet(path.get(i)), graphe.getSommet(path.get(i+1)));
                            }
                            s += path.get(path.size()-1);
                            toolbar.setVisible(true);
                            toolbar.toFront();
                            path_text.setText("Shortest path: " + s);
                        }else{
                            dialog.showMessage("- Given source and destination nodes are not connected\n"
                                + "- Le sommet de départ et d'arrivé ne sont pas connectées");
                        }
                        bar.setVisible(false);
                    });
                    succeeded();
                    System.out.println("FINI");
                    return null;
                    }
                };
                bar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();

            }
        }else{
            dialog.showMessage("Nothing imported yet, Please import a graph "
                    + "using one of the display buttons");
        }
    } 
    
    @FXML
    private void drawShortestPathThroughStop(ActionEvent event) {
        if(graphe != null){  
            short_path.getChildren().clear();
            pane.getChildren().remove(short_path);
            pane.getChildren().add(short_path);
            int[] nodes = dialog.getStartEndNodes(graphe, true);
            if(nodes[0] == -1) return;
            if(graphe != null){
                bar.setVisible(true);
                Task task = new Task<Void>() {
                @Override 
                public Void call() {
                    ArrayList<Integer> path = MathUtils.findAllShortestPaths(nodes[0], nodes[1], nodes[2], graphe);
                    Platform.runLater(() -> {
                        if(!path.isEmpty()){
                            String s = ""; 
                            for (int i = 0; i < path.size() - 1; i++) {
                                s += (path.get(i) == nodes[1] ? "["+nodes[1]+"]" : ""+path.get(i))+" --> ";
                                animateEdgeBetween(graphe.getSommet(path.get(i)), graphe.getSommet(path.get(i+1)));
                            }
                            s += path.get(path.size()-1);
                            toolbar.setVisible(true);
                            toolbar.toFront();
                            path_text.setText("Shortest path: " + s);
                        }else{
                            dialog.showMessage("- Given source and destination nodes are not connected\n"
                                + "- Le sommet de départ et d'arrivé ne sont pas connectées");
                        }
                        bar.setVisible(false);
                    });
                    succeeded();
                    System.out.println("FINI");
                    return null;
                    }
                };
                bar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();

            }
        }else{
            dialog.showMessage("Nothing imported yet, Please import a graph "
                    + "using one of the display buttons");
        }
    }
    
    @FXML
    private void testAlgos(ActionEvent event){
        pane.getChildren().clear();
        MathUtils.lehmer = 0;
        String path = getPath();
        bar.setVisible(true);
        Task task = new Task<Void>() {
            @Override 
            public Void call() {
                startTime(Instant.now());
                graphe = new Graphe();
                graphe.generateGraphe(path);
                finishTime(Instant.now(), graphe.nbr_sommet+" "+graphe.nbr_arete +"Graphe parseé en : ");
                
                 
                
                succeeded();
                Platform.runLater(() -> {
                    
                    bar.setVisible(false);
                });
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
     * This method is used for debugging to catch starting time of an execution
     * to calculate time
     * @param start 
     */
    private void startTime(Instant start){
        this.start = start;
    }
    
    /**
     * Used for debugging to catch finish time of an execution and then draw a message
     * to calculate time
     * @param finish
     * @param message 
     */
    private void finishTime(Instant finish, String message){
        long timeElapsed = Duration.between(this.start, finish).toMillis();
        System.out.println(message + " : "+timeElapsed + " ms");
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PauseTransition delay = new PauseTransition(javafx.util.Duration.millis(1000));
        delay.setOnFinished(event -> {
            pane = new Pane();
            pane.setPrefSize(1500, 1000);
            pane.setStyle("-fx-background-color: gray;");

            ZoomableScrollPane sc = new ZoomableScrollPane(pane);
            scroll2.setContent(sc.outerNode(sc.zoomNode));
        } );
        delay.play();
    }    
    
}
