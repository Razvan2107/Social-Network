package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private Long from;
    private Long to;
    private String message;
    private LocalDateTime date;

    public Message(Utilizator from, List<Utilizator> to, String message) {
        this.date=LocalDateTime.now();
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return date;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(LocalDateTime date) {
        this.date = date;
    }
}
