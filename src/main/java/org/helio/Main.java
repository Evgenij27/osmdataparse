package org.helio;

import org.helio.document.OSMDocument;
import org.helio.document.relation.Relation;
import org.helio.document.way.Way;
import org.helio.parser.OSMStreamParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final String URL_PATH_FORMAT =
            "https://www.openstreetmap.org/api/0.6/map?bbox=%f,%f,%f,%f";


    public static void main(String[] args) throws Exception {

        if (args.length < 4) {
            System.out.printf("You mush provide arguments%nArguments order is: left_top_lat left_top_lon right_bottom_lat rigth_bottom_lon");
            System.exit(1);
        }

        double leftTopLat     = Double.parseDouble(args[0]);
        double leftTopLon     = Double.parseDouble(args[1]);
        double rightBottomLat = Double.parseDouble(args[2]);
        double rightBottomLon = Double.parseDouble(args[3]);

        checkLat(leftTopLat, rightBottomLat);
        checkLon(leftTopLon, rightBottomLon);

        URI uri = URI.create(String.format(Locale.US, URL_PATH_FORMAT, leftTopLat, leftTopLon, rightBottomLat, rightBottomLon));
        URL osmServer = uri.toURL();

        URLConnection conn = osmServer.openConnection();

        OSMDocument osmDoc;

        try (InputStream is = conn.getInputStream()) {
            osmDoc = parseOSMData(is);
        }

        System.out.println("##################### AREA OBJECTS ##########################################################");

        getAreaObjects(osmDoc).stream()
                .sorted(Comparator.comparing(Way::getArea).reversed())
                .forEach(Main::printArea);

        System.out.println("##################### END AREA OBJECTS ##########################################################");
        System.out.println("##################### WAY OBJECTS ##########################################################");


        getDistanceObjects(osmDoc).stream()
                .sorted(Comparator.comparing(Way::getDistance).reversed())
                .forEach(Main::printWay);

        System.out.println("##################### END WAY OBJECTS ##########################################################");
    }

    private static OSMDocument parseOSMData(InputStream in) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(in, "UTF-8");

        OSMStreamParser osmStreamParser = new OSMStreamParser(reader);

        return osmStreamParser.parseOSM();
    }

    private static void printWay(Way way) {
        System.out.print(String.format("%-10d %-50s %10.3f%n",way.getId(), way.getName(), way.getDistance()));
    }

    private static void printArea(Way way) {

        System.out.print(String.format("%-10d %-50s %10.3f%n",way.getId(), way.getName(), way.getArea()));
    }

    private static Set<Way> getAreaObjects(OSMDocument doc) {
        Set<Way> multipolygonWays = doc.getRelations().stream()
                .filter(r -> r.checkTag("type", "multipolygon"))
                .flatMap(Relation::getMergedMembers)
                .filter(Way::isClosed)
                .peek(Way::calculateArea)
                .collect(Collectors.toSet());

        Set<Way> closedWays = doc.getWays().stream()
                .filter(Way::isClosed)
                .filter(way -> !way.getTags().containsKey("junction"))
                .filter(way -> !way.getTags().containsKey("highway"))
                .peek(Way::calculateArea)
                .collect(Collectors.toSet());

        multipolygonWays.addAll(closedWays);
        return multipolygonWays;
    }

    private static Set<Way> getDistanceObjects(OSMDocument doc) {
        return doc.getWays().stream()
                .filter(way -> !way.isClosed())
                .peek(Way::calculateDistance)
                .collect(Collectors.toSet());
    }

    private static void checkLat(double leftTop, double rightBottom) {
        if (leftTop > 180d || leftTop < -180d || rightBottom > 180d || rightBottom < -180d) {
            System.out.println("Invalid latitude value");
            System.exit(1);
        }
    }
    private static void checkLon(double leftTop, double rightBottom) {
        if (leftTop > 90d || leftTop < -90d || rightBottom > 90d || rightBottom < -90d) {
            System.out.println("Invalid longtitude value");
            System.exit(1);
        }
    }

}

