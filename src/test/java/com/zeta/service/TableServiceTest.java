package com.zeta.service;

import com.zeta.dao.TableDAO;
import com.zeta.model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TableServiceTest {
    private TableDAO tableDAO;
    private TableService tableService;

    @BeforeEach
    void setup() {
        tableDAO = Mockito.mock(TableDAO.class);
        tableService = new TableService(tableDAO);
    }

    @Test
    void lockTablesShouldReturnTrueWhenDaoReturnsTrue() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);

        when(tableDAO.lockTables(ids)).thenReturn(true);

        boolean result = tableService.lockTables(ids);

        assertTrue(result);
    }

    @Test
    void lockTablesShouldCallDaoOnce() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);

        tableService.lockTables(ids);

        verify(tableDAO, times(1)).lockTables(ids);
    }

    @Test
    void releaseTablesShouldCallDaoReleaseMethod() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);

        tableService.releaseTables(ids);

        verify(tableDAO, times(1)).releaseTables(ids);
    }

    @Test
    void getAllTablesShouldReturnSameListFromDao() {
        List<Table> fakeTables = new ArrayList<>();
        fakeTables.add(new Table(1, 4, true));
        fakeTables.add(new Table(2, 2, false));

        when(tableDAO.getAllTables()).thenReturn(fakeTables);

        List<Table> result = tableService.getAllTables();

        assertEquals(2, result.size());
    }
}
