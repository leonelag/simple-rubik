package rubik;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CubeTest {
    private final Cube cube1 = Cube.fromResource("/cube1/base.txt");

    public CubeTest() throws IOException {}

    @Test
    public void testRow() {
        int r = Cube.makeRow(1, 2, 3);
        assertEquals(0b001_010_011, r);
    }

    @Test
    public void testFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        int r1 = 0b001_010_011
          , r2 = 0b100_101_110
          , r3 = 0b111_001_010;
        int expected = (r1 << 18) | (r2 << 9) | r3;
        assertEquals(expected, f);
    }

    @Test
    public void testAt() {
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
    public void testColCw() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);

        assertEquals(Cube.makeRow(7, 4, 1), Cube.colCw(f, 1));
        assertEquals(Cube.makeRow(1, 5, 2), Cube.colCw(f, 2));
        assertEquals(Cube.makeRow(2, 6, 3), Cube.colCw(f, 3));
    }

    @Test
    public void testColCcw() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);

        assertEquals(Cube.makeRow(3, 6, 2), Cube.colCcw(f, 3));
        assertEquals(Cube.makeRow(2, 5, 1), Cube.colCcw(f, 2));
        assertEquals(Cube.makeRow(1, 4, 7), Cube.colCcw(f, 1));
    }

    @Test
    public void testCwFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] { 7, 4, 1, 1, 5, 2, 2, 6, 3},
            Cube.faceToArray(Cube.cwFace(f)));
    }

    @Test
    public void testCcwFace() {
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] { 3, 6, 2, 2, 5, 1, 1, 4, 7 },
            Cube.faceToArray(Cube.ccwFace(f)));
    }

    @Test
    public void testReplaceRow() {
        int f1 = Cube.makeFace(1, 1, 1, 2, 2, 2, 3 ,3, 3);
        int expected = Cube.makeFace(4, 5, 6, 2, 2, 2, 3, 3, 3);
        assertEquals(expected, Cube.replaceRow(f1, 1, Cube.makeRow(4, 5, 6)));
    }

    @Test
    public void testReverseRow() {
        int row = Cube.makeRow(1, 2, 3);
        int expected = Cube.makeRow(3, 2, 1);
        assertEquals(expected, Cube.reverseRow(row));
    }

    @Test
    public void testReplaceCol() {
        int f1 = Cube.makeFace(
            1, 2, 3,
            4, 5, 6,
            1, 1, 2);
        int f2 = Cube.makeFace(
            2, 2, 2,
            5, 5, 5,
            1, 1, 1);
        int expected = Cube.makeFace(
            1, 2, 2,
            4, 5, 5,
            1, 1, 1);
        assertEquals(expected, Cube.replaceCol(f1, 3, Cube.col(f2, 3)));
    }

    @Test
    public void testRotateFace() {
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
    public void testShiftCol_1() {
        final int colRight = Cube.makeFace(       // col on the right side of the face
            0, 0, 1,
            0, 0, 2,
            0, 0, 3);
        final int colLeft = Cube.makeFace(        // col on the left side of the face
            1, 0, 0,
            2, 0, 0,
            3, 0, 0);
        final int colMid = Cube.makeFace(         // middle column
            0, 1, 0,
            0, 2, 0,
            0, 3, 0);

        assertEquals(colLeft,  Cube.shiftCol(3, 1, colRight));
        assertEquals(colRight, Cube.shiftCol(1, 3, colLeft));
        assertEquals(colMid,   Cube.shiftCol(3, 2, colRight));
        assertEquals(colMid,   Cube.shiftCol(1, 2, colLeft));

        assertEquals(colMid,  Cube.shiftCol(3, 2, colRight));
        assertEquals(colLeft, Cube.shiftCol(2, 1, colMid));
    }

    @Test
    public void testFaceToArray() {
        // Cube does not have 9 colors, only 6; but this works for testing the method.
        int f = Cube.makeFace(1, 2, 3, 4, 5, 6, 7, 1, 2);
        assertArrayEquals(
            new int[] {1, 2, 3, 4, 5, 6, 7, 1, 2},
            Cube.faceToArray(f)
        );
    }

    @Test
    public void testU() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/u.txt");
        assertEquals(expectedCubeU, cube1.U());
    }

    @Test
    public void test_U() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/_u.txt");
        assertEquals(expectedCubeU, cube1._U());
    }

    @Test
    public void testU2() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/u2.txt");
        assertEquals(expectedCubeU, cube1.U2());
    }

    @Test
    public void testL() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/l.txt");
        assertEquals(expectedCubeU, cube1.L());
    }

    @Test
    public void test_L() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/_l.txt");
        assertEquals(expectedCubeU, cube1._L());
    }

    @Test
    public void testL2() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/l2.txt");
        assertEquals(expectedCubeU, cube1.L2());
    }

    @Test
    public void testR() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/r.txt");
        assertEquals(expectedCubeU, cube1.R());
    }

    @Test
    public void test_R() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/_r.txt");
        assertEquals(expectedCubeU, cube1._R());
    }

    @Test
    public void testR2() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/r2.txt");
        assertEquals(expectedCubeU, cube1.R2());
    }

    @Test
    public void testD() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/d.txt");
        assertEquals(expectedCubeU, cube1.D());
    }

    @Test
    public void test_D() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/_d.txt");
        assertEquals(expectedCubeU, cube1._D());
    }

    @Test
    public void testD2() throws IOException {
        var expectedCubeU = Cube.fromResource("/cube1/d2.txt");
        assertEquals(expectedCubeU, cube1.D2());
    }

    @Test
    public void testDTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube1, cube1.D2().D2());
        assertEquals(cube1, cube1.D()._D());
        assertEquals(cube1, cube1._D().D());

        assertEquals(cube1.D(), cube1._D()._D()._D());
        assertEquals(cube1._D(), cube1.D().D().D());

        assertEquals(cube1.D2(), cube1.D().D());
        assertEquals(cube1.D2(), cube1._D()._D());
    }

    @Test
    public void testUTransforms() {
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
    public void testLTransforms() {
        /*
         * Rotate the LEFT back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube1, cube1.L2().L2());
        assertEquals(cube1, cube1.L()._L());
        assertEquals(cube1, cube1._L().L());

        assertEquals(cube1.L(), cube1._L()._L()._L());
        assertEquals(cube1._L(), cube1.L().L().L());

        assertEquals(cube1.L2(), cube1.L().L());
        assertEquals(cube1.L2(), cube1._L()._L());
    }

    @Test
    public void testRTransforms() {
        /*
         * Rotate RIGHT face back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube1, cube1.R2().R2());
        assertEquals(cube1, cube1.R()._R());
        assertEquals(cube1, cube1._R().R());

        assertEquals(cube1.R(), cube1._R()._R()._R());
        assertEquals(cube1._R(), cube1.R().R().R());

        assertEquals(cube1.R2(), cube1.R().R());
        assertEquals(cube1.R2(), cube1._R()._R());
    }

    @Test
    public void testFromResource() throws IOException {
        var cube = Cube.fromResource("/cube1/base.txt");
        assertNotNull(cube);
    }

    @Test
    public void testRead() throws IOException {
        var txt = slurp("/cube1/base.txt");

        var sc = new Scanner(txt);
        var cube = Cube.read(sc);
        System.out.println(cube);

        assertEquals(txt, cube.toString());
    }

    private String slurp(String resourceName) throws IOException {
        var res = getClass().getResource(resourceName);
        if (res == null) throw new FileNotFoundException("Resource not found: " + resourceName);
        var bytes = res.openStream().readAllBytes();
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
