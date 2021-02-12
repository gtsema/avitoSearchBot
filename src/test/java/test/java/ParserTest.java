package test.java;

import avitoParser.Parser;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTest {

    private Parser parser;
    private String correctLocalPage;
    private String correctGlobalPage;
    private String incorrectLocalPage;
    private String incorrectGlobalPage;

    private List<String> localPagesLinks;
    private Document localFileDocument;
    private Document globalFileDocument;

    @BeforeAll
    public void setUp() {
        parser = new Parser(null);

        correctLocalPage = PrepareResources.correctLocalPageLink;
        correctGlobalPage = PrepareResources.correctGlobalPageLink;
        incorrectLocalPage = PrepareResources.incorrectLocalPageLink;
        incorrectGlobalPage = PrepareResources.incorrectGlobalPageLink;

        localPagesLinks = PrepareResources.getLocalFilePagesLinks();
        localFileDocument = PrepareResources.getLocalFileDocument();
        globalFileDocument = PrepareResources.getGlobalFileDocument();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("getDoc()_correctLocalFile_documentClass")
    public void getDoc_correctLocalFile() {
        try {
            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctLocalPage);
            assertEquals(obj.getClass(), localFileDocument.getClass());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getDoc()_incorrectLocalPathFile_FNFException")
    public void getDoc_incorrectLocalPathFile() {

        String incorrectLocalTypeFile = "src/main/java/avitoParser/Parser.java";

        try {
            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
            method.setAccessible(true);

            Throwable thrown = assertThrows(Exception.class, () -> {
                    method.invoke(parser, incorrectLocalTypeFile);
            });

            assertEquals(FileNotFoundException.class, thrown.getCause().getClass());

        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getDoc()_incorrectLocalTypeFile_FNFException")
    public void getDoc_incorrectLocalTypeFile() {
        try {
            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
            method.setAccessible(true);

            Throwable thrown = assertThrows(Exception.class, () -> {
                method.invoke(parser, incorrectLocalPage);
            });

            assertEquals(FileNotFoundException.class, thrown.getCause().getClass());

        } catch (NoSuchMethodException e) {
            fail();
        }
    }

















//    @Test
//    @DisplayName("getDoc()_incorrectLocalPage_IOException")
//    public void getDoc_incorrectLocal() {
//        try {
//            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
//            method.setAccessible(true);
//            assertThrows(IOException.class, () -> {
//                Object obj = method.invoke(parser, incorrectLocalPage);
//            });
//        } catch (NoSuchMethodException e) {
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("getDoc()_correctGlobalPage_documentObj")
//    public void getDoc_correctGlobal() {
//        try {
//            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
//            method.setAccessible(true);
//            Object obj = method.invoke(parser, correctGlobalPage);
//            assertEquals(obj.getClass(), globalFileDocument.getClass());
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            fail();
//        }
//    }

    //------------------------------------------------------------------------------------------------------------------

//    @Test
//    @DisplayName("Test parse local page")
//    public void testParse() {
//        Parser parser = new Parser(localPage);
//        assertEquals(54, parser.parse().size());
//    }
//
//    @Test
//    @DisplayName("Test getPages() private method")
//    public void testGetPages() {
//        try {
//            Method method = Parser.class.getDeclaredMethod("getPages", String.class);
//            method.setAccessible(true);
//            Object obj = method.invoke(new Parser(localPage), localPage);
//            assertEquals(obj, localPageSampleLinks);
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("Test parsePage() private method")
//    public void testParsePage() {
//        try {
//            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
//            method.setAccessible(true);
//            Object obj = method.invoke(new Parser(localPage), localPage);
//
//            assertEquals(((List<Advert>)obj).size(), 54);
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            fail();
//        }
//    }
}
