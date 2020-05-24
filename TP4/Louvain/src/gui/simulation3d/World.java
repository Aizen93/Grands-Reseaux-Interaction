/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.simulation3d;

import core.Graphe;
import core.Sommet;
import java.util.HashMap;
import java.util.Random;
import javafx.geometry.Point3D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author Oussama
 */
public class World {
    Group root, subRoot, grouparcs;
    SubScene subsc;
    SimpleFPSCamera camera;
    Random rand = new Random();
    Text info = new Text();
    Graphe graphe;
    HashMap<Sommet, Box> objects;
    
    public World(Graphe graphe) {
        this.graphe = graphe;
        this.objects = new HashMap<>();
        this.root = new Group();
        //root.setCache(true);
        //root.setCacheHint(CacheHint.SPEED);
        
        this.grouparcs = new Group();
        this.subRoot = new Group();
        //subRoot.setCache(true);
        
        this.subsc = new SubScene(subRoot, 1300, 900, true, SceneAntialiasing.BALANCED);
        subsc.setFocusTraversable(true);
        subsc.setFill(Color.BLACK);
        subsc.setCache(true);
        subsc.setCacheHint(CacheHint.SPEED);
        this.root.getChildren().add(subsc);
        
        this.camera = new SimpleFPSCamera();
        this.subsc.setCamera(camera.getCamera());
        this.camera.loadControlsForSubScene(subsc);
        
        buildTextArea();
        
        Scene scene = new Scene(root, 1300, 900);
        scene.setFill(Color.BLACK);
        Stage stage = new Stage();
        
        stage.setTitle("3D Graph Simulator in real time");
        stage.setScene(scene);
        stage.show();
    }
    
    private void buildTextArea(){
        TextFlow tf = new TextFlow();
        tf.setPrefSize(200, 200);
        tf.relocate(5, 650);
        tf.setStyle( "-fx-border-color: white;-fx-border-insets: 3;\n"
                + "-fx-border-width: 1;-fx-border-style: dashed;\n");
        
        info.setFill(Color.WHITE);
        Text moreinfo = new Text("- Current speed: "+this.camera.getMoveSpeed()+"\n"
                +"- Total nodes : "+graphe.nbr_sommet+"\n"
                +"- Total Arcs : "+ graphe.nbr_arete+"\n");
        moreinfo.setFill(Color.WHITE);
        tf.getChildren().addAll(moreinfo, info);
        
        this.root.getChildren().add(tf);
    }
    
    public Cylinder createConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.2, height);
        line.setCache(true);
        line.setCacheHint(CacheHint.SPEED);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
    
    public void buildWorld(){
        int range = (graphe.nbr_sommet < 700 ? 700 : graphe.nbr_sommet) + graphe.nbr_sommet / 2;
        //create nodes in space
        graphe.getListSommets().forEach((sommet) -> {
            int x = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
            int y = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
            int z = MathUtils.lehmerRandom(5, range - 5, rand.nextInt());
            PhongMaterial material2;
            Box b2;
            if(sommet.degre == graphe.degreMax){
                material2 = new PhongMaterial(Color.RED);
                b2 = new Box(25, 25, 25);
            }
            else{
                material2 = new PhongMaterial(Color.GREEN);
                b2 = new Box(10, 10, 10);
            }
            b2.setCache(true);
            b2.setCacheHint(CacheHint.SPEED);
            b2.setTranslateX(x);
            b2.setTranslateY(y);
            b2.setTranslateZ(z);
            b2.setMaterial(material2);

            b2.setOnMouseClicked((e) -> {
                if(e.getButton() == MouseButton.PRIMARY){
                    this.info.setText(sommet.getDetails());
                    grouparcs.getChildren().clear();
                    try{
                        for(int adj : sommet.getAdjacence()){
                                Sommet B = graphe.getSommet(adj);
                                Point3D start = new Point3D(objects.get(B).getTranslateX(), objects.get(B).getTranslateY(), objects.get(B).getTranslateZ());
                                Point3D end = new Point3D(objects.get(sommet).getTranslateX(), objects.get(sommet).getTranslateY(), objects.get(sommet).getTranslateZ());

                                Cylinder c = createConnection(start, end);

                                if(sommet.degre == graphe.degreMax || B.degre == graphe.degreMax) c.setMaterial(new PhongMaterial(Color.ORANGE));
                                else c.setMaterial(new PhongMaterial(Color.WHITE));
                                grouparcs.getChildren().add(c);

                        }
                    }catch(NullPointerException ex){
                    }
                    subRoot.getChildren().remove(grouparcs);
                    subRoot.getChildren().add(grouparcs);
                }else if(e.getButton() == MouseButton.SECONDARY){
                    grouparcs.getChildren().clear();
                    subRoot.getChildren().remove(grouparcs);
                }
            });
            objects.put(sommet, b2);
            subRoot.getChildren().add(b2);

        });
    }
    
    public void addLight(){
        PointLight light = new PointLight();
        light.setColor(Color.WHITE);
        light.getTransforms().add(new Translate(-200, -200, -200));
        subRoot.getChildren().add(light);
    }
}
