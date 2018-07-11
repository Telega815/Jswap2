package ru.jswap.objects;

import org.springframework.stereotype.Component;

@Component
public class AccessParams {
    private int read;
    private int write;


    public AccessParams() {
    }

    public AccessParams(int read, int write) {
        this.read = read;
        this.write = write;
    }
    public void setParams(int numb) {
        this.read=numb%3;
        this.write=numb/3;

    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getWrite() {
        return write;
    }

    public void setWrite(int write) {
        this.write = write;
    }
}
