package com.zeta.dao;

import com.zeta.io.FileHandler;
import com.zeta.model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileTableDAOTest {
    @Mock
    private FileHandler fileHandler;
    private FileTableDAO dao;
    private List<Table> tables;

    @BeforeEach
    void setup() throws Exception {
        fileHandler = Mockito.mock(FileHandler.class);

        String fakeFileData = "1,4,true\n" + "2,2,false\n";

        Mockito.when(fileHandler.getReader()).thenReturn(new BufferedReader(new StringReader(fakeFileData)));
        Mockito.when(fileHandler.getWriter()).thenReturn(Mockito.mock(java.io.BufferedWriter.class));

        Mockito.when(fileHandler.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(fakeFileData)));

        dao = new FileTableDAO(fileHandler);

        tables = dao.getAllTables();
    }

    @Test
    void getAllTablesReturnCorrectSize() {
        assertEquals(2, tables.size());
    }

    @Test
    void table1ShouldReturnId1() {
        assertEquals(1, tables.get(0).getId());
    }

    @Test
    void freeTablesShouldBeBooked() {
        List<Integer> tableIds = new java.util.ArrayList<>();
        tableIds.add(1);
        assertTrue(dao.lockTables(tableIds));
    }

    @Test void bookTableWithInvalidTableId() {
        List<Integer> tableIds = new java.util.ArrayList<>();
        tableIds.add(3);
        assertFalse(dao.lockTables(tableIds));
    }

    @Test
    void bookedTablesShouldNotBeBooked() {
        List<Integer> tableIds = new java.util.ArrayList<>();
        tableIds.add(2);
        assertFalse(dao.lockTables(tableIds));
    }

    @Test
    void releaseTableShouldMarkAvailabilityAsTrue() throws IOException {
        String fakeFileData = "1,4,true\n" + "2,2,false\n";

        Mockito.when(fileHandler.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(fakeFileData)));

        java.io.StringWriter stringWriter = new java.io.StringWriter();
        java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(stringWriter);

        Mockito.when(fileHandler.getWriter()).thenReturn(bufferedWriter);

        FileTableDAO dao = new FileTableDAO(fileHandler);

        List<Integer> tableIds = new ArrayList<>();
        tableIds.add(1);
        dao.releaseTables(tableIds);

//        bufferedWriter.flush();

        assertTrue(stringWriter.toString().contains("1,4,true"));
    }
}
