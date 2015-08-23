import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class pingUI extends Application {

    protected String initText = "192.168.";
    protected Label lblStart = new Label("Statr IP:");
    protected Label lblEnd = new Label("End IP:");
    protected Label lblDelay = new Label("Delay (ms):");
    protected TextField txtStart = new TextField(initText + "0.1");
    protected TextField txtEnd = new TextField(initText + "0.10");
    protected TextField txtDelay = new TextField("1200");
    protected TextArea txtLog = new TextArea("Log ...");
    protected Button btnScan = new Button("Scan");
    protected VBox box1 = new VBox();
    protected String outStr = "";

    @Override
    public void start(Stage primaryStage) {
        txtLog.setEditable(false);

        btnScan.setOnAction(event -> ping());

        /*
        btnScan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ping();
            }
        });
        */

        StackPane root = new StackPane();
        root.getChildren().add(box1);
        box1.getChildren().add(btnScan);
        box1.getChildren().add(lblStart);
        box1.getChildren().add(txtStart);
        box1.getChildren().add(lblEnd);
        box1.getChildren().add(txtEnd);
        box1.getChildren().add(lblDelay);
        box1.getChildren().add(txtDelay);
        box1.getChildren().add(txtLog);

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setTitle("IP SCAN - www.mshams.ir");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void ping() {

        int ipStart = Integer.parseInt(txtStart.getText().split("\\.")[3]);
        int ipEnd = Integer.parseInt(txtEnd.getText().split("\\.")[3]);
        int wait = Integer.parseInt(txtDelay.getText());
        String ipStr = "192.168.0.";

        txtLog.clear();
        txtLog.appendText("Start...\n");

        ExecutorService es = Executors.newFixedThreadPool(ipEnd - ipStart + 1);
        List<Future<Integer>> ft = new ArrayList<>();
              

        for (int i = ipStart; i <= ipEnd; i++) {
            Callable<Integer> c = new pingCal(ipStr + Integer.toString(i), wait);
            ft.add(es.submit(c));
        }

        for (int i = ipStart; i <= ipEnd; i++) {
            try {
                Integer result = ft.get(i - ipStart).get();
                String out;

                if (result > 0) {
                    out = String.format("Scan:%s Alive TTL:%d", ipStr + Integer.toString(i), result);
                } else {
                    out = String.format("Scan:%s Dead", ipStr + Integer.toString(i));
                }
                
                txtLog.appendText(out + "\n");
                //System.out.println(out);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
