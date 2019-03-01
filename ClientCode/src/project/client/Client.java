package project.client;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Client extends AbstractActor {

    private final InetSocketAddress remote;
    protected boolean connected = false;
    static boolean isLogged = false;

    // Подключаемся к серверу
    Client(InetSocketAddress remote) {
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

    abstract Receive connected(final ActorRef connection);
}

