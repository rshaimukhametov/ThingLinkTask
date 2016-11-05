import java.util.HashSet;
import java.util.Set;

/**
 * Created by RaySh on 04.11.16.
 */
public class Word {

    private Integer count = 0;
    private Set<String> ids = new HashSet<String>();

    public Word(int count, Set<String> ids) {
        this.count = count;
        this.ids = ids;
    }

    public int getCount() {
        return count;
    }

    public Set<String> getIds() {
        return ids;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }
}
