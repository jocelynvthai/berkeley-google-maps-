package bearmaps.proj2d;

import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.proj2d.TrieSet;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private HashMap<Point, Node> point_node;
    private List<Point> pointList;
    private HashMap<String, List<Node>> names_node;
    private TrieSet trieSet;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        point_node = new HashMap<>();
        pointList = new LinkedList<>();
        names_node = new HashMap<>();
        trieSet = new TrieSet();

        for (Node n : nodes) {

            // add all nodes to names_node, even if they have no neighbors
            if (n.name() != null) {
                String name = cleanString(n.name());
                trieSet.add(name);
                if (!names_node.containsKey(name)) {
                    names_node.put(name, new LinkedList<>());
                }
                names_node.get(name).add(n);
            }

            // only add to point_node and pointList if point has neighbors
            if (neighbors(n.id()).size() > 0) {
                Point point = new Point(n.lon(), n.lat());
                point_node.put(point, n);
                pointList.add(point);
            }
        }
    }

    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        WeirdPointSet wps = new WeirdPointSet(pointList);
        return point_node.get(wps.nearest(lon, lat)).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> names = new LinkedList<>();
        List<String> cleaned_names = trieSet.keysWithPrefix(cleanString(prefix));
        for (String s : cleaned_names) {
            if (names_node.get(s) != null) {
                for (Node n : names_node.get(s)) {
                    if (!names.contains(n.name())) {
                        names.add(n.name());
                    }
                }
            }
        }
        return names;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new LinkedList<>();
        String cleanedName = cleanString(locationName);
        if (names_node.containsKey(cleanedName)) {
            for (Node l : names_node.get(cleanedName)) {
                Map<String, Object> loc = new HashMap<>();
                loc.put("lat", l.lat());
                loc.put("lon", l.lon());
                loc.put("name", l.name());
                loc.put("id", l.id());
                locations.add(loc);
            }
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
