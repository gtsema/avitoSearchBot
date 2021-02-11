package test.java;

import avitoParser.Parser;
import dbService.entities.Advert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTest {

    private String localPage;
    private List<String> localPageSampleLinks;

    @BeforeAll
    public void setUp() {
        PrepareResources resources = new PrepareResources();

        localPage = resources.getLocalPageLink();
        localPageSampleLinks = resources.getLocalFilePagesLinks();
    }

    @Test
    @DisplayName("Test parse local page")
    public void testParse() {
        Parser parser = new Parser(localPage);
        assertEquals(54, parser.parse().size());
    }

    @Test
    @DisplayName("Test getPages() private method")
    public void testGetPages() {
        try {
            Method method = Parser.class.getDeclaredMethod("getPages", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(new Parser(localPage), localPage);
            assertEquals(obj, localPageSampleLinks);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Test parsePage() private method")
    public void testParsePage() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(new Parser(localPage), localPage);

            assertEquals(((List<Advert>)obj).size(), 54);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Test parsePage() private method")
    public void getAdvertPrice() {

    }
}
