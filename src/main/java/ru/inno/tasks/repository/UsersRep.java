package ru.inno.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import ru.inno.tasks.model.Users;

import java.util.List;

public interface UsersRep extends CrudRepository<Users, Integer> {
    public List<Users> findByUsernameAndFio(String username, String fio);
    public Users findFirstByUsernameAndFio(String username, String fio);

}
