package sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import sample.project.client.Passenger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static sample.project.client.Passenger.props;


public class Main extends Application {
	ActorSystem clientSystem = ActorSystem.create("ClientSystem");
	
	ActorRef client = clientSystem.actorOf(props(
			InetSocketAddress.createUnresolved("192.168.43.154", 8080)));
	
	boolean trip = true;
	
	Map<String, Pair<Integer, Integer>> coord = new HashMap<>();
	Map<String, Integer> stopId = new HashMap<>();
	
	Image image = new Image("sample/map.jpg");
	Image iblueMark = new Image("sample/blueMark.png");
	Image iredMark = new Image("sample/redMark.png");
	Image icon= new Image("sample/icon.png");
	Image iconLogIn= new Image("sample/icon_log_in.png");
	Image pd= new Image("sample/pd.png");
	ImageView iconLogInView=new ImageView(iconLogIn);
	ImageView iconView = new ImageView(icon);
	ImageView blueMark = new ImageView(iblueMark);
	ImageView redMark = new ImageView(iredMark);
	ImageView map = new ImageView(image);
	ObservableList<String> langs = FXCollections.observableArrayList("Зоопарк", "Парк имени Кирова", "Парк Имени Горького", "Аксион", "Центральная площадь", "ТРЦ Талисман", "ТРЦ Столица", "ТРК Петровский", "ТЦ Кит");
	ChoiceBox<String> fromChoice = new ChoiceBox<String>(langs);
	ChoiceBox<String> toChoice = new ChoiceBox<String>(langs);
	Label isLogged=new Label("");
	TextField dvInfo= new TextField("");
	
	
	
	Stage stage;
	AnchorPane loginPane;
	TextField login = new TextField();
	PasswordField password = new PasswordField();
	Button but_enter = new Button("Войти");
	{
		but_enter.setTextFill(Color.WHITE);
	}
	Button but_register = new Button("Регистрация");
	{
		but_register.setTextFill(Color.WHITE);
	}
	
	Label nickname=new Label("   Никнейм");
	Label phoneName= new Label("   Телефон");
	Label firstName= new Label("   Имя");
	Label secondName= new Label("   Фамилия");
	Label passWord= new Label("   Пароль");
	
	TextField edit_nickname=new TextField();
	TextField edit_phoneName= new TextField();
	TextField edit_firstName= new TextField();
	TextField edit_secondName= new TextField();
	TextField edit_passWord= new TextField();
	
	Button reg= new Button("Зарегестрироваться");
	{
		reg.setMinWidth(300);
		reg.setMinHeight(30);
		reg.setStyle("-fx-background-color: #5c2ad4;");
		reg.setTextFill(Color.WHITE);
	}
	
	VBox registerBox= new VBox(5,nickname,edit_nickname,phoneName,edit_phoneName,firstName,edit_firstName,secondName,edit_secondName,passWord,edit_passWord,reg);
	
	HBox sing=new HBox(10,but_enter,but_register);
	VBox loginBox = new VBox(10,iconLogInView, login, password, sing,isLogged);
	AnchorPane orderPane;
	Button makeOrder=new Button("Заказать");
	boolean logged = false;
	Button personalData= new Button();{
		personalData.setGraphic(new ImageView(pd));
	}
	
