package ru.inno.tasks;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.service.DataCheck.DataCheckDate;
import ru.inno.tasks.service.DataCheck.DataCheckFio;
import ru.inno.tasks.service.DataCheck.DataCheckType;
import ru.inno.tasks.service.DataReader.DataReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateReaderTest {

    @BeforeEach
    void setup() {

    }
    @Test
    @Description("Testing the list of files in folders")
    void readFiles() throws IOException {
        DataReader dataReader = new DataReader();
        List<Model> models = dataReader.readFiles();
        Assertions.assertEquals(7, models.size());
    }

    @Test
    @Description("Testing FIO")
    void DataCheckFio() throws IOException {
        DataCheckFio dataCheck = new DataCheckFio();
        Assertions.assertEquals("Иванов Иван Иванович" , dataCheck.checkFio("иванов    иван    иванович"));
    }
    @Test
    @Description("Testing Type")
    void DataCheckType() throws IOException {
        DataCheckType dataCheck = new DataCheckType();
        Assertions.assertEquals("mobile" , dataCheck.checkType("mobile"));
        Assertions.assertEquals("web" , dataCheck.checkType("web"));
        Assertions.assertEquals("other:something" , dataCheck.checkType("something"));
        Assertions.assertEquals("other:" , dataCheck.checkType(null));
    }

    @Test
    @Description("Testing date conversion")
    void DataCheckDate() throws IOException {
        DataCheckDate dataCheck = new DataCheckDate();
        LocalDateTime date = dataCheck.checkDate("07.04.2024 12:00:00");
        Assertions.assertEquals(12 , date.getHour());
        Assertions.assertEquals(0 , date.getMinute());
        Assertions.assertEquals(0 , date.getSecond());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Assertions.assertEquals("07.04.2024" , date.format(dateTimeFormatter));
        date = dataCheck.checkDate("07.04.24 12:00:00");
        Assertions.assertEquals(null , date);
    }

    @Test
    @Description("File verification testing")
    void checkFiles() throws IOException {
        DataReader dataReader = new DataReader();
        List<Model> models = dataReader.readFiles();
        DataCheckFio dataCheckFio = new DataCheckFio();
        DataCheckType dataCheckType = new DataCheckType();
        DataCheckDate dataCheckDate = new DataCheckDate();
        models = dataCheckFio.check(models);
        for (Model md: models) {
            Assertions.assertEquals(md.getFio(), dataCheckFio.checkFio(md.getFio()));
        }

        models = dataCheckType.check(models);

        for (Model md: models) {
            Assertions.assertEquals(md.getApplType().replace("other:","")
                    , dataCheckType.checkType(md.getApplType()).replace("other:", ""));
        }

        models = dataCheckDate.check(models);
        Assertions.assertEquals(6, models.size());

    }
}
