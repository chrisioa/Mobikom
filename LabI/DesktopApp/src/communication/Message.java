package communication;

import java.io.Serializable;

/**
 * Serializable Message that will be send between server and instances.
 */
public class Message implements Serializable {
    private String message = "";

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
