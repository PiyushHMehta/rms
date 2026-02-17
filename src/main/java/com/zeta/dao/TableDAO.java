package com.zeta.dao;

import com.zeta.model.Table;

import java.util.List;

public interface TableDAO {
    List<Table> getAllTables();
    boolean lockTables(List<Integer> tableIds);
    void releaseTables(List<Integer> tableIds);
    void saveAllTables(List<Table> tables);
}
