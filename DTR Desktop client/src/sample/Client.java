package sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import sample.com.server.Configurations;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public abstract class Client extends AbstractActor {
	
	private final InetSocketAddress remote;
	
	/**
	 * True - when user logged in
	 */
	public static boolean isLogged = false;
	
	/**
	 * True - when user connected to the server
	 */
	public static boolean isConnected = false;
	
	/**
	 * Wait message from server
	 */
	public static boolean wait = false;
	
	/**
	 * Current bus stops
	 */
	public static String[] busStops;
	
	// Подключаемся к серверу
	public Client(InetSocketAddress remote) {
		this.remote = remote;
		final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
		tcp.tell(TcpMessage.connect(remote), getSelf());
		
		String builder = "CentralSquare " + "Talisman " + "Petrovsky " +
				"KirovPark " + "GorkyPark " + "Axion " +
				"Zoo " + "Whale " + "Capital";
		busStops = builder.split(" ");
	}
	
	public static class UserInformation {
		
		final String userName;
		final String password;
		final String fullname;
		final String phoneNumber;
		final char group;
		final char register;
		
		private UserInformation
				(String userName, String password, String fullname, String phoneNumber, char group, char register) {
			this.userName = userName;
			this.password = password;
			this.group = group;
			this.register = register;
			this.fullname = fullname;
			this.phoneNumber = phoneNumber;
		}
		
		public ByteString getInformation() {
			switch (register){
				case 'y': {
					return ByteString.fromString(String.format("%d %s %s %s %s %s",
							Configurations.REGISTRATION,
							userName, md5Custom(password), fullname, phoneNumber, group));
				}
				case 'n': {
					return ByteString.fromString(String.format("%d %s %s %s %s",
							Configurations.LOGIN,
							userName, phoneNumber, md5Custom(password), group));
				}
				default:{
					return ByteString.fromString(String.format("%d %s",
							Configurations.EXCEPTION, "Wrong&client&group"));
				}
			}
		}
		
		private static String md5Custom(String st) {
			byte[] digest = new byte[0];
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(st.getBytes());
				digest = messageDigest.digest();
			} catch (NoSuchAlgorithmException e) {
				// тут можно обработать ошибку
				// возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
				e.printStackTrace();
			}
			
			BigInteger bigInt = new BigInteger(1, digest);
			StringBuilder md5Hex = new StringBuilder(bigInt.toString(16));
			
			while( md5Hex.length() < 32 ){
				md5Hex.insert(0, "0");
			}
			
			return md5Hex.toString();
		}
		
		public static UserInformation createInstance
				(String userName, String password, String fullname, String phoneNumber, char group, char register) {
			return new UserInformation(userName, password, fullname, phoneNumber, group,register);
		}
		
		static UserInformation createRegistrationInstance(char group) {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter your name");
			String name = in.nextLine();
			
			System.out.println("Enter your phone number");
			String phoneNumber = in.nextLine();
			
			System.out.println("Enter your fullName");
			String fullName = in.nextLine().replace(" ", "&");
			
			System.out.println("Enter your password");
			String password = in.nextLine();
			
			return new UserInformation(name, password, fullName, phoneNumber,  group, 'y');
		}
		
		static UserInformation createLoginInstance(char group) {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter your name or phone number");
			String name = in.nextLine();
			
			System.out.println("Enter your password");
			String password = in.nextLine();
			
			return new UserInformation(name, password, "", name, group, 'n');
		}
	}
	
	/**
	 * Convert message from server to normal string message
	 * @param message message from server
	 * @return normal message
	 */
	protected String convertMessage(String message) {
		return message.replace('&', ' ');
	}
	
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Tcp.CommandFailed.class, msg -> {
					getContext().stop(getSelf());
					
				})
				.match(Tcp.Connected.class, msg -> {
					getSender().tell(TcpMessage.register(getSelf()), getSelf());
					getContext().become(connected(getSender()));
				})
				.build();
	}
	
	public abstract Receive connected(final ActorRef connection);
	
	/**
	 * Registration or login user
	 * @param client ActorRef client
	 * @param group client group
	 * @throws InterruptedException
	 */
	static void registrationOrLogin(ActorRef client, char group) throws InterruptedException {
		Scanner in = new Scanner(System.in);
		
		String mes;
		do {
			if(!wait) {
				System.out.println("Would you like to (r)egister or (l)ogin");
				mes = in.nextLine();
				
				switch (mes){
					case "r": {
						client.tell(UserInformation.createRegistrationInstance(group), ActorRef.noSender());
						break;
					}
					case "l" :{
						client.tell(UserInformation.createLoginInstance(group), ActorRef.noSender());
						break;
					}
				}
				
			}
			Thread.sleep(1000);
		} while(!isLogged);
	}
	
	public static void printBusStops() {
		for(int i = 0; i < busStops.length; ++i) {
			System.out.println(String.format("%d) %s", i + 1, busStops[i]));
		}
		System.out.println("********************\n");
	}
}
