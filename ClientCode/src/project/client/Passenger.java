package project.client;

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
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Passenger extends Client {
    private static Props props(InetSocketAddress remote) {
        return Props.create(Passenger.class, remote);
    }

    public Passenger(InetSocketAddress remote) {
        super(remote);
    }

    @Override
    Receive connected(ActorRef connection) {
        {
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

                        if(!isLogged) {
                            System.out.println(message.getServerMessage());
                            if (message.isWrongName() || !message.isEntered()) {
                                return;
                            }
                            isLogged = true;

                        } else {
                            System.out.println(message.getServerMessage());
                        }

                    })
                    .build();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ActorSystem clientSystem = ActorSystem.create("ClientSystem");

        Scanner in = new Scanner(System.in);

        ActorRef client = clientSystem.actorOf(Passenger.props(
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
