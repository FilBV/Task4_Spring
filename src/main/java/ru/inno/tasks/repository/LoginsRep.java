package ru.inno.tasks.repository;

import org.springframework.data.repository.CrudRepository;
import ru.inno.tasks.model.Logins;

public interface LoginsRep extends CrudRepository<Logins, Integer> {
}
