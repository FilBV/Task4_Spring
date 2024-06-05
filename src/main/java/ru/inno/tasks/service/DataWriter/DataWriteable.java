package ru.inno.tasks.service.DataWriter;

import ru.inno.tasks.model.Model;
import java.util.List;

public interface DataWriteable {
    void writeDb(List<Model> mods);
}
