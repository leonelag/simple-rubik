package rubik;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class ReadTest {
    @Parameterized.Parameters(name="{0}")
    public static Collection<String> baseDirs() {
        return Arrays.asList(
            "/cube1",
            "/cube2"
        );
    }

    private final String baseDir;

    public ReadTest(String baseDir) {
        this.baseDir = baseDir;
    }

    @Test
    public void testFromResource() throws IOException {
        var resourceName = baseDir + "/base.txt";
        var cube = Cube.fromResource(resourceName);
        assertNotNull(cube);

        var expected = Cube.fromResource(baseDir + "/base_nocomments.txt");
        assertEquals(expected, cube);
    }
}
