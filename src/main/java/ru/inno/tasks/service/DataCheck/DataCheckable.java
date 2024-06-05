package ru.inno.tasks.service.DataCheck;

import ru.inno.tasks.model.Model;

import java.io.IOException;
import java.util.List;

public interface DataCheckable {
    List<Model> check(List<Model> mods) throws IOException;
}
