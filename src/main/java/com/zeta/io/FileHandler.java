package com.zeta.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface FileHandler {
    BufferedReader getReader() throws IOException;
    BufferedWriter getWriter() throws IOException;
}
