package test.java;

import java.util.ArrayList;
import java.util.List;

public class PrepareResources {

    public String getLocalPageLink() {
        return "src/test/resources/107Adverts_3page.htm";
    }

    public List<String> getLocalFilePagesLinks() {
        return new ArrayList<>() {{
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?p=2&cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?p=3&cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
        }};
    }

}
