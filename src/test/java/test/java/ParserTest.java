package test.java;

import avitoParser.Parser;
import com.google.common.collect.Sets;
import dbService.entities.Advert;
import exceptions.ParserException;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTest {

    private Parser parser;
    private String correctPathToExampleAdvertsDataFile;
    private String correctPathToExamplePagesDataFile;
    private String incorrectPath;
    private String incorrectFilePath;
    private String empty;
    private String AdvertsWithoutIdOrHref;
    private String AdvertsWithSameId;

    private Set<Advert> expectedAdvSet;
    private Set<String> expectedPagesSet;

    @BeforeAll
    public void setUp() {
        parser = new Parser();
        correctPathToExampleAdvertsDataFile = "src/test/resources/2TestAdverts.htm";
        correctPathToExamplePagesDataFile = "src/test/resources/2TestPages.htm";
        incorrectPath = "src/test/resources";
        incorrectFilePath = "src/main/java/avitoParser/Parser.java";
        empty = "src/test/resources/empty.htm";
        AdvertsWithoutIdOrHref = "src/test/resources/2TestAdvertsWithoutIdOrHref.htm";
        AdvertsWithSameId = "src/test/resources/2TestAdvertsWithSameId's.htm";

        expectedAdvSet = new HashSet<>() {{
            add(new Advert("/sankt-peterburg/audio_i_video/vinilovyy_proigryvatel_stantion_t_52_2065691171",
                           2065691171,
                           "Виниловый проигрыватель stantion t 52",
                           "Парнас",
                           1500,
                           10000));
            add(new Advert("/sankt-peterburg/audio_i_video/vinilovyy_proigryvatel_2075016916",
                           2075016916,
                           "Виниловый проигрыватель",
                           "Пролетарская",
                           2300,
                           9000));
        }};
        expectedPagesSet = new HashSet<>() {{
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?localPriority=0&pmax=1500&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C&user=1");
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?p=2&localPriority=0&pmax=1500&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C&user=1");
        }};
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("parse()_correctPath_List<Advert>")
    public void parse_correctPath() {
        try {
            Set<Advert> actual = parser.parse(correctPathToExampleAdvertsDataFile);
            assertEquals(actual, expectedAdvSet);
        } catch (ParserException e) {
            fail();
        }
    }

    @Test
    @DisplayName("parse()_incorrectPath_ParserException")
    public void parse_incorrectPath() {
        try {
            assertThrows(ParserException.class, () -> parser.parse(incorrectPath));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @DisplayName("parse()_incorrectFile_ParserException")
    public void parse_incorrectFile() {
        try {
            assertThrows(ParserException.class, () -> parser.parse(incorrectFilePath));
        } catch (Exception e) {
            fail();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("getDoc()_correctLocalPath_document")
    public void getDoc_correctLocalPath() {
        try {
            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctPathToExampleAdvertsDataFile);
            assertEquals(obj.getClass(), Document.class);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getDoc()_incorrectPath_IOException")
    public void getDoc_incorrectPath() {
        try {
            Method method = Parser.class.getDeclaredMethod("getDoc", String.class);
            method.setAccessible(true);
            assertThrows(IOException.class, () -> {
                try {
                    method.invoke(parser, incorrectPath);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("getPages_correctPathTwoPage_Set<String>withTwoPagePath")
    public void getPages_correctPathTwoPage() {
        try {
            Method method = Parser.class.getDeclaredMethod("getPages", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctPathToExamplePagesDataFile);
            assertEquals(obj, expectedPagesSet);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getPages_correctPathOnePage_Set<String>withOnePagePath")
    public void getPages_correctPathOnePage() {
        try {
            Method method = Parser.class.getDeclaredMethod("getPages", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, empty);
            assertEquals(obj, new HashSet<>() {{ add(empty); }});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("parsePage_correctPathToFileWithTwoAdverts_Set<Advert>")
    public void parsePage_correctPathTwoAdverts() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctPathToExampleAdvertsDataFile);
            assertEquals(obj, expectedAdvSet);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("parsePage_correctPathToEmptyFile_IOException")
    public void parsePage_correctPathToEmptyFile() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            assertThrows(IOException.class, () -> {
                try {
                    method.invoke(parser, empty);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    @Test
    @DisplayName("parsePage_withoutIdOrHref_emptySet<Advert>")
    public void parsePage_withoutIdOrHref() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, AdvertsWithoutIdOrHref);
            assertEquals(obj, new HashSet<>());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("parsePage_sameId's_Set<Advert>widthFirstAdv")
    public void parsePage_sameId() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, AdvertsWithSameId);
            assertEquals(obj, new HashSet<Advert>() {{ add(expectedAdvSet.stream().findFirst().get()); }});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }
}
