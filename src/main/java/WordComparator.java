import java.util.Comparator;
import java.util.Map;

public class WordComparator implements Comparator<Map.Entry<String, Word>> {

    @Override
    public int compare(Map.Entry<String, Word> first, Map.Entry<String, Word> second) {
        return second.getValue().getCount() - first.getValue().getCount();
    }
}
