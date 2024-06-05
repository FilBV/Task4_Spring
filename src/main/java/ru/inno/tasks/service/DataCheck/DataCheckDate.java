package ru.inno.tasks.service.DataCheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.inno.tasks.model.Model;

import ru.inno.tasks.service.LogTransformation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataCheckDate implements DataCheckable {
    public DateTimeFormatter getDateFormatter(){
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }
    public LocalDateTime checkDate(String date) {
        LocalDateTime dateloc = null;
        try {
            if (!(date == null)) {
                dateloc = LocalDateTime.parse(date, getDateFormatter()); //!!
            }
        } catch (Exception ex) {
            System.out.println("Ошибка форматирования даты");
        }
        return dateloc;
    }

    private void writeFile(List<String> linesToWrite, String fullFileName) throws IOException {
        Path textFile = Paths.get(fullFileName); // "/Users/LearnJAVA/logFile"
        Files.write(textFile, linesToWrite, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
     private String getStrWrite(Model model) {
        return model.getFileName() + ": " +
                model.getUsername() + " " +
                model.getFio() + " ";
    }
    @Value("${file.name.check.date}")
    private String fileNameLog;

    public String getFileNameLog() {
        return (fileNameLog == null) ? "LogBlankDate.log" : fileNameLog;
    }

    @LogTransformation("LogComponentReader.log")
    @Override
    public List<Model> check(List<Model> mods) throws IOException {
        List<String> linesToFile = new ArrayList<>();
        List<Model> modelsOut = mods.stream().filter(x-> (!x.getDateInput().isEmpty())).collect(Collectors.toList());
        List<Model> linesToFileMod = mods.stream().filter(x-> (x.getDateInput().isEmpty())).toList();
        if (!linesToFileMod.isEmpty()) {
            for (Model mod : linesToFileMod){
                linesToFile.add(getStrWrite(mod));
            }

            writeFile(linesToFile, getFileNameLog() );
        }
        return modelsOut;
    }

}
