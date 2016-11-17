package percy.campus.campus.posts;

/**
 * Created by percy on 2016/4/6.
 */
public class Posts {
    public String postName;
    public String location;
    public String price;
    public String type;
    public  String username;
    public String notes;
    public  String school;
    public Posts(String postName, String location, String price, String username, String notes, String school){
        this.postName = postName;
        this.location = location;
        this.price = price;
        this.notes = notes;
        this.school = school;
        this. username = username;
    }
    public Posts(String type, String location, String username, String notes, String school){
        this.type = type;
        this.location = location;
        this.notes = notes;
        this.school = school;
        this. username = username;
    }

}
