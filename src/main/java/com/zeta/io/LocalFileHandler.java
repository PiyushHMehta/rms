package com.zeta.io;

import java.io.*;

public class LocalFileHandler implements FileHandler {
    private final String filePath;

    public LocalFileHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    @Override
    public BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }
}
