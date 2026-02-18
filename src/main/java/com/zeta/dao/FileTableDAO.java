package com.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeta.model.Table;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileTableDAO implements TableDAO {

    private static final Object FILE_LOCK = new Object();
    private static final String FILE_NAME = "Data/tables.json";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Table> getAllTables() {
        synchronized (FILE_LOCK) {
            try {
                File file = new File(FILE_NAME);

                if (!file.exists()) {
                    throw new RuntimeException("tables.json file not found at: " + FILE_NAME);
                }

                return mapper.readValue(file, new TypeReference<List<Table>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Error reading tables.json", e);
            }
        }
    }

    @Override
    public void saveAllTables(List<Table> tables) {
        synchronized (FILE_LOCK) {
            try {
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File(FILE_NAME), tables);
            } catch (IOException e) {
                throw new RuntimeException("Error writing tables.json", e);
            }
        }
    }
}