	private void orderMethod() {
		dvInfo.setVisible(false);
		loginPane.setVisible(false);
		coord.put("Зоопарк", new Pair<>(52, 201));
		coord.put("Парк имени Кирова", new Pair<>(58, 231));
		coord.put("Парк Имени Горького", new Pair<>(145, 339));
		coord.put("Аксион", new Pair<>(160, 327));
		coord.put("Центральная площадь", new Pair<>(188, 291));
		coord.put("ТРЦ Талисман", new Pair<>(167, 130));
		coord.put("ТРК Петровский", new Pair<>(494, 153));
		coord.put("ТЦ Кит", new Pair<>(480, 338));
		coord.put("ТРЦ Столица", new Pair<>(448, 168));
		
		stopId.put("Зоопарк", 7);
		stopId.put("Парк имени Кирова", 4);
		stopId.put("Парк Имени Горького", 5);
		stopId.put("Аксион", 6);
		stopId.put("Центральная площадь", 1);
		stopId.put("ТРЦ Талисман", 2);
		stopId.put("ТРК Петровский", 3);
		stopId.put("ТЦ Кит", 8);
		stopId.put("ТРЦ Столица", 9);
		
		redMark.setVisible(false);
		blueMark.setVisible(false);
		changeMark(fromChoice, redMark);
		changeMark(toChoice, blueMark);
		
		
		
		AnchorPane.setTopAnchor(map, 10d);
		AnchorPane.setLeftAnchor(map, 10d);
		
		Label from = new Label("Точка отправления");
		from.setTextFill(Color.WHITE);
		from.setLabelFor(fromChoice);
		from.setFont(new Font(16));
		fromChoice.setMinHeight(40);
		fromChoice.setMinWidth(200);
		HBox fromBox = new HBox(10, from, fromChoice);
		
		Label to = new Label("Точка прибытия     ");
		to.setTextFill(Color.WHITE);
		to.setLabelFor(toChoice);
		to.setFont(new Font(16));
		toChoice.setMinWidth(200);
		toChoice.setMinHeight(40);
		HBox toBox = new HBox(10, to, toChoice);
		
		TextField priceEdit = new TextField();
		priceEdit.setDisable(true);
		Label price = new Label("Выберете цену       ");
		price.setTextFill(Color.WHITE);
		price.setLabelFor(toChoice);
		price.setFont(new Font(16));
		priceEdit.setMinWidth(200);
		priceEdit.setMinHeight(40);
		HBox pricebox = new HBox(10, price, priceEdit);
		
		
		makeOrder = new Button("Заказать");
		makeOrder.setTextFill(Color.WHITE);
		makeOrder.setMinWidth(375);
		makeOrder.setMinHeight(40);
		makeOrder.setDisable(true);
		makeOrder.setStyle("-fx-background-color: lightpink");
		
		order_click();
		
		AnchorPane.setBottomAnchor(personalData,10d );
		AnchorPane.setRightAnchor(personalData,10d );
		
		VBox vBox = new VBox(10,iconView, fromBox, toBox, pricebox, makeOrder,dvInfo);
		AnchorPane.setTopAnchor(vBox, 30d);
		AnchorPane.setRightAnchor(vBox, 10d);
		orderPane = new AnchorPane(map, vBox, blueMark, redMark);
		
		orderPane.setStyle("-fx-background-color: #7876FF");
		if(Passenger.taken){
			Thread tak= new Thread(new Runnable() {
				@Override
				public void run() {
					while (Passenger.taken){
						try {
						dvInfo.setFont(new Font(16));
						dvInfo.setVisible(true);
						dvInfo.setDisable(true);
						dvInfo.setText(Passenger.message);
						makeOrder.setVisible(false);
						fromChoice.setDisable(true);
						toChoice.setDisable(true);
						Thread.sleep(300);
						} catch (Exception e) { }
					}
					dvInfo.setFont(new Font(16));
					dvInfo.setVisible(false);
					dvInfo.setDisable(false);
					dvInfo.setText(Passenger.message);
					makeOrder.setVisible(true);
					fromChoice.setDisable(false);
					toChoice.setDisable(false);
					order_click();
				}
			});
			
			tak.start();
		}
		
		Scene order = new Scene(orderPane);
		stage.setScene(order);
		stage.setMinHeight(640);
		stage.setMinWidth(1000);
		stage.setMaxHeight(640);
		stage.setMaxWidth(1000);
		stage.show();
	}
	
	private void order_click() {
		makeOrder.setOnMouseClicked((b)->{
			
			dvInfo.setFont(new Font(16));
			dvInfo.setVisible(true);
			dvInfo.setDisable(true);
			makeOrder.setVisible(false);
			fromChoice.setDisable(true);
			toChoice.setDisable(true);
			Integer idFrom=stopId.get(fromChoice.getValue());
			Integer idTo=stopId.get(toChoice.getValue());
			client.tell(Passenger.PassengerMessage.createInstance(idFrom.toString(), idTo.toString(), 0), ActorRef.noSender() );
			
			Thread t= new Thread(new Runnable() {
				@Override
				public void run() {
					Passenger.arrived=false;
					while (!Passenger.arrived){
						try {
							dvInfo.setText(Passenger.message);
							Thread.sleep(100);
						} catch (Exception e) { }
					}
					dvInfo.setVisible(false);
					makeOrder.setVisible(true);
					fromChoice.setDisable(false);
					toChoice.setDisable(false);
					makeOrder.setTextFill(Color.WHITE);
					makeOrder.setMinWidth(375);
					makeOrder.setMinHeight(40);
					makeOrder.setDisable(true);
					makeOrder.setStyle("-fx-background-color: lightpink");
				}
			});
			t.start();
			
			
			
		});
	}
	
