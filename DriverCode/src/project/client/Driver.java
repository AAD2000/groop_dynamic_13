package project.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import com.server.ServerInformation;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Driver extends Client {
    private static Props props(InetSocketAddress remote) {
        return Props.create(Passenger.class, remote);
    }

    public Driver(InetSocketAddress remote) {
        super(remote);
    }

    static class DriverInformation implements Message {
        final int capacity;
        final int stopId;

        private DriverInformation(int capacity, int stopId) {
            this.capacity = capacity;
            this.stopId = stopId;
        }

        static DriverInformation createInstance(int capacity, int stopId) {
            return new DriverInformation(capacity,stopId);
        }

        @Override
        public ByteString getInformation() {
            return ByteString.fromString(String.format("%d %d", capacity, stopId));
        }
    }

    @Override
    Receive connected(ActorRef connection) {
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
                    if (message == null) return;

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

        ActorRef client = clientSystem.actorOf(Driver.props(
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
            int capacity = in.nextInt();
            int stopId = in.nextInt();
            client.tell(DriverInformation.createInstance(capacity, stopId),
                    ActorRef.noSender());
        }
    }
}
