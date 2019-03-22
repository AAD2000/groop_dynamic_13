package sample.project.client;

import akka.util.ByteString;

public interface Message {
    ByteString getInformation();
}
