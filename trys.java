package trybind;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
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

public class trys extends Application implements Initializable {
	@FXML
	public Label guesswhat;
	@FXML
	public Label required_name;
	@FXML
	public Label required_first_no;
	@FXML
	public Label required_sec_no;
	@FXML
	public Label operator;
	@FXML
	public Label first_number;
	@FXML
	public Label second_number;
	@FXML
	public Label big_or_small;
	@FXML
	public Label plus_or_minus;
	@FXML
	public Label computer_flag;
	@FXML
	public Label compare;
	@FXML
	public ImageView check;
	@FXML
	public ImageView wa;

	@FXML
	public Button startBtn;
	int whichInput = 0;
	int get_nums = 0;
	String op = "";
	int first_num;
	int sec_num;
	int result = 0;
	String returnMsg = "Good";
	String bigger_or_smaller = "";
	int flag;
	int server_first_num;
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("serverScene.fxml"));

		// Create a scene and place it in the stage
		primaryStage.setTitle("Server"); // Set the stage title
		primaryStage.setScene(new Scene(root)); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	@FXML
	public void startgame(ActionEvent event) {

		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(8888);

				// Listen for a connection request ����client�s�u
				Socket socket = serverSocket.accept();

				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				 // �üƲ��ͭn�Pclient������C�����Ʀr
				Random r = new Random();
				flag = r.nextInt(100);
				guesswhat.setVisible(false);
				System.out.println("�q�Ʀr��j�p���סGflag = " + flag);
				required_first_no.setVisible(true);// ����Server�N�q�X�Ĥ@�ӼƦr
				// �Q�ΰj��Ӥ��_send client�Mreceive client�Ǧ^�T�� 
				while (true) {
					
					// ��ܭn�Dclient�ݿ�J���a�W��
					required_name.setVisible(true);
					
					// ����clinet�ݶǰe���T��
					String clientGuess = inputFromClient.readUTF();
					
					// �Q�Υ��W��F���P�_inputFromClient�O�_���Ʀr(�Y�_,�h���r��)
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher isNum = pattern.matcher(clientGuess);
					
					// �Y���Ʀr��ܬOclient�Ǩӭn�q���Ʀr
					if (isNum.matches()) {
						Platform.runLater(() -> {
							get_nums++;

							if (get_nums == 1) {// ��J�ĤG�ӼƦr

								sec_num = Integer.parseInt(clientGuess);
								required_sec_no.setVisible(false);// ������ܭn�D��J�ĤG�ӼƦr������
								second_number.setText(clientGuess);
								second_number.setVisible(true);// ���A������ܲĤG�ӼƦr
								big_or_small.setVisible(true);// ���A���ݭn�Dclient��J�n��j�Τ�p
								compare.setText(">or<");
								compare.setVisible(true);// ���A������ܤj��Τp��
								computer_flag.setVisible(true);// ���A������ܹw����     �Ʋ��ͪ��Q����Ʀr(���ɬ�"?"���X���A)
								if (op.equals("+")) {// ��J�ĤG�ӼƦr�N��e���w�g��J�B��l �i�i�浲�G�p��
									result = first_num + sec_num;// �Y��J�ۥ[ �N�ۥ[���G�s�Jresult
								} else if (op.equals("-")) {
									result = first_num - sec_num;// �Y��J�۴� �N�۴�G�s�Jresult
								}

							}

						});
					} else {// �Y���O�Ʀr��ܬOclient�Ǩ�Player name, operator, bigger or smaller, try again request
						
						Platform.runLater(() -> {// �ϥ�thread�ɬ��F�קK��ܵe�����P�B��p�o�͡A�ե�Platform.runLater(()
					
							if (clientGuess.equals("+")) {// client�Ǩ�operator"+", ��ܱ��[�W�ۤv�q���ĤG�ӼƦr
								op = "+";
								operator.setText(op);
								operator.setVisible(true);// ���A������ܹB���"+"
								plus_or_minus.setVisible(false);// ���A��������ܭn�D��J�B���
								required_sec_no.setVisible(true);// ���A����ܭn�Dclient��J�ĤG�ӼƦr
							} else if (clientGuess.equals("-")) {// client�Ǩ�operator"-", ��ܱ���h�ۤv�q���ĤG�ӼƦr
								op = "-";
								operator.setText(op);
								operator.setVisible(true);// ���A���������ܹB���"-"
								plus_or_minus.setVisible(false);// ���A����������ܭn�D��J�ĤG�ӼƦr������
								required_sec_no.setVisible(true);// ���A������ܭn�Dclient��J�ĤG�ӼƦr
							} else if (clientGuess.equals("close_socket")) {// client�ШD����socket
								Button button = (Button) event.getSource();
								button.requestFocus();
								String function = button.getText();
							    Stage stage = (Stage)startBtn.getScene().getWindow();
							    // do what you have to do
							    stage.close();
							}
							else if (!clientGuess.equals("bigger") && !clientGuess.equals("smaller")&& !clientGuess.equals("again")) {
								// client�ǨӫDoperator, bigger or smaller, try again request, ��ܦ��T���ά����a�W�r
								String show_msg = "Welcome " + clientGuess + " !";
								required_name.setText(show_msg);// ���A�������"Welcome [���a��J�W�r]!"

								// �C���}�l���F�q���üƥͦ�flag(�n�Q������Ʀr)���~�Aserver�|�H���M�w�Ĥ@�ӼƦr�Aclient�ݨ��Ĥ@�ӼƦr��~�i�H�M�w�n�[�δ�t�@�ӼƦr
								Random g = new Random();
								server_first_num = g.nextInt(100);
								first_num = server_first_num;// �Nserver�M�w���Ĥ@�ӼƦr�s�J�B�⦡���Ĥ@�ӼƦr
								first_number.setText(Integer.toString(server_first_num));
								first_number.setVisible(true);// ���A������ܲĤ@�ӼƦr
								required_first_no.setVisible(false);// ���A����������ܭn�D��J�Ĥ@�ӼƦr������
								plus_or_minus.setVisible(true);// ���A������ܭn�D��J�B��l(+��-)
								

							} else if (clientGuess.equals("bigger")) {// client��ܭn��j
								compare.setText(">");
								compare.setVisible(true);// ���A���ݱ����T������ܭn��j">"
								// flag���Ҥ�r�q�쥻��"?"�令���e�üƥͦ��n�Q������Ʀr �ǳƱ��U�ӵ��X�p�⵲�G���clientĹ�ο�
								computer_flag.setText(Integer.toString(flag));

							} else if (clientGuess.equals("smaller")) {// client��ܭn��p
								compare.setText("<");
								compare.setVisible(true);// ���A���ݱ����T������ܭn��p"<"
								// flag���Ҥ�r�q�쥻��"?"�令���e�üƥͦ��n�Q������Ʀr �ǳƱ��U�ӵ��X�p�⵲�G���clientĹ�ο�
								computer_flag.setText(Integer.toString(flag));
							} else if (clientGuess.equals("again")) {// client�n�D�A�@���i��C�� �N�����l��(������ܺ⦡ �ñqserver�M�w�Ĥ@�ӼƦr�}�l�~��)
								first_number.setVisible(false);
								second_number.setVisible(false);
								big_or_small.setVisible(false);
								operator.setVisible(false);
								compare.setVisible(false);
								computer_flag.setVisible(false);
								check.setVisible(false);
								wa.setVisible(false);
								Random k = new Random();
								flag = k.nextInt(100);
								required_first_no.setVisible(true);
								Random g = new Random();
								server_first_num = g.nextInt(100);
								first_num = server_first_num;
								first_number.setText(Integer.toString(server_first_num));
								first_number.setVisible(true);
								System.out.println("�q�Ʀr�p�������סGflag = " + flag);
								get_nums=0;
								result=0;
								required_first_no.setVisible(false);
								plus_or_minus.setVisible(true);
								op = "";
								computer_flag.setText("?");
								returnMsg = "Good";
								bigger_or_smaller = "";
							}

						});
					}

					if (clientGuess.equals("bigger")) {// client��ܭn��j������U
						if (result > flag)// �ӥB�p�⵲�G�T��j��ؼ�
							returnMsg = "Win";// �h�N�^��Win�r�굹client
						else
							returnMsg = "Loose";// �q�����ܫh�N�^��Loose�r�굹client
					} else if (clientGuess.equals("smaller")) {// client��ܭn��p������U
						if (result < flag)// �ӥB�p�⵲�G�T��p��ؼ�
							returnMsg = "Win";// �h�N�^��Win�r�굹client
						else
							returnMsg = "Loose";// �q�����ܫh�N�^��Loose�r�굹client
					}
					if (returnMsg.equals("Win"))
						check.setVisible(true);// clientĹ�F�|��ܤĤ�(�p�⦡����)
					else if (returnMsg.equals("Loose"))
						wa.setVisible(true);// client��F�|��ܤe�e(�p�⦡������)
					System.out.print(returnMsg);
					outputToClient.writeUTF(returnMsg);// �^�Ǧ��\�P�_(Win or Loose)�άO�_����T��(Good)��client
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}