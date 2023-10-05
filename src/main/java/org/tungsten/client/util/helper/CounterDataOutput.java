package org.tungsten.client.util.helper;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;

public class CounterDataOutput implements DataOutput {
    @Getter
    long total;

    private void i(int b) {
        total += b;
    }

    @Override
    public void write(int b) throws IOException {
        i(1);
    }

    @Override
    public void write(byte @NotNull [] b) throws IOException {
        i(b.length); // one byte per element
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        i(len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        i(1);
    }

    @Override
    public void writeByte(int v) throws IOException {
        i(1);
    }

    @Override
    public void writeShort(int v) throws IOException {
        i(2);
    }

    @Override
    public void writeChar(int v) throws IOException {
        i(2);
    }

    @Override
    public void writeInt(int v) throws IOException {
        i(4);
    }

    @Override
    public void writeLong(long v) throws IOException {
        i(8);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        i(4);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        i(8);
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        i(s.length());
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        i(s.length() * 2);
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        i(2); // 2 bytes to denote length of string
        for (char c : s.toCharArray()) {
            if (c >= 0b0000_0001 && c <= 0b0111_1111) { // 01 - 7F, one (signed) byte
                i(1);
            } else if (c > 0b0111_1111_1111) { // above 07FF, need 3 (signed) bytes to denote
                i(3);
            } else {
                i(2); // between 80 - 7FF or 00, use 2 (signed) bytes
            }
        }
    }
}
