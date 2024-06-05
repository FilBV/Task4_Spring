package ru.inno.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.service.DataCheck.DataCheckDate;
import ru.inno.tasks.service.DataCheck.DataCheckFio;
import ru.inno.tasks.service.DataCheck.DataCheckType;
import ru.inno.tasks.service.DataCheck.DataCheckable;
import ru.inno.tasks.service.DataReader.DataReader;
import ru.inno.tasks.service.DataWriter.DataWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataMaker {
    private List<Model> models;
    private final DataReader dataReader;
    private final DataWriter dataWriter;
    List<DataCheckable> component = new ArrayList<>();

    @Autowired
    public DataMaker(DataReader dataReader
            ,  DataCheckFio dataCheckFio
            ,  DataCheckType dataCheckType
            ,  DataCheckDate dataCheckDate
            , DataWriter dateWriter) {

        this.dataReader = dataReader;
        this.dataWriter = dateWriter;

        this.component.add(dataCheckFio);
        this.component.add(dataCheckType);
        this.component.add(dataCheckDate);
    }

    public  void make() throws IOException {
        String strPath = dataReader.getPath();
        models = dataReader.readFromFiles(strPath);
        for (DataCheckable comp: component) {
            models = comp.check(models);
        }
        dataWriter.writeDb(models);
    }
}
