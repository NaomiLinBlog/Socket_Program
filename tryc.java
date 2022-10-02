package trybind;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class tryc extends Application implements Initializable {
	// IO streams
	DataOutputStream toServer = null;
	DataInputStream fromServer = null;
	Socket socket;
	@FXML
	public TextField guessInt;
	@FXML
	public Button okButton;
	@FXML
	public Button try_again;
	@FXML
	public Button closeSocket;
	@FXML
	public Button guess_Continue;
	@FXML
	public Label hint;
	@FXML
	public ImageView winner;
	@FXML
	public ImageView looser;
	@FXML
	public ImageView initial;

	@Override // Override the start method in the Application class
	public void start(Stage mainStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("clientScene.fxml"));
		mainStage.setTitle("Client");
		mainStage.setScene(new Scene(root));
		mainStage.show();

	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */

	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	public void guessContinue(ActionEvent event) {// ���U�Ȥ�ݪ� ok Button��Ĳ�o
		try {
			Button button = (Button) event.getSource();
			button.requestFocus();
			String function = button.getText();

			String guessInput="";
			if (function.equals("ok"))
				guessInput = guessInt.getText();
			else {
			    Stage stage = (Stage)closeSocket.getScene().getWindow();
			    // do what you have to do
			    guessInput = "close_socket";
			    toServer.writeUTF(guessInput);
			    stage.close();
				
				//socket.shutdownOutput();
				
			}

			toServer.writeUTF(guessInput);
			toServer.flush();

			String result = fromServer.readUTF();// �Nserver�ǨӪ��T���s�Jresult

			if (result.equals("Win")) {// �Yresult��"Win"
				initial.setVisible(false);
				winner.setVisible(true);// �Ȥ����ܳӧQ�Y��
				hint.setText("You Win!");// �Ȥ�����"You Win!"
			} else if (result.equals("Loose")) {// �Yresult��"Loose"
				initial.setVisible(false);
				looser.setVisible(true);// �Ȥ����ܥ����Y��
				hint.setText("You Loose!");// �Ȥ�����"You Loose!"
			}
			System.out.print(result);

		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

	@FXML
	public void again() {// client�Q�n�A�i��C�����A��l�Ʊ���
		try {

			String guessInput = "again";
			initial.setVisible(true);
			looser.setVisible(false);
			winner.setVisible(false);
			hint.setText("Let's Game!");
			toServer.writeUTF(guessInput);
			toServer.flush();

			String result = fromServer.readUTF();

		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

	@FXML
	public void connectServer() {// ���ճs�uServer
//
		try {
			// Create a socket to connect to the server
			socket = new Socket("localhost", 8888);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			System.out.print(ex.toString() + '\n');
		}

//      
//    

//	  }
//  @Override
//  public void initialize(URL arg0, ResourceBundle arg1) {
//  	// TODO Auto-generated method stub
//  	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}