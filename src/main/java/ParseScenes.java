import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseScenes {

    public static final String FILENAME = "./web/sample.html";
    public static final String URL_START = "https://www.thinglink.com/api/user/";
    public static final String URL_END = "/scenes?pretty=true";
    public static final String THINGLINK = "https://www.thinglink.com/";

    public static void main(String[] args) {

        try {
            List<String> userIds = FileUtils.readLines(new File("./src/main/resources/user_ids.txt"), "utf-8");

            List<Scene> scenes = getScenes(userIds);
            Map<String, Word> words = getWords(scenes);
            writeToFile(FILENAME, words);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String fileName, Map<String, Word> words) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.append(generateHtml(words));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Scene> getScenes(List<String> userIds) {
        List<Scene> scenes = new ArrayList<>();

        try {

            for (String userId : userIds) {
                URL url = new URL(URL_START + userId + URL_END);

                JSONObject jsonObject = (JSONObject) new JSONTokener(IOUtils.toString(url.openStream(), "UTF-8")).nextValue();
                JSONArray results = (JSONArray) jsonObject.get("results");

                for (int i = 0; i < results.length(); i++) {

                    JSONObject object = results.getJSONObject(i);

                    String id = object.getString("id");
                    String title = object.getString("title");

                    scenes.add(new Scene(id, title));
                }
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return scenes;
    }

    /* 1) Words "Test" and "test" considered as the same word
    *  2) In result .html file frequency of words will be shown for lower case word - "test" */
    public static Map<String, Word> getWords(List<Scene> scenes) {
        Map<String, Word> words = new LinkedHashMap<>();

        for (Scene scene : scenes) {
            String title = scene.getTitle();

            List<String> allMatches = new ArrayList<>();
            Matcher m = Pattern.compile("\\b[a-zA-Z]+\\b", Pattern.CASE_INSENSITIVE)
                    .matcher(title);
            while (m.find()) {
                allMatches.add(m.group());
            }
            for (String match : allMatches) {
                Set<String> ids = new HashSet<>();
                Word word = words.get(match.toLowerCase());
                Integer count = null;
                if (word != null) {
                    count = word.getCount();
                    ids = word.getIds();
                }
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                ids.add(scene.getId());
                if (word != null) {
                    word.setCount(count);
                    word.setIds(ids);
                } else {
                    words.put(match.toLowerCase(), new Word(count, ids));
                }
            }
        }

        List<Map.Entry<String, Word>> entries = new ArrayList<>(words.entrySet());
        Collections.sort(entries, new WordComparator());
        Map<String, Word> sortedWordsMap = new LinkedHashMap<>();
        for (Map.Entry<String, Word> entry : entries) {
            sortedWordsMap.put(entry.getKey(), entry.getValue());
        }
        return sortedWordsMap;
    }

    public static StringBuilder generateHtml(Map<String, Word> words) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");

        html.append("\t<head>\n");
        html.append("\t\t<meta charset=\"utf-8\">\n");
        html.append("\t\t<link rel=\"stylesheet\" href=\"summary.css\">\n");
        html.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script>");
        html.append("<script type=\"text/javascript\" src=\"summary.js\"></script>");
        html.append("\t\t<title>Summary</title>\n");
        html.append("\t</head>\n");

        html.append("\t<body>\n");
        html.append("\t\t<h1 class=\"title\">Word frequency summary</h1>\n");
        html.append("\t\t<div class=\"container\">\n");

        for (Map.Entry entry : words.entrySet()) {
            Word word = (Word) entry.getValue();
            Set<String> ids = word.getIds();

            html.append("\t\t\t<div class=\"line\">\n");
            html.append("\t\t\t\t<p class=\"word\">").append(entry.getKey()).append("</p>\n");
            html.append("\t\t\t\t<p class=\"count\">").append(word.getCount()).append("</p>\n");

            for (String id : ids) {
                String link = THINGLINK + id;
                html.append("\t\t\t\t<div class=\"link\">\n");
                html.append("\t\t\t\t\t<a href=\"").append(link).append("\">").append(link).append("</a>\n");
                html.append("\t\t\t\t</div>\n");
            }
            html.append("\t\t\t</div>\n");
        }

        html.append("\t\t</div>\n");
        html.append("\t</body>\n");

        html.append("</html>");

        return html;
    }
}
