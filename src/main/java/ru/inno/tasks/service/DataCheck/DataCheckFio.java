package ru.inno.tasks.service.DataCheck;

import org.springframework.stereotype.Component;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.service.LogTransformation;

import java.util.List;
import java.util.stream.Collectors;

@Component()
public class DataCheckFio implements DataCheckable {

    public String checkFio(String fio) {
        String[] strArr = fio.split(" ");
        StringBuilder strRes = new StringBuilder();
        for (String s : strArr) {
            String strAdd = s.trim();
            if (!strAdd.isEmpty())
                strRes.append(" ").append(strAdd.substring(0, 1).toUpperCase()).append(strAdd.substring(1).toLowerCase());
        }
        return strRes.toString().trim();
    }

    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> check(List<Model> mods) {
        mods.stream().peek(x-> x.setFio(checkFio(x.getFio()))).collect(Collectors.toList());
        return mods;
    }
}
