package ru.jswap.objects;

import org.springframework.stereotype.Component;

@Component
public class AccessParams {
    private short read;
    private short write;


    public AccessParams() {
    }

    public AccessParams(short read, short write) {
        this.read = read;
        this.write = write;
    }
    public void setParams(short numb) {
        this.read= (short) (numb%3);
        this.write=(short)(numb/3);

    }
    public short getParams() {
        return (short) (read+write*3);
    }
    public short getRead() {
        return read;
    }

    public void setRead(short read) {
        this.read = read;
    }

    public short getWrite() {
        return write;
    }

    public void setWrite(short write) {
        this.write = write;
    }
}
