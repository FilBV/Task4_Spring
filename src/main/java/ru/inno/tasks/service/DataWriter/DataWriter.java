package ru.inno.tasks.service.DataWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.tasks.model.Logins;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.model.Users;
import ru.inno.tasks.repository.LoginsRep;
import ru.inno.tasks.repository.UsersRep;

import java.util.List;

@Component
public class DataWriter implements DataWriteable {
    private final UsersRep usersRep;
    private final LoginsRep loginsRep;

    @Autowired
    public DataWriter(UsersRep usersRep, LoginsRep loginsRep) {
        this.usersRep = usersRep;
        this.loginsRep = loginsRep;
    }

    @Override
    @Transactional
    public void writeDb(List<Model> mods) {
        mods.sort((o1, o2) -> (o1.getUsername() + o2.getFio()).compareTo(o2.getUsername() + o2.getFio()));
        String keyCmp = ""; //  для сравнения в цикле
        Users usr = new Users();
        for (Model m : mods) {
            if (!keyCmp.equals(m.getUsername() + m.getFio())) {
                keyCmp = m.getUsername() + m.getFio();
                usr = new Users();
                usr.setUsername(m.getUsername());
                usr.setFio(m.getFio());
                usersRep.save(usr);
            }
            Logins log = new Logins();
            log.setApplication(m.getApplType());
            log.setAccessDate(m.getDateInput());
            log.setUsers(usr);
            loginsRep.save(log);
        }
    }
}
