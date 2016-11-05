import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaySh on 04.11.16.
 */
public class Scene {

    private String id;
    private String title;

    public Scene(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public String getId() {
        return id;
    }

}
