package com.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//TODO Клиент: ->bus stop - start/end names;  server->current location driver.
// When driver has arrived the driver is here
//TODO Дривер: ->current position/moment of picking up a passenger server->list of bus stop names

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

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public boolean isEntered() {
        return entered;
    }

    public String getServerMessage() {
        return serverMessage;
    }


    public void setWrongName(boolean wrongName) {
        this.wrongName = wrongName;
    }

    boolean isWrongName() {
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
