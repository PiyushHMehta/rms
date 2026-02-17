package com.zeta.dao;

import com.zeta.io.FileHandler;
import com.zeta.model.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTableDAO implements TableDAO {
//    private final String filePath;
    private final FileHandler fileHandler;

    // single lock shared across all instances
    private static final Object FILE_LOCK = new Object();

    public FileTableDAO(FileHandler fileHandler) {
//        this.filePath = filePath;
        this.fileHandler = fileHandler;
    }

//    public String getFilePath() {
//        return filePath;
//    }

    @Override
    public List<Table> getAllTables() {
        synchronized(FILE_LOCK) {
            List<Table> tables = new ArrayList<>();

            // read table file, make table object and add in tables list
            try(BufferedReader bufferedReader = fileHandler.getReader()) {
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int capacity = Integer.parseInt(parts[1]);
                    boolean available = Boolean.parseBoolean(parts[2]);

                    tables.add(new Table(id, capacity, available));
                }
            } catch (IOException e) {
                System.out.println("Error reading tables file");
            }
            return tables;
        }
    }

    @Override
    public boolean lockTables(List<Integer> tableIds) {
        synchronized(FILE_LOCK) {
            List<Table> tables = getAllTables();

            for(int tableId : tableIds) {
                boolean exists = false;

                for (Table table : tables) {
                    if (table.getId() == tableId) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    System.out.println("Invalid table ID: " + tableId);
                    return false;
                }
            }

            // check availability
            for(Table table: tables) {
                if(tableIds.contains(table.getId()) && !table.isAvailable()) {
                    return false;
                    // can't book, already booked table
                }
            }

            // lock tables
            for(Table table: tables) {
                if(tableIds.contains(table.getId())) {
                    table.lock();
                }
            }

            // save updated tables
            saveAllTables(tables);

            return true;
        }
    }

    @Override
    public void releaseTables(List<Integer> tableIds) {
        synchronized(FILE_LOCK) {
            List<Table> tables = getAllTables();

            // release tables
            for(Table table: tables) {
                if(tableIds.contains(table.getId())) {
                    table.release();
                }
            }

            // save updated tables
            saveAllTables(tables);
        }
    }

    public void saveAllTables(List<Table> tables) {
        // write back updated table status to file
        synchronized(FILE_LOCK) {
//            File file = new File(filePath);
            try(BufferedWriter bufferedWriter = fileHandler.getWriter()) {
                for(Table table: tables) {
                    String line = table.getId() + "," + table.getCapacity() + "," + table.isAvailable();
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            } catch(IOException e) {
                System.out.println("Error writing tables file");
            }
        }
    }
}