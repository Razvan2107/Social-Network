package utils.events;


import domain.Entity;
import domain.Utilizator;

public class UserChangeEvent extends Entity implements Event {
    private ChangeEventType type;
    private Utilizator data, oldData;

    public UserChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }
    public UserChangeEvent(ChangeEventType type, Utilizator data, Utilizator oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}