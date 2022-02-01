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
            "/cube1",
            "/cube2"
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
        var expectedCube = Cube.fromResource(baseDir + "/u.txt");
        assertCubeEquals(expectedCube, cube.U());
    }

    @Test
    public void test_U() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/u_.txt");
        assertCubeEquals(expectedCubeU, cube.U_());
    }

    @Test
    public void testU2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/u2.txt");
        assertEquals(expectedCubeU, cube.U2());
    }

    @Test
    public void testUTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertCubeEquals(cube, cube.U2().U2());
        assertCubeEquals(cube, cube.U().U_());
        assertCubeEquals(cube, cube.U_().U());

        assertCubeEquals(cube.U(), cube.U_().U_().U_());
        assertCubeEquals(cube.U_(), cube.U().U().U());

        assertCubeEquals(cube.U2(), cube.U().U());
        assertCubeEquals(cube.U2(), cube.U_().U_());
    }

    @Test
    public void testL() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/l.txt");
        assertCubeEquals(expectedCubeU, cube.L());
    }

    @Test
    public void test_L() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/l_.txt");
        assertCubeEquals(expectedCubeU, cube._L());
    }

    @Test
    public void testL2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/l2.txt");
        assertCubeEquals(expectedCubeU, cube.L2());
    }

    @Test
    public void testLTransforms() {
        /*
         * Rotate the LEFT back and forth and make sure transformations are equivalent.
         */
        assertCubeEquals(cube, cube.L2().L2());
        assertCubeEquals(cube, cube.L()._L());
        assertCubeEquals(cube, cube._L().L());

        assertCubeEquals(cube.L(), cube._L()._L()._L());
        assertCubeEquals(cube._L(), cube.L().L().L());

        assertCubeEquals(cube.L2(), cube.L().L());
        assertCubeEquals(cube.L2(), cube._L()._L());
    }

    @Test
    public void testR() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/r.txt");
        assertCubeEquals(expectedCubeU, cube.R());
    }

    @Test
    public void test_R() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/r_.txt");
        assertCubeEquals(expectedCubeU, cube._R());
    }

    @Test
    public void testR2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/r2.txt");
        assertCubeEquals(expectedCubeU, cube.R2());
    }

    @Test
    public void testRTransforms() {
        /*
         * Rotate RIGHT face back and forth and make sure transformations are equivalent.
         */
        assertCubeEquals(cube, cube.R2().R2());
        assertCubeEquals(cube, cube.R()._R());
        assertCubeEquals(cube, cube._R().R());

        assertCubeEquals(cube.R(), cube._R()._R()._R());
        assertCubeEquals(cube._R(), cube.R().R().R());

        assertCubeEquals(cube.R2(), cube.R().R());
        assertCubeEquals(cube.R2(), cube._R()._R());
    }

    @Test
    public void testD() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/d.txt");
        assertCubeEquals(expectedCubeU, cube.D());
    }

    @Test
    public void test_D() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/d_.txt");
        assertCubeEquals(expectedCubeU, cube._D());
    }

    @Test
    public void testD2() throws IOException {
        var expectedCubeU = Cube.fromResource(baseDir + "/d2.txt");
        assertCubeEquals(expectedCubeU, cube.D2());
    }

    @Test
    public void testDTransforms() {
        /*
         * Rotate the top back and forth and make sure transformations are equivalent.
         */
        assertCubeEquals(cube, cube.D2().D2());
        assertCubeEquals(cube, cube.D()._D());
        assertCubeEquals(cube, cube._D().D());

        assertCubeEquals(cube.D(), cube._D()._D()._D());
        assertCubeEquals(cube._D(), cube.D().D().D());

        assertCubeEquals(cube.D2(), cube.D().D());
        assertCubeEquals(cube.D2(), cube._D()._D());
    }

    private void assertCubeEquals(Cube expected, Cube actual) {
        assertEquals(expected, actual);
        actual.validate();
    }
}
