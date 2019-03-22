package sample.com.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class ServerInformation implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    ServerInformation(boolean entered, String serverMessage, boolean wrongName) {
        this.entered = entered;
        this.serverMessage = serverMessage;
        this.wrongName = wrongName;
    }

    private boolean entered;
    private String serverMessage;
    private boolean wrongName;


    public boolean isEntered() {
        return entered;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public boolean isWrongName() {
        return wrongName;
    }

    public byte[] serializeSelf() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(this);
        objectOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }
}
