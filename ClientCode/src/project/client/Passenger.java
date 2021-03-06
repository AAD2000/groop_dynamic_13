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

    private static class PassengerMessage implements Message{
        private String from;
        private String to;

        private PassengerMessage(String from, String to) {
            this.from = from;
            this.to = to;
        }

        static PassengerMessage createInstance(String from, String to) {
            return new PassengerMessage(from, to);
        }

        @Override
        public ByteString getInformation() {
            return ByteString.fromString(String.format("%s %s", from, to));
        }
    }

    @Override
    Receive connected(ActorRef connection) {
        return receiveBuilder()
                .match(UserInformation.class, info -> {
                    connection.tell(TcpMessage.write(info.getInformation()),
                            getSelf());
                })
                .match(PassengerMessage.class, msg -> {
                    connection.tell(TcpMessage.write(msg.getInformation()), getSelf());
                })
                .match(ByteString.class, msg -> {
                    connection.tell(TcpMessage.write(msg), getSelf());
                })
                .match(Tcp.CommandFailed.class, msg -> {
                    System.out.println("Command Failed");
                })
                .matchEquals("close", msg -> {
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

                    ServerInformation message = getInformation(msg);
                    if(message == null) return;

                    if (!isLogged) {
                        registration(message);

                    } else {
                        System.out.println(message.getServerMessage());
                    }

                })
                .build();

    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem clientSystem = ActorSystem.create("ClientSystem");
        Scanner in = new Scanner(System.in);

        ActorRef client = clientSystem.actorOf(Passenger.props(
                InetSocketAddress.createUnresolved("localhost", 8080)));

        Thread.sleep(1000);


        while(!isLogged) {
            // Регистрация и вход
            // Ничего другого умного не придумал, так как Actor максимально закрыт
            // TODO: Лёха, здесь регистрация и вход!!!
            client.tell(
                    UserInformation.createInstance("Tohenz", "44234123","",
                            "", 'u', 'n'),
                    ActorRef.noSender());
            Thread.sleep(1000);
        }

        while(true) {
            System.out.println("Enter start stop and end stop");
            String from = in.nextLine();
            String to = in.nextLine();
            client.tell(PassengerMessage.createInstance(from, to),
                    ActorRef.noSender());
        }
    }
}
