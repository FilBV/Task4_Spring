package ru.inno.tasks.service.DataCheck;

import org.springframework.stereotype.Component;
import ru.inno.tasks.model.Model;
import ru.inno.tasks.service.LogTransformation;

import java.util.List;

@Component
public class DataCheckType implements DataCheckable {
    public String checkType(String applType) {
        if (applType == null)
            applType = "";
        if (!applType.equals("web") && !applType.equals("mobile"))
            return "other:" + applType;
        return applType;
    }

    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> check(List<Model> mods) {
        mods.stream().peek(x-> x.setApplType(checkType(x.getApplType()))).toList();//.collect(Collectors.toList());
        return mods;
    }
}
