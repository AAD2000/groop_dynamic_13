package project.client;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.server.ServerInformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client extends AbstractActor {

    final InetSocketAddress remote;
    private boolean connected = false;
    static private boolean isLogged = false;

    private static Props props(InetSocketAddress remote) {
        return Props.create(Client.class, remote);
    }

    // Подключаемся к серверу
    public Client(InetSocketAddress remote) {
        this.remote = remote;
        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.connect(remote), getSelf());
    }

    static class UserInformation {

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

        ByteString getInformation() {
            return ByteString.fromString(String.format("%s %s %s %s %s %s",
                    userName, md5Custom(password), fullname, phoneNumber, group, register));
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

        static UserInformation createInstance
                (String userName, String password, String fullname, String phoneNumber, char group, char register) {
            return new UserInformation(userName, password, fullname, phoneNumber, group,register);
        }
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

    private Receive connected(final ActorRef connection) {
        // System.out.println(connection);
        return receiveBuilder()
                .match(UserInformation.class, info -> {
                        connection.tell(TcpMessage.write(info.getInformation()),
                                getSelf());
                })
                .match(ByteString.class, msg -> {
                    connection.tell(TcpMessage.write(msg), getSelf());
                })
                .match(Tcp.CommandFailed.class, msg -> {
                    System.out.println("Command Failed");
                })
                .matchEquals("close",msg -> {
                    connection.tell(TcpMessage.close(), getSelf());
                })
                .match(TcpMessage.class, msg -> {
                    getSender().tell(msg, getSelf());
                })
                .match(Tcp.ConnectionClosed.class, msg -> {
                    getContext().stop(getSelf());
                    System.out.println("Server Closed");
                })
                .match(Tcp.Received.class, msg -> {
                    byte[] byteArray = msg.data().toArray();

                    ServerInformation message;
                    // Дессериализация класса
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(
                                new ByteArrayInputStream(byteArray));

                        message = (ServerInformation) objectInputStream.readObject();
                        objectInputStream.close();
                    } catch (ClassCastException ex) {
                        System.out.println("Wrong message from server");
                        return;
                    } catch (IOException ex) {
                        System.out.println("Wrong class");
                        return;
                    }

                    System.out.println(message.getServerMessage());
                    if(message.isWrongName() || !message.isEntered()) {
                        return;
                    }

                    isLogged = true;



                })
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem clientSystem = ActorSystem.create("ClientSystem");

        Scanner in = new Scanner(System.in);

        ActorRef client = clientSystem.actorOf(Client.props(
                InetSocketAddress.createUnresolved("localhost", 8080)));

        Thread.sleep(1000);

            /*client.tell(
                    UserInformation.createInstance("Tohenz", "44234123","Kalinin&Anton",
                            "8999213123", 'u', 'y'),
                    ActorRef.noSender());*/
            while(!isLogged) {
                // Регистрация и вход
                // Ничего другого умного не придумал, так как Actor максимально закрыт
                // TODO: Лёха, здесь регистрация и вход!!!
                client.tell(
                        UserInformation.createInstance(in.nextLine(), "44234123","",
                                "", 'u', 'n'),
                        ActorRef.noSender());
            }





    }

}

