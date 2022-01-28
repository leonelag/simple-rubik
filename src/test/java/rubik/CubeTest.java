package rubik;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CubeTest {
    private Cube cube1 = Cube.fromResource("/cube1.txt");

    public CubeTest() throws IOException {}

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

        assertEquals(Cube.makeRow(7, 4, 1), Cube.colCw(f, 1));
        assertEquals(Cube.makeRow(1, 5, 2), Cube.colCw(f, 2));
        assertEquals(Cube.makeRow(2, 6, 3), Cube.colCw(f, 3));
    }

    @Test
    void testColCcw() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);

        assertEquals(Cube.makeRow(3, 6, 2), Cube.colCcw(f, 3));
        assertEquals(Cube.makeRow(2, 5, 1), Cube.colCcw(f, 2));
        assertEquals(Cube.makeRow(1, 4, 7), Cube.colCcw(f, 1));
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
    void testReplaceRow() {
        int f1 = Cube.makeFace(1, 1, 1, 2, 2, 2, 3 ,3, 3);
        int expected = Cube.makeFace(4, 5, 6, 2, 2, 2, 3, 3, 3);
        assertEquals(expected, Cube.replaceRow(f1, 1, Cube.makeRow(4, 5, 6)));
    }

    @Test
    void testReverseRow() {
        int row = Cube.makeRow(1, 2, 3);
        int expected = Cube.makeRow(3, 2, 1);
        assertEquals(expected, Cube.reverseRow(row));
    }

    @Test
    void testRotateFace() {
        int f = Cube.makeFace(
                1, 2, 3,
                4, 5, 6,
                7, 1, 2);
        int expected = Cube.makeFace(
                2, 1, 7,
                6, 5, 4,
                3, 2, 1);
        assertEquals(expected, Cube.rotateFace(f));
    }

    @Test
    void testFaceToArray() {
        // Cube does not have 9 colors, only 6; but this works for testing the method.
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] {1, 2, 3, 4, 5, 6, 7, 1, 2},
            Cube.faceToArray(f)
        );
    }

    @Test
    void testU() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1-u.txt");
        assertEquals(expectedCubeU, cube1.U());
    }

    @Test
    void test_U() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1-_u.txt");
        assertEquals(expectedCubeU, cube1._U());
    }

    @Test
    void testU2() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1-u2.txt");
        assertEquals(expectedCubeU, cube1.U2());
    }

    @Test
    void testUTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube1, cube1.U2().U2());
        assertEquals(cube1, cube1.U()._U());
        assertEquals(cube1, cube1._U().U());

        assertEquals(cube1.U(), cube1._U()._U()._U());
        assertEquals(cube1._U(), cube1.U().U().U());

        assertEquals(cube1.U2(), cube1.U().U());
        assertEquals(cube1.U2(), cube1._U()._U());
    }

    @Test
    void testFromResource() throws IOException {
        var cube = Cube.fromResource("/cube1.txt");
        assertNotNull(cube);
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
