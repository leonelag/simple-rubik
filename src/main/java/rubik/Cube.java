package rubik;

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

    public static Cube read(Scanner sc) {
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

    /**
     * Rotates a face clockwise; does not affect other faces.
     */
    static int cwFace(int face) {
        return makeFace(
            colCw(face, 1),
            colCw(face, 2),
            colCw(face, 3));
    }

    static int ccwFace(int face) {
        return makeFace(
            colCcw(face, 3),
            colCcw(face, 2),
            colCcw(face, 1));
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
}
