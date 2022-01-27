package rubik;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CubeTest {
    @Test
    void testRow() {
        int r = Cube.makeRow(1, 2, 3);
        assertEquals(0b001_010_011, r);
    }

    @Test
    void testFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        int r1 = 0b001_010_011
          , r2 = 0b100_101_110
          , r3 = 0b111_001_010;
        int expected = (r1 << 18) | (r2 << 9) | r3;
        assertEquals(expected, f);
    }

    @Test
    void testAt() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertEquals(1, Cube.at(f, 1, 1));
        assertEquals(2, Cube.at(f, 1, 2));
        assertEquals(3, Cube.at(f, 1, 3));
        assertEquals(4, Cube.at(f, 2, 1));
        assertEquals(5, Cube.at(f, 2, 2));
        assertEquals(6, Cube.at(f, 2, 3));
        assertEquals(7, Cube.at(f, 3, 1));
        assertEquals(1, Cube.at(f, 3, 2));
        assertEquals(2, Cube.at(f, 3, 3));
    }

    @Test
    void testColCw() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);

        Assertions.assertEquals(Cube.makeRow(7, 4, 1), Cube.colCw(f, 1));
        Assertions.assertEquals(Cube.makeRow(1, 5, 2), Cube.colCw(f, 2));
        Assertions.assertEquals(Cube.makeRow(2, 6, 3), Cube.colCw(f, 3));
    }

    @Test
    void testColCcw() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);

        Assertions.assertEquals(Cube.makeRow(3, 6, 2), Cube.colCcw(f, 3));
        Assertions.assertEquals(Cube.makeRow(2, 5, 1), Cube.colCcw(f, 2));
        Assertions.assertEquals(Cube.makeRow(1, 4, 7), Cube.colCcw(f, 1));
    }

    @Test
    void testCwFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] { 7, 4, 1, 1, 5, 2, 2, 6, 3},
            Cube.faceToArray(Cube.cwFace(f)));
    }

    @Test
    void testCcwFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] { 3, 6, 2, 2, 5, 1, 1, 4, 7 },
            Cube.faceToArray(Cube.ccwFace(f)));
    }
    @Test
    void testFaceToArray() {
        // Cube does not have 9 colors, only 6; but this works for testing the method.
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        Assertions.assertArrayEquals(
            new int[] {1, 2, 3, 4, 5, 6, 7, 1, 2},
            Cube.faceToArray(f)
        );
    }

    @Test
    void testRead() throws IOException {
        var txt = slurp("/cube1.txt");

        var sc = new Scanner(txt);
        var cube = Cube.read(sc);
        System.out.println(cube.toString());

        assertEquals(txt, cube.toString());
    }

    private String slurp(String resourceName) throws IOException {
        var res = getClass().getResource(resourceName);
        if (res == null) throw new FileNotFoundException("Resource not found: " + resourceName);
        var bytes = res.openStream().readAllBytes();
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
