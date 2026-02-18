package com.zeta.dao;

import com.zeta.model.Table;

import java.util.List;

public interface TableDAO {
    List<Table> getAllTables();
    void saveAllTables(List<Table> tables);
}
