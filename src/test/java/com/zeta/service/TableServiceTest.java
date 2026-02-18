package com.zeta.service;

import com.zeta.dao.TableDAO;
import com.zeta.model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TableServiceTest {
    private TableDAO tableDAO;
    private TableService tableService;

    @BeforeEach
    void setup() {
        tableDAO = mock(TableDAO.class);
        tableService = new TableService(tableDAO);
    }

    @Test
    void getAllTablesShouldReturnListFromDAO() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, true));
        fakeTables.add(new Table(2, 2, false));
        
        when(tableDAO.getAllTables()).thenReturn(fakeTables);
        
        List<Table> result = tableService.getAllTables();
        
        assertEquals(2, result.size());
    }

    @Test
    void lockTablesShouldReturnFalseIfTableIdDoesNotExist() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, true));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Integer> tableIds = new ArrayList<>();
        tableIds.add(5);
        boolean result = tableService.lockTables(tableIds);

        assertFalse(result);
    }

    @Test
    void lockTablesShouldReturnFalseIfTableAlreadyBooked() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, false));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Integer> tableIds = new ArrayList<>();
        tableIds.add(1);
        boolean result = tableService.lockTables(tableIds);

        assertFalse(result);
    }

    @Test
    void lockTablesShouldLockTableAndSaveWhenAvailable() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, true));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Integer> tableIds = new ArrayList<>();
        tableIds.add(1);
        boolean result = tableService.lockTables(tableIds);

        assertTrue(result);

        verify(tableDAO, times(1)).saveAllTables(anyList());
    }

    @Test
    void releaseTablesShouldMakeTableAvailableAgain() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, false));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        tableService.releaseTables(ids);

        assertTrue(fakeTables.get(0).isAvailable());
    }

    @Test
    void releaseTablesShouldCallSaveAllTables() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, false));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        tableService.releaseTables(ids);

        verify(tableDAO, times(1)).saveAllTables(anyList());
    }
}
