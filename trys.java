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

				// Listen for a connection request 等待client連線
				Socket socket = serverSocket.accept();

				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
				
				 // 亂數產生要與client比較的遊戲的數字
				Random r = new Random();
				flag = r.nextInt(100);
				guesswhat.setVisible(false);
				System.out.println("猜數字比大小答案：flag = " + flag);
				required_first_no.setVisible(true);// 提示Server將秀出第一個數字
				// 利用迴圈來不斷send client和receive client傳回訊息 
				while (true) {
					
					// 顯示要求client端輸入玩家名稱
					required_name.setVisible(true);
					
					// 接收clinet端傳送的訊息
					String clientGuess = inputFromClient.readUTF();
					
					// 利用正規表達式判斷inputFromClient是否為數字(若否,則為字串)
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher isNum = pattern.matcher(clientGuess);
					
					// 若為數字表示是client傳來要猜的數字
					if (isNum.matches()) {
						Platform.runLater(() -> {
							get_nums++;

							if (get_nums == 1) {// 輸入第二個數字

								sec_num = Integer.parseInt(clientGuess);
								required_sec_no.setVisible(false);// 關閉顯示要求輸入第二個數字的提示
								second_number.setText(clientGuess);
								second_number.setVisible(true);// 伺服器端顯示第二個數字
								big_or_small.setVisible(true);// 伺服器端要求client輸入要比大或比小
								compare.setText(">or<");
								compare.setVisible(true);// 伺服器端顯示大於或小於
								computer_flag.setVisible(true);// 伺服器端顯示預先亂     數產生的被比較數字(此時為"?"打碼狀態)
								if (op.equals("+")) {// 輸入第二個數字代表前面已經輸入運算子 可進行結果計算
									result = first_num + sec_num;// 若輸入相加 將相加結果存入result
								} else if (op.equals("-")) {
									result = first_num - sec_num;// 若輸入相減 將相減結果存入result
								}

							}

						});
					} else {// 若不是數字表示是client傳來Player name, operator, bigger or smaller, try again request
						
						Platform.runLater(() -> {// 使用thread時為了避免顯示畫面不同步其況發生，調用Platform.runLater(()
					
							if (clientGuess.equals("+")) {// client傳來operator"+", 表示欲加上自己猜的第二個數字
								op = "+";
								operator.setText(op);
								operator.setVisible(true);// 伺服器端顯示運算符"+"
								plus_or_minus.setVisible(false);// 伺服器關閉顯示要求輸入運算符
								required_sec_no.setVisible(true);// 伺服器顯示要求client輸入第二個數字
							} else if (clientGuess.equals("-")) {// client傳來operator"-", 表示欲減去自己猜的第二個數字
								op = "-";
								operator.setText(op);
								operator.setVisible(true);// 伺服器端顯示顯示運算符"-"
								plus_or_minus.setVisible(false);// 伺服器端關閉顯示要求輸入第二個數字的提示
								required_sec_no.setVisible(true);// 伺服器端顯示要求client輸入第二個數字
							} else if (clientGuess.equals("close_socket")) {// client請求關閉socket
								Button button = (Button) event.getSource();
								button.requestFocus();
								String function = button.getText();
							    Stage stage = (Stage)startBtn.getScene().getWindow();
							    // do what you have to do
							    stage.close();
							}
							else if (!clientGuess.equals("bigger") && !clientGuess.equals("smaller")&& !clientGuess.equals("again")) {
								// client傳來非operator, bigger or smaller, try again request, 表示此訊息及為玩家名字
								String show_msg = "Welcome " + clientGuess + " !";
								required_name.setText(show_msg);// 伺服器端顯示"Welcome [玩家輸入名字]!"

								// 遊戲開始除了電腦亂數生成flag(要被比較的數字)之外，server會隨機決定第一個數字，client看見第一個數字後才可以決定要加或減另一個數字
								Random g = new Random();
								server_first_num = g.nextInt(100);
								first_num = server_first_num;// 將server決定的第一個數字存入運算式的第一個數字
								first_number.setText(Integer.toString(server_first_num));
								first_number.setVisible(true);// 伺服器端顯示第一個數字
								required_first_no.setVisible(false);// 伺服器端關閉顯示要求輸入第一個數字的提示
								plus_or_minus.setVisible(true);// 伺服器端顯示要求輸入運算子(+或-)
								

							} else if (clientGuess.equals("bigger")) {// client選擇要比大
								compare.setText(">");
								compare.setVisible(true);// 伺服器端接收訊息後顯示要比大">"
								// flag標籤文字從原本的"?"改成先前亂數生成要被比較的數字 準備接下來結合計算結果顯示client贏或輸
								computer_flag.setText(Integer.toString(flag));

							} else if (clientGuess.equals("smaller")) {// client選擇要比小
								compare.setText("<");
								compare.setVisible(true);// 伺服器端接收訊息後顯示要比小"<"
								// flag標籤文字從原本的"?"改成先前亂數生成要被比較的數字 準備接下來結合計算結果顯示client贏或輸
								computer_flag.setText(Integer.toString(flag));
							} else if (clientGuess.equals("again")) {// client要求再一次進行遊戲 將條件初始化(關閉顯示算式 並從server決定第一個數字開始繼續)
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
								System.out.println("猜數字小游戲答案：flag = " + flag);
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

					if (clientGuess.equals("bigger")) {// client選擇要比大的條件下
						if (result > flag)// 而且計算結果確實大於目標
							returnMsg = "Win";// 則將回傳Win字串給client
						else
							returnMsg = "Loose";// 猜錯的話則將回傳Loose字串給client
					} else if (clientGuess.equals("smaller")) {// client選擇要比小的條件下
						if (result < flag)// 而且計算結果確實小於目標
							returnMsg = "Win";// 則將回傳Win字串給client
						else
							returnMsg = "Loose";// 猜錯的話則將回傳Loose字串給client
					}
					if (returnMsg.equals("Win"))
						check.setVisible(true);// client贏了會顯示勾勾(計算式成立)
					else if (returnMsg.equals("Loose"))
						wa.setVisible(true);// client輸了會顯示叉叉(計算式不成立)
					System.out.print(returnMsg);
					outputToClient.writeUTF(returnMsg);// 回傳成功與否(Win or Loose)或是否接到訊息(Good)給client
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