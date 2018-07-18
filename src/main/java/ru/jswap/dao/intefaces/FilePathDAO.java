package ru.jswap.dao.intefaces;

import ru.jswap.entities.FilePath;

public interface FilePathDAO {
    FilePath getPath(Integer id);
    int savePath(FilePath path);
    void deletePath(FilePath path);
    void deletePath(Integer id);
}