	private void allright() {
		if (trip) {
			makeOrder.setDisable(false);
			makeOrder.setStyle("-fx-text-inner-color: white;-fx-background-color: lightgreen");
		} else {
			makeOrder.setDisable(true);
			makeOrder.setStyle("-fx-text-inner-color: white;-fx-background-color: lightpink");
		}
		
	}
	
	private void changeMark(ChoiceBox<String> choice, ImageView mark) {
		choice.setOnAction(event -> {
			mark.setVisible(true);
			Pair<Integer, Integer> p = coord.get(choice.getValue());
			mark.setX(p.getKey());
			mark.setY(p.getValue());
			if (redMark.getX() != blueMark.getX() && redMark.getY() != blueMark.getY() && redMark.getX() != 0 && blueMark.getX() != 0)
				trip = true;
			else
				trip = false;
			allright();
		});
	}
	
	private void loginMethod(){
		but_enter.setMinHeight(40);
		login.setPromptText("Введите логин");
		login.setMinHeight(40);
		login.setMinWidth(300);
		login.setMaxWidth(300);
		password.setMinHeight(40);
		password.setPromptText("Введите пароль");
		password.setMinWidth(300);
		password.setMaxWidth(300);
		but_enter.setMinWidth(145);
		stage.setMinWidth(600);
		stage.setMaxWidth(600);
		stage.setMaxHeight(400);
		stage.setMinHeight(400);
		but_enter.setStyle("-fx-background-color: #5c2ad4;");
		but_register.setStyle("-fx-background-color: #5c2ad4;");
		but_register.setMinWidth(145);
		but_register.setMinHeight(40);
		
		AnchorPane.setLeftAnchor(loginBox, 150d);
		AnchorPane.setTopAnchor(loginBox, 50d);
		AnchorPane.setLeftAnchor(registerBox,150d );
		AnchorPane.setTopAnchor(registerBox,30d );
		loginPane = new AnchorPane(loginBox,registerBox);
		loginPane.setVisible(true);
		registerBox.setVisible(false);
		isLogged.setTextFill(Color.RED);
		
		but_register.setOnMouseClicked((e)->{
			loginBox.setVisible(false);
			registerBox.setVisible(true);
			edit_firstName.setMinHeight(20);
			edit_nickname.setMinHeight(20);
			edit_passWord.setMinHeight(20);
			edit_phoneName.setMinHeight(20);
			edit_secondName.setMinHeight(20);
			edit_firstName.setMinWidth(300);
			edit_nickname.setMinWidth(300);
			edit_passWord.setMinWidth(300);
			edit_phoneName.setMinWidth(300);
			edit_secondName.setMinWidth(300);
			firstName.setMinWidth(300);
			nickname.setMinWidth(300);
			passWord.setMinWidth(300);
			phoneName.setMinWidth(300);
			secondName.setMinWidth(300);
			firstName.setTextFill(Color.WHITE);
			nickname.setTextFill(Color.WHITE);
			passWord.setTextFill(Color.WHITE);
			phoneName.setTextFill(Color.WHITE);
			secondName.setTextFill(Color.WHITE);
			
		});
		reg.setOnMouseClicked((e)->{registerBox.setVisible(false);loginBox.setVisible(true);});
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		if (!logged)
			loginMethod();
		
		but_enter.setOnMouseClicked((b) -> {
			String l = login.getText();
			String p = password.getText();
			
			
			
			
			// registrationOrLogin(client, 'u');
			client.tell(Client.UserInformation.createInstance(l, p,
					"", "", 'u', 'n'), ActorRef.noSender());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(Passenger.isLogged) {
				orderMethod();
				//chooseBusStop(client);
			}
			else{
				isLogged.setText("                     Неверные данные");
			}
			
		});
		
		loginPane.setStyle("-fx-background-color: #7876FF");
		Scene scene = new Scene(loginPane);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent windowEvent) {
				System.exit(0);
				
			}
		});
		
		stage.setTitle("DRT");
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
