package com.example.boardroombookingsystem.io;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.boardroombookingsystem.model.Boardroom;
import com.example.boardroombookingsystem.model.FileExtensionType;
import com.example.boardroombookingsystem.model.Result;

public interface FileReader {
    FileExtensionType getFileExtensionType();

    void createInputStream() throws FileNotFoundException;

    void setInputStream(InputStream inputStream);

    void read();

    Result<Boardroom> getBoardroom();
}
