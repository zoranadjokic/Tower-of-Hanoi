package TowerOfHanoi;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class TowerOfHanoi extends Application {

    private int NUM_CIRCLES;

    //Optional bc there can be no selected circle at the moment
    //we start with empty - no selected circle
    private Optional<Circle> selectedCircle = Optional.empty();



    @Override
    public void start(Stage primaryStage) throws Exception {



        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);


        Pane pane = new Pane();
        pane.setPrefSize(1200,400);


        Button btEnter = new Button("Enter number of circles");
        btEnter.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("3");
            dialog.setTitle("Tower of Hanoi Game");
            dialog.setHeaderText("How many circles do you want?");
            dialog.setContentText("Please enter number of circles:");


            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                NUM_CIRCLES = Integer.parseInt(result.get());
                for (int i = 0; i < 3; i++){
                    Tower tower = new Tower(i*400, 0);

                    //at the beginning we put all circles at the first tower
                    if(i==0){
                        for(int j = NUM_CIRCLES; j > 0; j--){
                            //we dont want any fill color, thats why is null
                            Circle circle = new Circle(30 + 20*j, null);
                            circle.setStroke(Color.BLUE);
                            circle.setStrokeWidth(circle.getRadius() / 30.0);

                            tower.addCircle(circle);
                        }
                    }
                    pane.getChildren().add(tower);
                }
            }
        });


        Button btRules = new Button("Rules");

        btRules.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rules");
            alert.setHeaderText(null);
            String s = "The Tower of Hanoi is a mathematical game or puzzle." +
                    " It consists of three rods and a number of circles of different sizes," +
                    " which can be put around any rod. " +
                    "The puzzle starts with the circles in ascending " +
                    "order of size on one rod, the smallest at the inside. " +
                    "The objective of the puzzle is to move all circles to another rod," +
                    " obeying the following simple rules: \n" +
                    " 1. Only one circle can be moved at a time. \n" +
                    " 2. Each move consists of taking the inner circle from one of the rods" +
                    " and placing it inside of already existing circles of another rod " +
                    " or on an empty rod.\n" +
                    " 3. You can only place smaller circles inside bigger ones, " +
                    " you can not put bigger circles around smaller ones";
            alert.setContentText(s);

            alert.showAndWait();
        });


        Button btInstructions = new Button("Instructions");

        btInstructions.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Instructions");
            alert.setHeaderText(null);

            String s = "By clicking on the rod you can " +
                    "pick up the smallest circle from that rod. " +
                    "You can place selected circle by clicking on the rod you wan to place it.";

            alert.setContentText(s);
            alert.showAndWait();
        });

        HBox hbTop = new HBox(10);
        hbTop.setAlignment(Pos.CENTER);


        hbTop.getChildren().addAll(btRules, btInstructions);


        HBox hbBottom = new HBox(10);
        Button btRestart = new Button("Restart");
        Button btExit = new Button("Exit");
        hbBottom.setAlignment(Pos.CENTER);

        btExit.setOnAction(e -> {
            primaryStage.close();
        });

        btRestart.setOnAction(e -> {TowerOfHanoi app = new TowerOfHanoi();
            primaryStage.close();
            try {
                app.start(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        hbBottom.getChildren().addAll(btRestart, btExit);


        root.getChildren().addAll(btEnter,hbTop, pane, hbBottom);


        primaryStage.setScene(new Scene(root,1200,700));
        primaryStage.setTitle("Tower of Hanoi Game");
        primaryStage.show();

    }


    private class Tower extends StackPane{

        public Tower(int x, int y){
            setTranslateX(x);
            setTranslateY(y);

            setPrefSize(400,400);

            Rectangle rod = new Rectangle(25,25);
            rod.setFill(Color.PURPLE);
            rod.setStroke(Color.PURPLE);

            rod.setOnMouseClicked(e -> {
                if(selectedCircle.isPresent()){
                    addCircle(selectedCircle.get());


                    //once we add circle to the tower we no longer
                    // have selected circle
                    selectedCircle = Optional.empty();
                }
                else{
                    //we need to select circle
                    //if we attempt to select circle from the tower which has
                    //no circle, selected circle will be null
                    selectedCircle = Optional.ofNullable(getTopMost());
                }
            });

            getChildren().add(rod);

        }

        private Circle getTopMost(){
            return getChildren().stream()
                    .filter(n -> (n instanceof Circle))
                    .map(n -> (Circle) n)
                    .min(Comparator.comparingDouble(n -> n.getRadius()))
                    .orElse(null);
        }

        public void addCircle(Circle circle){

            Circle topCircle = getTopMost();

            if(topCircle == null){
                getChildren().add(circle);
            }
            else{
                if(circle.getRadius() < topCircle.getRadius()) {
                    getChildren().add(circle);
                }
            }

        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
