package ru.inno.tasks.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.inno.tasks.model.Users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Logins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime access_date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    private String application;

    public void setAccessDate(String date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        this.access_date = LocalDateTime.parse(date, dateTimeFormatter);
    }
}
