package com.zeta.service;

import com.zeta.dao.TableDAO;
import com.zeta.logger.AppLogger;
import com.zeta.model.Table;

import java.util.List;

public class TableService {
    private final TableDAO tableDAO;

    // single lock shared across all instances
    private static final Object FILE_LOCK = new Object();

    public TableService(TableDAO tableDAO) {
        this.tableDAO = tableDAO;
    }

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
                    AppLogger.warning(String.format("Invalid table ID: %d", tableId));
                    return false;
                }
            }

            // check availability
            for(Table table: tables) {
                if(tableIds.contains(table.getId()) && !table.isAvailable()) {
                    AppLogger.info(String.format("Sorry, table already booked: %s", Thread.currentThread().getName()));
                    return false;
                    // can't book, already booked table
                }
            }

            // lock tables
            for(Table table: tables) {
                if(tableIds.contains(table.getId())) {
                    table.lock();
                    AppLogger.info(String.format("Table booked successfully: %s", Thread.currentThread().getName()));
                }
            }

            // save updated tables
            tableDAO.saveAllTables(tables);

            return true;
        }
    }

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
            tableDAO.saveAllTables(tables);
        }
    }

    public List<Table> getAllTables() {
        return tableDAO.getAllTables();
    }
}
