package store.catsocket.finnkinofinder;

public class Movie extends MovieTheatre {

    private String id;
    private String dttmShowStart;
    private String dttmShowEnd;
    private String EventID;
    private String Title;
    private String TheatreID;


    public Movie() {
    }

    public String getDttmShowStart(){

        return dttmShowStart;
    }

    public void setDttmShowStart(String ds){

        dttmShowStart = ds;

    }

    public String getDttmShowEnd(){

        return dttmShowEnd;
    }

    public void setDttmShowEnd(String de){

        dttmShowEnd = de;

    }

    public String getTitle(){

        return Title;
    }

    public void setTitle(String t){

        Title = t;

    }

    public String getTheatreID(){

        return TheatreID;
    }

    public void setTheatreID(String tid){

        TheatreID = tid;

    }

    public String getEventID(){

        return EventID;
    }

    public void setEventID(String eid){

        EventID = eid;

    }

    public String getId(){

        return id;
    }

    public void setId(String i){

        id = i;

    }
}
