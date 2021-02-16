package test.java;

import avitoParser.Parser;
import dbService.entities.Advert;
import exceptions.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTests {

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

    private Element correctAdvElement;
    private Element incorrectAdvElement;

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

        String advHtml = "<div data-marker=\"item\" data-item-id=\"2065691171\" id=\"i2065691171\" class=\"iva-item-root-G3n7v photo-slider-slider-3tEix iva-item-list-2_PpT items-item-1Hoqq items-listItem-11orH js-catalog-item-enum\" itemscope=\"\" itemType=\"http://schema.org/Product\"><div class=\"iva-item-content-m2FiN\"><div class=\"iva-item-slider-37uKh\"><div class=\"iva-item-favoritesStep-xDyvH\"><span class=\"tooltip-tooltip-box-2rApK\"><span class=\"tooltip-target-wrapper-XcPdv\"><div data-marker=\"favorites-add\" data-state=\"empty\" class=\"styles-root-3BW8A iva-item-favorite-3cxBX\" title=\"Добавить в избранное и в сравнение\"></div></span></span></div><a class=\"iva-item-sliderLink-2hFV_\" itemProp=\"url\" href=\"/sankt-peterburg/audio_i_video/vinilovyy_proigryvatel_stantion_t_52_2065691171\" target=\"_blank\" title=\"Объявление «Виниловый проигрыватель stantion t 52» 2 фотографии\" rel=\"noopener\"><div class=\"photo-slider-root-1w2KO\" data-marker=\"item-photo\"><div class=\"photo-slider-photoSlider-1vw51 photo-slider-aspect-ratio-4-3-q_elM\"><ul class=\"photo-slider-list-1RVxp\"><li class=\"photo-slider-list-item-35GzI\" data-marker=\"slider-image/image-https://75.img.avito.st/image/1/JuQxHraFig1Hu3gLcXYpu8u9gAuTvYoL9N6OC0e7eAuHuYYJh7meDYe_ikk\"><div class=\"photo-slider-item-15V4q photo-slider-keepImageRatio-1bSLF\"><img class=\"photo-slider-image-1fpZZ\" itemProp=\"image\" alt=\"Виниловый проигрыватель stantion t 52\" src=\"https://75.img.avito.st/image/1/JuQxHraFig1Hu3gLcXYpu8u9gAuTvYoL9N6OC0e7eAuHuYYJh7meDYe_ikk\" srcSet=\"\"/></div></li></ul></div></div></a></div><div class=\"iva-item-body-NPl6W\"><div class=\"iva-item-titleStep-2bjuh\"><a href=\"/sankt-peterburg/audio_i_video/vinilovyy_proigryvatel_stantion_t_52_2065691171\" target=\"_blank\" rel=\"noopener\" title=\"Виниловый проигрыватель stantion t 52 в Санкт-Петербурге\" itemProp=\"url\" data-marker=\"item-title\" class=\"link-link-39EVK link-design-default-2sPEv title-root-395AQ iva-item-title-1Rmmj title-list-1IIB_ title-root_maxHeight-3obWc\"><h3 itemProp=\"name\" class=\"title-root-395AQ iva-item-title-1Rmmj title-list-1IIB_ title-root_maxHeight-3obWc text-text-1PdBw text-size-s-1PUdo text-bold-3R9dt\">Виниловый проигрыватель stantion t 52</h3></a></div><div class=\"iva-item-priceStep-2qRpg\"><span class=\"price-root-1n2wM price-list-14p4v\"><span class=\"price-price-32bra\" data-marker=\"item-price\" itemProp=\"offers\" itemscope=\"\" itemType=\"http://schema.org/Offer\"><meta itemProp=\"priceCurrency\" content=\"RUB\"/><meta itemProp=\"price\" content=\"10000\"/><meta itemProp=\"availability\" content=\"https://schema.org/LimitedAvailability\"/><span class=\"price-text-1HrJ_ text-text-1PdBw text-size-s-1PUdo\">10 000<!-- --> <span class=\"price-currency-LOpM3\">₽</span></span></span></span><div class=\"delivery-icon-root-1WkFb\"><svg width=\"20\" height=\"18\" xmlns=\"http://www.w3.org/2000/svg\" class=\"delivery-icon-icon-1VqPQ\"><g fill=\"currentColor\" fill-rule=\"evenodd\"><path d=\"M17.462 13c.025.132.038.267.038.406 0 1.182-.933 2.14-2.083 2.14-1.151 0-2.084-.958-2.084-2.14 0-.139.013-.274.038-.406H6.63c.024.132.037.267.037.406 0 1.182-.933 2.14-2.084 2.14-1.15 0-2.083-.958-2.083-2.14 0-.139.013-.274.038-.406H.917A.917.917 0 0 1 0 12.083V1.917C0 1.41.41 1 .917 1h10.166c.507 0 .917.41.917.917V10h1V5.917c0-.507.41-.917.917-.917h1.734c.232 0 .454.087.624.245l3.432 3.183A.917.917 0 0 1 20 9.1v2.983c0 .507-.41.917-.917.917h-1.62z\"></path></g></svg></div></div><div><div class=\"geo-root-1pUZ8 iva-item-geo-1Ocpg\"><div class=\"geo-georeferences-3or5Q text-text-1PdBw text-size-s-1PUdo\"><span class=\"geo-icons-agBYC\"><i class=\"geo-icon-34rTK\" style=\"background-color:#0078C9\"></i></span><span>Парнас</span><span>, <!-- --> 1,5 км</span></div></div></div><div class=\"iva-item-dateStep-pZ3hT\"><div class=\"date-root-3w7Ry\"><span class=\"tooltip-tooltip-box-2rApK\"><span class=\"tooltip-target-wrapper-XcPdv\"><div data-marker=\"item-date\" class=\"date-text-2jSvU text-text-1PdBw text-size-s-1PUdo text-color-noaccent-bzEdI\">20 часов назад</div></span></span></div></div></div><div class=\"iva-item-aside-2Wxs_\"><div class=\"iva-item-actions-3fZ0Y\"><div data-marker=\"messenger-button\"><div class=\"messenger-button-root-3gcie messenger-button-root_fullwidth-1Qoze messenger-button-root_header-2wd2a\"><a width=\"12\" href=\"#login?next=%3Fwritein%3D2065691171&amp;authsrc=w\" target=\"_self\" data-marker=\"messenger-button/link\" class=\"button-button-2Fo5k button-size-s-3-rn6 button-default-mSfac width-width-12-2VZLz\"><div class=\"\">Написать</div></a></div></div><div class=\"iva-item-phone-jrUuP\" data-marker=\"item-contact\"><button class=\"iva-item-phoneButton-d5h1- button-button-2Fo5k button-size-s-3-rn6 button-default-mSfac\" aria-busy=\"false\"><span class=\"button-textBox-Row9K\">Показать телефон</span></button></div></div><div class=\"iva-item-vasStep-tChez\"></div></div></div><div class=\"iva-item-content-m2FiN\"></div></div>";
        correctAdvElement = Jsoup.parse(advHtml, "", org.jsoup.parser.Parser.xmlParser());
        incorrectAdvElement = Jsoup.parse("", "", org.jsoup.parser.Parser.xmlParser());
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
    @DisplayName("getPages()_correctPathTwoPage_Set<String>withTwoPagePath")
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
    @DisplayName("getPages()_correctPathOnePage_Set<String>withOnePagePath")
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
    @DisplayName("parsePage()_correctPathToFileWithTwoAdverts_Set<Advert>")
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
    @DisplayName("parsePage()_correctPathToEmptyFile_IOException")
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
    @DisplayName("parsePage()_withoutIdOrHref_emptySet<Advert>")
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
    @DisplayName("parsePage()_sameId's_Set<Advert>widthFirstAdv")
    public void parsePage_sameId() {
        try {
            Method method = Parser.class.getDeclaredMethod("parsePage", String.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, AdvertsWithSameId);
            assertEquals(obj, new HashSet<Advert>() {{ add(expectedAdvSet.stream().findFirst().orElseThrow()); }});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchElementException e) {
            fail();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("getAdvertPath()_correctAdvElement_hrefString")
    public void getAdvertPath_correctAdvElement() {
        String advPath = "http://www.avito.ru/sankt-peterburg/audio_i_video/vinilovyy_proigryvatel_stantion_t_52_2065691171";
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertPath", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advPath);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertPath()_incorrectAdvElement_IllegalArgumentException")
    public void getAdvertPath_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertPath", Element.class);
            method.setAccessible(true);
            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    method.invoke(parser, incorrectAdvElement);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertId()_correctAdvElement_idInteger")
    public void getAdvertId_correctAdvElement() {
        int advId = 2065691171;
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertId", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advId);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertId()_incorrectAdvElement_IllegalArgumentException")
    public void getAdvertId_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertId", Element.class);
            method.setAccessible(true);
            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    method.invoke(parser, incorrectAdvElement);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            });
        } catch (NoSuchMethodException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertTitle()_correctAdvElement_titleString")
    public void getAdvertTitle_correctAdvElement() {
        String advTitle = "Виниловый проигрыватель stantion t 52";
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertTitle", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advTitle);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertTitle()_incorrectAdvElement_\"noTitle\"String")
    public void getAdvertTitle_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertTitle", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, incorrectAdvElement);
            assertEquals(obj, "noTitle");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertSubway()_correctAdvElement_subwayString")
    public void getAdvertSubway_correctAdvElement() {
        String advSubway = "Парнас";
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertSubway", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advSubway);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertSubway()_incorrectAdvElement_\"noSubway\"String")
    public void getAdvertSubway_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertSubway", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, incorrectAdvElement);
            assertEquals(obj, "noSubway");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertDistance()_correctAdvElement_distanceInteger")
    public void getAdvertDistance_correctAdvElement() {
        int advIDistance = 1500;
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertDistance", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advIDistance);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertDistance()_incorrectAdvElement_-1")
    public void getAdvertDistance_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertDistance", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, incorrectAdvElement);
            assertEquals(obj, -1);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertPrice()_correctAdvElement_priceInteger")
    public void getAdvertPrice_correctAdvElement() {
        int advPrice = 10000;
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertPrice", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, correctAdvElement);
            assertEquals(obj, advPrice);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("getAdvertPrice()_incorrectAdvElement_-1")
    public void getAdvertPrice_incorrectAdvElement() {
        try {
            Method method = Parser.class.getDeclaredMethod("getAdvertPrice", Element.class);
            method.setAccessible(true);
            Object obj = method.invoke(parser, incorrectAdvElement);
            assertEquals(obj, -1);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail();
        }
    }
}
