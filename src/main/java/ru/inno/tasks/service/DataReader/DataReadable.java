package ru.inno.tasks.service.DataReader;

import ru.inno.tasks.model.Model;
import java.io.IOException;
import java.util.List;


public interface DataReadable {
    List<Model> readFromFiles(String strPath) throws IOException;
}

