package store.catsocket.finnkinofinder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MovieTheatreList{

    ArrayList<MovieTheatre> movieTheatreList = new ArrayList<MovieTheatre>();
    ArrayList<Movie> movieList = new ArrayList<Movie>();

    private static MovieTheatreList mtl = new MovieTheatreList();

    public MovieTheatreList(){

    }

    public static MovieTheatreList getInstance() {

        return mtl;
    }



    public ArrayList<String> getAreaList() {

        ArrayList<String> areaList = new ArrayList<String>();


        for (MovieTheatre mt: movieTheatreList){

            areaList.add(mt.getArea());

        }
        return areaList;

    }

    public ArrayList<String> getTitleList() {

        ArrayList<String> titleList = new ArrayList<String>();


        for (Movie m: movieList){

            titleList.add(m.getTitle());

        }
        return titleList;

    }

    // Read xml for the list of Areas / theatres (Spinner)

    public void readXmlAreaID(){

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = db.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element "+ doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            for (int i = 0; i < ((NodeList) nList).getLength(); i++){
                Node node = ((NodeList) nList).item(i);
                System.out.println("Element is this: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    // Create Movie Theatres
                    MovieTheatre mt = new MovieTheatre();
                    mt.setArea(element.getElementsByTagName("Name").item(0).getTextContent());
                    mt.setId(element.getElementsByTagName("ID").item(0).getTextContent());

                    // Add Movie Theatres to the Movie Theatre List
                    movieTheatreList.add(mt);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("####### readXmlAreaID done! #######");

        }

    }

    // Read xml for the list of Events (Spinner)

    public void readXmlEvents(){
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Events/";
            Document doc = db.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element "+ doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");

            for (int i = 0; i < ((NodeList) nList).getLength(); i++){
                Node node = ((NodeList) nList).item(i);
                System.out.println("Element is this: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    Movie m = new Movie();
                    m.setId(element.getElementsByTagName("ID").item(0).getTextContent());
                    m.setTitle(element.getElementsByTagName("Title").item(0).getTextContent());
                    // Add Movies to the Movie List
                    movieList.add(m);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("####### readXmlAreaID done! #######");

        }
    }

    // Search a right list by filters

    public ArrayList<String> readXmlDateShow(String theatreName,String movieName, String d, int timeIn, int timeBetween){

        ArrayList<String> showList = new ArrayList<>();

        String show = "";
        String idd = "";
        String date = d;
        String name = movieName;
        int in = timeIn;
        int between = timeBetween;

        // Get the right theatre id for the right xml-url

        for (int i = 0; i < movieTheatreList.size();i++){
            if (movieTheatreList.get(i).getArea().equals(theatreName)){
                idd = movieTheatreList.get(i).getId();

                try {
                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                    // Right idd and date for the right url
                    String urlString = "http://www.finnkino.fi/xml/Schedule/?area="+idd+"+&dt="+date;
                    Document doc = db.parse(urlString);
                    doc.getDocumentElement().normalize();
                    System.out.println("Root element "+ doc.getDocumentElement().getNodeName());

                    NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

                    for (int id = 0; id < ((NodeList) nList).getLength(); id++){
                        Node node = ((NodeList) nList).item(id);
                        System.out.println("Element is this: " + node.getNodeName());

                        if (node.getNodeType() == Node.ELEMENT_NODE){
                            Element element = (Element) node;

                            // Get Show Start Time from XML, substring and convert to int.

                            String showStartString = element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11,19);
                            showStartString = showStartString.replaceAll("\\D+","");
                            int showStart = Integer.parseInt(showStartString);

                            // Get Movie Title from XML

                            String title = element.getElementsByTagName("Title").item(0).getTextContent();

                            // Filter time frame and get right shows

                            // If no specific title is selected
                            if (showStart >= in && showStart <= between && name.equals("")){
                                show = "Title: " +element.getElementsByTagName("Title").item(0).getTextContent()+"\n"+" Start time: "+
                                        element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11,19)+"\n"+" End time: "+
                                        element.getElementsByTagName("dttmShowEnd").item(0).getTextContent().substring(11,19)+"\n"+"Theatre: "+
                                        element.getElementsByTagName("Theatre").item(0).getTextContent()+"\n";
                                showList.add(show);
                            }

                            // If a specific title is selected
                            else if(showStart >= in && showStart <= between && title.equals(name)){

                                show = "Title: " +element.getElementsByTagName("Title").item(0).getTextContent()+"\n"+" Start time: "+
                                        element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11,19)+"\n"+" End time: "+
                                        element.getElementsByTagName("dttmShowEnd").item(0).getTextContent().substring(11,19)+"\n"+"Theatre: "+
                                        element.getElementsByTagName("Theatre").item(0).getTextContent()+"\n";
                                showList.add(show);
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("####### readXmlDateShow done! #######");
                }
            } else {
                System.out.println("Select theatre first!");
            }

        }
        return showList;
    }
}



