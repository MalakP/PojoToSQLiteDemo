package digitalfish.test.pojotosqlitedemo.DataClasses;


/**
 * Created by Piotr Malak on 2015-07-09.
 */
public class Visit {
    private Long id;
    private String name;
    private String description;


    public Visit() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }
}