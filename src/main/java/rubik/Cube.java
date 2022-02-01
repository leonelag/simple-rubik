package rubik;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.Scanner;

public class Cube {
    final int top, left, front, right, back, bottom;

    private static OptionalInt parseInt(String s) {
        try {
            return OptionalInt.of(Integer.parseInt(s));
        } catch (NumberFormatException ex) {
            return OptionalInt.empty();
        }
    }

    private static String slurp(String resourceName) throws IOException {
        var res = Cube.class.getResource(resourceName);
        if (res == null) throw new FileNotFoundException("Resource not found: " + resourceName);
        var bytes = res.openStream().readAllBytes();
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static Cube fromResource(String resourceName) throws IOException {
        var txt = slurp(resourceName);
        var sc = new Scanner(txt);

        final int nCells = 9 * 6;
        final int[] arr = new int[nCells];
        for (int i = 0; i < nCells; i++) {
            OptionalInt val = parseInt(sc.next());
            while (val.isEmpty())
                val = parseInt(sc.next());
            arr[i] = val.getAsInt();
        }
        if (sc.hasNextInt())
            throw new IllegalArgumentException("Trailing data in input");

        int top = makeFace(arr, 0);

        // rows in each face
        var L = new ArrayList<Integer>();
        var F = new ArrayList<Integer>();
        var R = new ArrayList<Integer>();
        var B = new ArrayList<Integer>();

        ArrayList<Integer>[] faces = new ArrayList[]{L, F, R, B};
        int i = 9;
        for (int row = 1; row <= 3; row++) {
            for (var face: faces) {
                for (int col = 1; col <= 3; col++) {
                    face.add(arr[i++]);
                }
            }
        }
        int left   = makeFace(L)
          , front  = makeFace(F)
          , right  = makeFace(R)
          , back   = makeFace(B)
          , bottom = makeFace(arr, 9 * 5);
        return new Cube(top, left, front, right, back, bottom);
    }

    Cube(int top, int left, int front, int right, int back, int bottom) {
        this.top    = top;
        this.left   = left;
        this.front  = front;
        this.right  = right;
        this.back   = back;
        this.bottom = bottom;
    }

    public Cube U() {
        return new Cube(
            cwFace(top),
            replaceRow(left,  1, row(front, 1)),
            replaceRow(front, 1, row(right, 1)),
            replaceRow(right, 1, row(back, 1)),
            replaceRow(back,  1, row(left, 1)),
            bottom);
    }

    public Cube U_() {
        return new Cube(
            ccwFace(top),
            replaceRow(left,  1, row(back, 1)),
            replaceRow(front, 1, row(left,  1)),
            replaceRow(right, 1, row(front, 1)),
            replaceRow(back,  1, row(right, 1)),
            bottom);
    }

    public Cube U2() {
        return new Cube(
            rotateFace(top),
            replaceRow(left,  1, row(right, 1)),
            replaceRow(front, 1, row(back, 1)),
            replaceRow(right, 1, row(left, 1)),
            replaceRow(back,  1, row(front, 1)),
            bottom);
    }

    public Cube L() {
        return new Cube(
            replaceCol(top, 1, shiftCol(3, 1, reverseCol(col(back, 3)))),
            cwFace(left),
            replaceCol(front, 1, col(top, 1)),
            right,
            replaceCol(back, 3, shiftCol(1, 3, reverseCol(col(bottom, 1)))),
            replaceCol(bottom, 1, col(front, 1)));

    }

    public Cube _L() {
        return new Cube(
            replaceCol(top, 1, col(front, 1)),
            ccwFace(left),
            replaceCol(front, 1, col(bottom, 1)),
            right,
            replaceCol(back, 3, shiftCol(1, 3, reverseCol(col(top, 1)))),
            replaceCol(bottom, 1, shiftCol(3, 1, reverseCol(col(back, 3)))));
    }

    public Cube L2() {
        return new Cube(
            replaceCol(top, 1, col(bottom, 1)),
            rotateFace(left),
            replaceCol(front, 1, shiftCol(3, 1, reverseCol(col(back, 3)))),
            right,
            replaceCol(back, 3, shiftCol(1, 3, reverseCol(col(front, 1)))),
            replaceCol(bottom, 1, col(top, 1)));
    }

    public Cube R() {
        return new Cube(
            replaceCol(top, 3, col(front, 3)),
            left,
            replaceCol(front, 3, col(bottom, 3)),
            cwFace(right),
            replaceCol(back, 1, shiftCol(3, 1, reverseCol(col(top, 3)))),
            replaceCol(bottom, 3, shiftCol(1, 3, reverseCol(col(back, 1)))));
    }

    public Cube _R() {
        return new Cube(
            replaceCol(top, 3, shiftCol(1, 3, reverseCol(col(back, 1)))),
            left,
            replaceCol(front, 3, col(top, 3)),
            ccwFace(right),
            replaceCol(back, 1, shiftCol(3, 1, reverseCol(col(bottom, 3)))),
            replaceCol(bottom, 3, col(front, 3)));
    }

    public Cube R2() {
        return new Cube(
            replaceCol(top, 3, col(bottom, 3)),
            left,
            replaceCol(front, 3, shiftCol(1, 3, reverseCol(col(back, 1)))),
            rotateFace(right),
            replaceCol(back, 1, shiftCol(3, 1, reverseCol(col(front, 3)))),
            replaceCol(bottom, 3, col(top, 3)));
    }

    public Cube D() {
        return new Cube(
            top,
            replaceRow(left,  3, reverseRow(row(back,  3))),
            replaceRow(front, 3, row(left,  3)),
            replaceRow(right, 3, row(front, 3)),
            replaceRow(back,  3, reverseRow(row(right, 3))),
            cwFace(bottom));
    }

    public Cube _D() {
        return new Cube(
            top,
            replaceRow(left,  3, row(front, 3)),
            replaceRow(front, 3, row(right, 3)),
            replaceRow(right, 3, reverseRow(row(back,  3))),
            replaceRow(back, 3,  reverseRow(row(left,  3))),
            ccwFace(bottom));
    }

    public Cube D2() {
        return new Cube(
            top,
            replaceRow(left,  3, row(right, 3)),
            replaceRow(front, 3, reverseRow(row(back,  3))),
            replaceRow(right, 3, row(left,  3)),
            replaceRow(back,  3, reverseRow(row(front, 3))),
            rotateFace(bottom));
    }

    /**
     * New face with replaced row.
     */
    static int replaceRow(int face, int r, int newRow) {
        final int offset = 9 * (3 - r);
        final int maskErase = ~(0b111_111_111 << offset);
        return (face & maskErase) | (newRow << offset);
    }

    /**
     * New face with replaced row.
     */
    static int replaceCol(int face, int c, int newCol) {
        final int offset = 3 * (3 - c);
        final int maskErase = ~(0b000_000_111__000_000_111__000_000_111 << offset);
        return (face & maskErase) | newCol;
    }

    /**
     * Return new face with the column shifted.
     */
    static int shiftCol(int cFrom, int cTo, int col) {
        if (cFrom < cTo) {
            final int offset = 3 * (cTo - cFrom);
            return col >> offset;
        } else {
            final int offset = 3 * (cFrom - cTo);
            return col << offset;
        }
    }

    /**
     * Rotates a face CW; does not affect other faces.
     */
    static int cwFace(int face) {
        return makeFace(
            colCw(face, 1),
            colCw(face, 2),
            colCw(face, 3));
    }

    /**
     * Rotates a face CCW; does not affect other faces.
     */
    static int ccwFace(int face) {
        return makeFace(
            colCcw(face, 3),
            colCcw(face, 2),
            colCcw(face, 1));
    }

    /**
     * Rotates a face 180-degs; does not affect other faces.
     */
    static int rotateFace(int face) {
        return makeFace(
            reverseRow(row(face, 3)),
            reverseRow(row(face, 2)),
            reverseRow(row(face, 1)));
    }

    /**
     * Extract and rotate CW a single column from a face.
     * Column is returned as a row.
     */
    static int colCw(int face, int col) {
        final int colOffset = 3 * (3 - col);
        final int mask = 0b111;
        int offset1 = colOffset + 9 * 2
          , offset2 = colOffset + 9
          , offset3 = colOffset;

        return makeRow(
            (face & (mask << offset3)) >> offset3,
            (face & (mask << offset2)) >> offset2,
            (face & (mask << offset1)) >> offset1);
    }

    /**
     * Extract and rotate CCW a single column from a face.
     * Column is returned as a row.
     */
    static int colCcw(int face, int col) {
        final int colOffset = 3 * (3 - col);
        final int mask = 0b111;
        int offset1 = colOffset + 9 * 2
          , offset2 = colOffset + 9
          , offset3 = colOffset;
        return makeRow(
                (face & (mask << offset1)) >> offset1,
                (face & (mask << offset2)) >> offset2,
                (face & (mask << offset3)) >> offset3);
    }

    /**
     * The r-th row of the face.
     */
    static int row(int face, int r) {
        final int offset = 9 * (3 - r);
        final int mask = 0b111_111_111 << offset;
        return (face & mask) >> offset;
    }

    /**
     * The c-th col of the face.
     * Unlike {@link #colCw(int, int)} and {@link #colCcw(int, int)}, does not pack the column values in a row.
     */
    static int col(int face, int c) {
        final int offset = 3 * (3 - c);
        final int mask = 0b000_000_111__000_000_111__000_000_111 << offset;
        return face & mask;
    }

    /**
     * Reverses a row.
     */
    static int reverseRow(int row) {
        int maskC1 = 0b111 << 6,
            maskC2 = 0b111 << 3,
            maskC3 = 0b111;
        return makeRow(
            row & maskC3,
            (row & maskC2) >> 3,
            (row & maskC1) >> 6);
    }

    /**
     * Reverses a column. Result can be fed into {@link #replaceCol(int, int, int)}
     */
    static int reverseCol(int col) {
        /*
         * A column touches bits in all rows in the face.
         * Look at the col as if it were a face and move rows in place.
         * Then the rows pattern r1-r2-r3 become r3-r2-r1.
         */
        int row1 = row(col, 1),
            row2 = row(col, 2),
            row3 = row(col, 3);
        return makeFace(row3, row2, row1);
    }

    /**
     * Value of the cell in that face.
     */
    static int at(int face, int row, int col) {
        final int offset = 9 * (3 - row) + 3 * (3 - col);
        final int mask = 0b111 << offset;
        return (face & mask) >> offset;
    }

    /**
     * Make a row of a face.
     * Simply pack all three cells into an int.
     */
    static int makeRow(int c1, int c2, int c3) {
        return c1 << 6 | c2 << 3 | c3;
    }

    /**
     * Make a face from cells.
     * Simply pack all nine cells of the face into an int.
     */
    static int makeFace(int r11, int r12, int r13, int r21, int r22, int r23, int r31, int r32, int r33) {
        return makeFace(
                makeRow(r11, r12, r13),
                makeRow(r21, r22, r23),
                makeRow(r31, r32, r33));
    }

    /**
     * Make a face from rows.
     */
    static int makeFace(int row1, int row2, int row3) {
        return (row1 << (9 * 2))
            |  (row2 << 9)
            |   row3;
    }

    /**
     * Make a face from cells, but fetch from an array
     */
    static int makeFace(int[] arr, int from) {
        if (from + 9 > arr.length) throw new IndexOutOfBoundsException("Invalid input: " + from);
        int i = from;
        return makeFace(
                arr[i++], arr[i++], arr[i++],
                arr[i++], arr[i++], arr[i++],
                arr[i++], arr[i++], arr[i++]);
    }

    /**
     * Make a face from cells, but fetch from a list
     */
    static int makeFace(ArrayList<Integer> lst) {
        int i = 0;
        return makeFace(
            lst.get(i++), lst.get(i++), lst.get(i++),
            lst.get(i++), lst.get(i++), lst.get(i++),
            lst.get(i++), lst.get(i++), lst.get(i++));
    }

    static int[] faceToArray(int face) {
        int offset = 3 * 8;
        int mask = 0b111 << offset;
        int[] arr = new int[9];
        for (int i = 0; i < 9; i++) {
            arr[i] = (face & mask) >> offset;
            mask >>= 3;
            offset -= 3;
        }
        return arr;
    }

    @Override
    public String toString() {
        var edge  = "        +-------+\n";
        var longEdge = "+-------+-------+-------+-------+\n";
        var space = "        |";
        var sb = new StringBuilder(edge);
        for (int row = 1; row <= 3; row++) {
            sb.append(space);
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(top, row, col));
            }
            sb.append(" |\n");
        }
        sb.append(longEdge);
        for (int row = 1; row <= 3; row++) {
            sb.append("|");
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(left, row, col));
            }
            sb.append(" |");
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(front, row, col));
            }
            sb.append(" |");
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(right, row, col));
            }
            sb.append(" |");
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(back, row, col));
            }
            sb.append(" |\n");
        }
        sb.append(longEdge);
        for (int row = 1; row <= 3; row++) {
            sb.append(space);
            for (int col = 1; col <= 3; col++) {
                sb.append(' ');
                sb.append(at(bottom, row, col));
            }
            sb.append(" |\n");
        }
        sb.append(edge);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cube that = (Cube) o;
        return this.top    == that.top
            && this.left   == that.left
            && this.front  == that.front
            && this.right  == that.right
            && this.back   == that.back
            && this.bottom == that.bottom;
    }
}
