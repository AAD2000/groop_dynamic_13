package com.server;

import java.io.Serializable;

public interface Message extends Serializable {
    long serialVersionUID = 6529685098261231232L;
    String getMessage();
}
