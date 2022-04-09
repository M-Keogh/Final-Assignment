import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class TernarySearch {

    private final static int MAX_NUM_OF_STOP_PARAMETERS = 10;

     public static void main(String[] args) {
         TernarySearch TST = new TernarySearch("stops.txt");
         printArrayList( TST.search("UNGLESS WAY FS GUILDFORD WAY WB") );
         printArrayList( TST.search("J") );
         printArrayList( TST.search("hi") );
         printArrayList( TST.search("marine WAY") );
         printArrayList( TST.search("ENG") );
    }

    public static void printArrayList(ArrayList<String> array) {
        for (String s : array) {
            System.out.println(s);
        }
    }

    TernarySearch(String stopFile) {
        root = null;
        File stops = new File(stopFile);
        try {
            Scanner fileScanner = new Scanner(stops);

            while (fileScanner.hasNextLine()) {
                String[] currentLine = fileScanner.nextLine().split(",");

                String[] allInfo = new String[MAX_NUM_OF_STOP_PARAMETERS];
                for (int i = 0; i < MAX_NUM_OF_STOP_PARAMETERS; i++) {
                    if(i >= currentLine.length)
                        break;
                    allInfo[i] = currentLine[i];
                }

                String stopName = allInfo[2];
                String prefix = stopName.substring(0, 2);
                if (prefix.equals("NB") || prefix.equals("WB") || prefix.equals("SB") || prefix.equals("EB")) {
                    stopName = stopName.substring(3).concat(" " + prefix);
                }

                StopInfo newInfo = new StopInfo(allInfo);

                add(stopName.toCharArray(), newInfo);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Node root;
    private boolean finishedAdd;

    //adds nodes to tree
    private void add(char[] stopName, StopInfo info) {

        if (stopName.length != 0) {
            if (root == null)
                root = new Node(stopName[0], -1);
            finishedAdd = false;
            add(stopName, 0, root, info);
        }
    }
    private Node add(char[] stopName, int i, Node node, StopInfo stop) {
        if (node == null)
            node = new Node(stopName[i], -1);

        if (Character.toLowerCase(node.key) < Character.toLowerCase(stopName[i])) {
            stopName[i] = Character.toUpperCase(stopName[i]);
            node.left = add(stopName, i, node.left, stop);
        }else if (Character.toLowerCase(node.key) > Character.toLowerCase(stopName[i])) {
            stopName[i] = Character.toUpperCase(stopName[i]);
            node.right = add(stopName, i, node.right, stop);
        }else if (i < stopName.length - 1)
            node.centre = add(stopName, ++i, node.centre, stop);

        if (i == stopName.length - 1 && !finishedAdd) {
            node.setValue(i);
            node.setStopInfo(stop);
            finishedAdd = true;
        }

        return node;
    }

    private boolean foundWord;
    public ArrayList<String> search(String input) {
        foundWord = false;
        ArrayList<String> hits = new ArrayList<String>();
        ArrayList<StopInfo> search_info_hits = new ArrayList<StopInfo>();
        Node start = search(input.toCharArray(), 0, root);

        if (foundWord) {
            System.out.println("Found an exact match for: " + input);
            hits.add(input);
        }
        if (start != null) {
            searchMatch(start.centre, hits, search_info_hits, "");
            System.out.println("Found " + hits.size() + " possible matches.");
            for (int i = (foundWord ? 1 : 0); i < hits.size(); i++) {
                hits.set(i, input + hits.get(i) + ", stop_code: " + search_info_hits.get(i).stop_code);
            }
        } else {
            System.out.println("No matches found.");
        }

        return hits;
    }
    private Node search(char[] stopName, int i, Node node) {
        if (node != null) {
            if (Character.toLowerCase(node.key) < Character.toLowerCase(stopName[i])) {
                stopName[i] = Character.toUpperCase(stopName[i]);
                node = search(stopName, i, node.left);
            } else if (Character.toLowerCase(node.key) > Character.toLowerCase(stopName[i])) {
                stopName[i] = Character.toUpperCase(stopName[i]);
                node = search(stopName, i, node.right);
            } else if (i < stopName.length - 1)
                node = search(stopName, ++i, node.centre);

            if (node != null && i == stopName.length - 1 && node.value != -1)
                foundWord = true;

            return node;
        }
        return null;
    }

    private void searchMatch(Node node, ArrayList<String> matches, ArrayList<StopInfo> infos, String prefix) {

        if (node != null) {
            searchMatch(node.left, matches, infos, prefix);
            searchMatch(node.centre, matches, infos, prefix + node.key);
            searchMatch(node.right, matches, infos, prefix);

            if (node.value != -1) {
                prefix += node.key;
                matches.add(prefix);
                infos.add(node.info);
            }
        }
    }


    private class Node {
        private final char key;
        private int value;
        private Node centre, left, right;
        private StopInfo info;

        public Node(char key, int value) {
            this.key = key;
            this.value = value;
            this.centre = null;
            this.left = null;
            this.right = null;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void setStopInfo(StopInfo info) {
            this.info = info;
        }
    }

    protected class StopInfo {

        protected String stop_id;
        protected String stop_code;
        protected String stop_name;
        protected String stop_desc;
        protected String stop_lat;
        protected String stop_lon;
        protected String zone_id;
        protected String stop_url;
        protected String location_type;
        protected String parent_station;

        protected StopInfo( String stop_id, String stop_code, String stop_name, String stop_desc,
                            String stop_lat, String stop_lon, String zone_id, String stop_url, String location_type,
                            String parent_station ) {

            this.stop_id = stop_id;
            this.stop_code = stop_code;
            this.stop_name = stop_name;
            this.stop_desc = stop_desc;
            this.stop_lat = stop_lat;
            this.stop_lon = stop_lon;
            this.zone_id = zone_id;
            this.stop_url = stop_url;
            this.location_type = location_type;
            this.parent_station = parent_station;

        }

        protected StopInfo( String[] s ) {

            this.stop_id = s[0];
            this.stop_code = s[1];
            this.stop_name = s[2];
            this.stop_desc = s[3];
            this.stop_lat = s[4];
            this.stop_lon = s[5];
            this.zone_id = s[6];
            this.stop_url = s[7];
            this.location_type = s[8];
            this.parent_station = s[9];

        }
    }
}
