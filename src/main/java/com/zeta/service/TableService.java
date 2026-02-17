package com.zeta.service;

import com.zeta.dao.TableDAO;
import com.zeta.model.Table;

import java.util.List;

public class TableService {
    private final TableDAO tableDAO;

    public TableService(TableDAO tableDAO) {
        this.tableDAO = tableDAO;
    }

    public boolean lockTables(List<Integer> tablesIds) {
        return tableDAO.lockTables(tablesIds);
    }

    public void releaseTables(List<Integer> tablesIds) {
        tableDAO.releaseTables(tablesIds);
    }

    public List<Table> getAllTables() {
        return tableDAO.getAllTables();
    }
}
