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


public class Passenger extends Client {

    public static String message="";
    public static Props props(InetSocketAddress remote) {
        return Props.create(Passenger.class, remote);
    }

    /**
     *     True - when passenger arrived
     */
    public static boolean arrived = false;
    public static boolean taken = false;
    public static boolean registered = false;

    public Passenger(InetSocketAddress remote) {
        super(remote);

    }


    public static class PassengerMessage implements sample.project.client.Message {
        private String from;
        private String to;
        private int waitingTime;

        private PassengerMessage(String from, String to, int waitingTime) {
            this.from = from;
            this.to = to;
            this.waitingTime = waitingTime;
        }

        public static PassengerMessage createInstance(String from, String to, int waitingTime) {
            return new PassengerMessage(from, to, waitingTime);
        }

        @Override
        public ByteString getInformation() {
            return ByteString.fromString(String.format("%d %s %s %s", Configurations.SELECTION, from, to, waitingTime));
        }
    }



    @Override
    public Receive connected(ActorRef connection) {
        return receiveBuilder()
                .match(UserInformation.class, info -> {
                    wait = true;
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
                    String strInfo = msg.data().decodeString("UTF-8");
                    String[] serverInfo = strInfo.split(" ");
                    int configuration = Integer.valueOf(serverInfo[0]);

                    switch (configuration) {
                        case Configurations.REGISTRATION: {
                            registered = Boolean.parseBoolean(serverInfo[1]);
                            System.out.println(convertMessage(serverInfo[2]));
                            break;
                        }
                        case Configurations.LOGIN: {
                            isLogged = Boolean.parseBoolean(serverInfo[1]);
                            if(isLogged) {
                                taken = Boolean.parseBoolean(serverInfo[2]);
                                System.out.println(convertMessage(serverInfo[3]));
                                message=convertMessage(serverInfo[3]);
                                arrived=false;
                                return;
                            }
                            System.out.println(convertMessage(serverInfo[2]));
                            message=convertMessage(serverInfo[2]);
                            break;
                        }
                        case Configurations.ARRIVAL: {
                            System.out.println(convertMessage(serverInfo[1]));
                            message = convertMessage(serverInfo[1]);
                            arrived = true;
                            taken = false;
                            break;
                        }
                        case Configurations.SELECTION: {
                            message = convertMessage(serverInfo[1]);
                            System.out.println(convertMessage(serverInfo[1]));
                            break;
                        }
                        case Configurations.EXCEPTION: {
                            System.out.println(convertMessage(serverInfo[1]));
                            break;
                        }
                        case Configurations.SET_DRIVER_STOP :{
                            System.out.println(convertMessage(serverInfo[1]));
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


        ActorRef client = clientSystem.actorOf(Passenger.props(
                InetSocketAddress.createUnresolved("localhost", 8080)));

        Thread.sleep(1000);


       // registrationOrLogin(client, 'u');
        client.tell(UserInformation.createInstance("Tohenz2", "135-toha-0135",
                "", "",'u', 'n'), ActorRef.noSender());

        chooseBusStop(client);

    }


    /**
     * Choose bus stop from list
     * @param client ActorRef client
     */
    public static void chooseBusStop(ActorRef client) {
        System.out.println("********************");
        Scanner in = new Scanner(System.in);
        System.out.println("Choose bus stops");
        for(int i = 0; i < busStops.length; ++i) {
            System.out.println(String.format("%d) %s", i + 1, busStops[i]));
        }
        System.out.println("********************");
        System.out.println("Choose a starting stop");
        String from = in.nextLine();

        System.out.println("Choose an ending point");
        String to = in.nextLine();

        System.out.println("Choose a waiting time");
        int waitingTime = in.nextInt();

        client.tell(new PassengerMessage(from, to, waitingTime), ActorRef.noSender());
    }
}
