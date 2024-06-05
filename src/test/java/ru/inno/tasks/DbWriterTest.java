package ru.inno.tasks;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.inno.tasks.model.Logins;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.model.Users;
import ru.inno.tasks.repository.UsersRep;
import ru.inno.tasks.service.DataCheck.DataCheckDate;
import ru.inno.tasks.service.DataCheck.DataCheckFio;
import ru.inno.tasks.service.DataCheck.DataCheckType;
import ru.inno.tasks.service.DataReader.DataReader;
import ru.inno.tasks.service.DataWriter.DataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class DbWriterTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.name", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    DataReader dataReader;
    @Autowired
    DataWriter dataWriter;
    @Autowired
    DataCheckFio dataCheckFio;
    @Autowired
    DataCheckType dataCheckType;
    @Autowired
    DataCheckDate dataCheckDate;
    @Autowired
    UsersRep usersRep;

    @BeforeEach
    void setUp() {
        usersRep.deleteAll();
    }

    boolean compareDataWrite(List<Model> md, List<Users> users) {
        List<Model> mdDbLst = new ArrayList<>();
        for (Users u : users) {
            for (Logins lg : u.getLogins()) {
                Model modDb = new Model();
                modDb.setUsername(u.getUsername());
                modDb.setFio(u.getFio());
                modDb.setDateInput(lg.getAccess_date().format(dataCheckDate.getDateFormatter()));
                modDb.setApplType(lg.getApplication());
                mdDbLst.add(modDb);
            }
        }
        if (!(mdDbLst.size() == md.size()))
            return false;
        for (Model m1 : mdDbLst) {
            String m1Str = m1.getUsername() + m1.getFio() + m1.getDateInput() + m1.getApplType();
            boolean isFind = false;
            for (Model m2 : md) {
                String m2Str = m2.getUsername() + m2.getFio() + m2.getDateInput() + m2.getApplType();
                if (m1Str.equals(m2Str))
                    isFind = true;
            }
            if (!isFind)
                return false;
        }
        return true;
    }

    @Test
    void TestDb() throws IOException {
        Assertions.assertNotNull(dataReader);
        Assertions.assertNotNull(dataCheckDate);
        Assertions.assertNotNull(dataCheckType);
        Assertions.assertNotNull(dataCheckFio);
        Assertions.assertNotNull(dataWriter);
        List<Model> mods = dataReader.readFiles();
        then(!mods.isEmpty()).isTrue();
        mods = dataCheckType.check(mods);
        mods = dataCheckFio.check(mods);
        mods = dataCheckDate.check(mods);
        dataWriter.writeDb(mods);
        List<Users> users = (List<Users>) usersRep.findAll();
        then(compareDataWrite(mods, users)).isTrue();
        for (int i = 0; i < 20; i++) {
            int randomIdx = (int) (Math.random() * mods.size());
            Model mdTest = mods.get(randomIdx);
            then(usersRep.findByUsernameAndFio(mdTest.getUsername(), mdTest.getFio())).isNotEmpty();
        }

    }
}
