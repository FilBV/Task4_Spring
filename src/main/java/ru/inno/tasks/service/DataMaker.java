package ru.inno.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.service.DataCheck.DataCheckable;
import ru.inno.tasks.service.DataReader.DataReader;
import ru.inno.tasks.service.DataWriter.DataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataMaker {

    @Autowired
    private List<Model> models;
    @Autowired
    private DataReader dataReader;
    @Autowired
    private DataWriter dataWriter;
    @Autowired
    List<DataCheckable> component = new ArrayList<>();

    public void make() throws IOException {
        String strPath = dataReader.getPath();

        models = dataReader.readFromFiles(strPath);
        for (DataCheckable comp : component) {
            models = comp.check(models);
        }
        dataWriter.writeDb(models);
    }


}
