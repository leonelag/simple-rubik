package rubik;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StepsTest {
    @Parameterized.Parameters(name="{0}")
    public static Collection<String> baseDirs() {
        return Arrays.asList(
            "/cube1"
//            "/cube2"
        );
    }

    private final String baseDir;
    private final Cube cube;

    public StepsTest(String baseDir) throws IOException {
        this.baseDir = baseDir;
        cube = Cube.fromResource(baseDir + "/base.txt");
    }


    @Test
    public void testU() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/u.txt");
        assertEquals(expectedCubeU, cube.U());
    }

    @Test
    public void test_U() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/_u.txt");
        assertEquals(expectedCubeU, cube._U());
    }

    @Test
    public void testU2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/u2.txt");
        assertEquals(expectedCubeU, cube.U2());
    }

    @Test
    public void testL() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/l.txt");
        assertEquals(expectedCubeU, cube.L());
    }

    @Test
    public void test_L() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/_l.txt");
        assertEquals(expectedCubeU, cube._L());
    }

    @Test
    public void testL2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/l2.txt");
        assertEquals(expectedCubeU, cube.L2());
    }

    @Test
    public void testR() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/r.txt");
        assertEquals(expectedCubeU, cube.R());
    }

    @Test
    public void test_R() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/_r.txt");
        assertEquals(expectedCubeU, cube._R());
    }

    @Test
    public void testR2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/r2.txt");
        assertEquals(expectedCubeU, cube.R2());
    }

    @Test
    public void testD() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/d.txt");
        assertEquals(expectedCubeU, cube.D());
    }

    @Test
    public void test_D() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/_d.txt");
        assertEquals(expectedCubeU, cube._D());
    }

    @Test
    public void testD2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/d2.txt");
        assertEquals(expectedCubeU, cube.D2());
    }

    @Test
    public void testDTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube, cube.D2().D2());
        assertEquals(cube, cube.D()._D());
        assertEquals(cube, cube._D().D());

        assertEquals(cube.D(), cube._D()._D()._D());
        assertEquals(cube._D(), cube.D().D().D());

        assertEquals(cube.D2(), cube.D().D());
        assertEquals(cube.D2(), cube._D()._D());
    }

    @Test
    public void testUTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube, cube.U2().U2());
        assertEquals(cube, cube.U()._U());
        assertEquals(cube, cube._U().U());

        assertEquals(cube.U(), cube._U()._U()._U());
        assertEquals(cube._U(), cube.U().U().U());

        assertEquals(cube.U2(), cube.U().U());
        assertEquals(cube.U2(), cube._U()._U());
    }

    @Test
    public void testLTransforms() {
        /*
         * Rotate the LEFT back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube, cube.L2().L2());
        assertEquals(cube, cube.L()._L());
        assertEquals(cube, cube._L().L());

        assertEquals(cube.L(), cube._L()._L()._L());
        assertEquals(cube._L(), cube.L().L().L());

        assertEquals(cube.L2(), cube.L().L());
        assertEquals(cube.L2(), cube._L()._L());
    }

    @Test
    public void testRTransforms() {
        /*
         * Rotate RIGHT face back and forth and make sure transformations are equivalent.
         */
        assertEquals(cube, cube.R2().R2());
        assertEquals(cube, cube.R()._R());
        assertEquals(cube, cube._R().R());

        assertEquals(cube.R(), cube._R()._R()._R());
        assertEquals(cube._R(), cube.R().R().R());

        assertEquals(cube.R2(), cube.R().R());
        assertEquals(cube.R2(), cube._R()._R());
    }
}
