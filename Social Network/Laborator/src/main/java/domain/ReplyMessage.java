package domain;

import java.util.List;

public class ReplyMessage extends Message{
    public Message mesaj;
    public ReplyMessage(Utilizator from, List<Utilizator> to, String message) {
        super(from, to, message);
    }
}
