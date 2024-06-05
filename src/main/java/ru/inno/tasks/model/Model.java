package ru.inno.tasks.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Model {
    private String fileName;
    private String username;
    private String fio;
    private String dateInput;
    private String applType;

    @Override
    public String toString() {
        return "Model= " +
                "filename= " + fileName +
                "username=" + username +
                ", fio=" + fio +
                ", dateInput=" + dateInput +
                ", applType=" + applType + "\n";
    }
}
