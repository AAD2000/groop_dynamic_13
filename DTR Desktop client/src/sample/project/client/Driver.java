package sample.project.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import sample.Client;
import sample.com.server.Configurations;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Driver extends Client {
    private static Props props(InetSocketAddress remote) {
        return Props.create(Driver.class, remote);
    }

    public Driver(InetSocketAddress remote) {
        super(remote);
    }

    private static class DriverInformation implements Message {
        String capacity;
        String stopId;

        DriverInformation(String capacity, String stopId) {
            this.capacity = capacity;
            this.stopId = stopId;
        }

        @Override
        public ByteString getInformation() {
            return ByteString.fromString(String.format("%d %s %s",
                    Configurations.SET_DRIVER_STOP, capacity, stopId));
        }

    }



    @Override
    public Receive connected(ActorRef connection) {
        return receiveBuilder()
                .match(DriverInformation.class, msg -> {
                    connection.tell(TcpMessage.write(msg.getInformation()),
                            getSelf());
                })
                .match(UserInformation.class, info -> {
                    wait = true;
                    connection.tell(TcpMessage.write(info.getInformation()),
                            getSelf());
                })
                .match(String.class, str -> connection.tell(TcpMessage.write(ByteString.fromString(str)),getSelf()))
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
                    String strInfo = msg.data().decodeString("UTF-8");
                    String[] serverInfo = strInfo.split(" ");
                    int configuration = Integer.valueOf(serverInfo[0]);

                    switch (configuration) {
                        case Configurations.REGISTRATION: {
                            boolean registered = Boolean.parseBoolean(serverInfo[1]);
                            System.out.println(convertMessage(serverInfo[2]));
                            break;
                        }
                        case Configurations.LOGIN: {
                            isLogged = Boolean.parseBoolean(serverInfo[1]);
                            System.out.println(convertMessage(serverInfo[2]));
                            break;
                        }
                        case Configurations.SET_DRIVER_STOP :{
                            System.out.println(convertMessage(serverInfo[1]));
                            break;
                        }
                        case Configurations.DRIVER_ARRIVE: {
                            System.out.println(convertMessage(serverInfo[1]));
                            break;
                        }
                        case Configurations.EXCEPTION: {

                            break;
                        }
                        case Configurations.CONNECTION: {
                            isConnected = Boolean.getBoolean(serverInfo[1]);
                            System.out.println(convertMessage(serverInfo[2]));
                            break;
                        }
                        default: {
                            System.out.println("Incorrect message from server: " + getSender());
                        }
                    }

                    wait = false;
                })
                .build();


    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem clientSystem = ActorSystem.create("ClientSystem");
        Scanner in = new Scanner(System.in);

        ActorRef client = clientSystem.actorOf(Driver.props(
                InetSocketAddress.createUnresolved("localhost", 8080)));

        Thread.sleep(1000);

        client.tell(UserInformation.createInstance("DriverAnton", "0000",
                "", "",'d', 'n'), ActorRef.noSender());
       // registrationOrLogin(client, 'd');

        Thread.sleep(1000);
        chooseBusStop(client);

        String str = "";
        while(!str.equals("close")) {
            str = in.nextLine();
            client.tell(String.format("%d %s", Configurations.DRIVER_ARRIVE, str),
                    ActorRef.noSender());
        }
    }

    private static void chooseBusStop(ActorRef client) {
        System.out.println("********************");
        Scanner in = new Scanner(System.in);
        System.out.println("Choose bus stop");

        printBusStops();
        System.out.println("Choose capacity");
        String capacity = in.nextLine();

        System.out.println("Choose point where you are");
        String from = in.nextLine();

        client.tell(new DriverInformation(capacity,from), ActorRef.noSender());
    }

}
