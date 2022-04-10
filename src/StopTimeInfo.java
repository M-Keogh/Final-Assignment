import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class StopTimesInfo {

    static List<TripInfo> TripInfos;

    StopTimesInfo(String stop_times) {
        File stopTimes;
        try {

            TripInfos = new ArrayList<>();
            double shape_dist_traveled;
            stopTimes = new File(stop_times);
            Scanner fileScanner = new Scanner(stopTimes);
            //skip first line
            fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String[] currentLine = fileScanner.nextLine().split("\\s+|,\\s*");

                int tripID = (!currentLine[0].equals("")) ? Integer.parseInt(currentLine[0]) : -1;
                Time arrivalTime = Time.valueOf(currentLine[1]);
                Time depatureTime = Time.valueOf(currentLine[2]);
                int stopID = (!currentLine[3].equals("")) ? Integer.parseInt(currentLine[3]) : -1;
                int sequence = (!currentLine[4].equals("")) ? Integer.parseInt(currentLine[4]) : -1;
                String headsign = (!currentLine[5].equals("")) ? currentLine[5] : null;
                int pickupType = (!currentLine[6].equals("")) ? Integer.parseInt(currentLine[6]) : -1;
                int dropOffType = (!currentLine[7].equals("")) ? Integer.parseInt(currentLine[7]) : -1;
                try {
                    shape_dist_traveled = (!currentLine[8].equals("")) ? Double.parseDouble(currentLine[8]) : -1;
                } catch (ArrayIndexOutOfBoundsException e1) {
                    shape_dist_traveled = -1;

                }

                TripInfos.add(new TripInfo(tripID, arrivalTime, depatureTime, stopID, sequence, headsign,
                        pickupType, dropOffType, shape_dist_traveled));
            }
            fileScanner.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    // Sort by trip id
    protected static Comparator<TripInfo> sortByTripId = Comparator.comparingInt(a -> a.trip_id);

    //returns all Stops with the specified arrival time sorted by by trip_id
    public static List<TripInfo> getStopsInfo(String arrivalTime) {
        List<TripInfo> stopsByArrival = new ArrayList<>();
        Time arriveT = Time.valueOf(arrivalTime);
        String[] testTime = arrivalTime.split(":");
        if (Integer.parseInt(testTime[0]) < 24 && Integer.parseInt(testTime[1]) < 60
                && Integer.parseInt(testTime[2]) < 60) {
            for (TripInfo tripInfo : TripInfos) {
                if ((tripInfo.arrival_time).equals(arriveT)) {
                    stopsByArrival.add(tripInfo);
                }
            }
            stopsByArrival.sort(StopTimesInfo.sortByTripId);

            return stopsByArrival;
        }
        return null;
    }

    public static void main(String[] args) {

        StopTimesInfo test = new StopTimesInfo("stop_times.txt");

        List<TripInfo> myStops = test.getStopsInfo("5:34:59");
        if(myStops!=null) {
            System.out.println(myStops.size());

            for(TripInfo s:myStops){
                System.out.println("stopheadsign:"+s.stop_headsign);
                System.out.printf("trip_id:%d,arrival_time:%s,departure_time:%s,stop_id:%d,stop_sequence:%d,"
                                + "stop_headsign:%s,pickup_type:%d,drop_off_type:%d,shape_dist_traveled:%f%n",
                        s.trip_id,s.arrival_time,s.departure_time,s.stop_id,s.stop_sequence,s.stop_headsign,s.pickup_type,
                        s.drop_off_type,s.shape_dist_traveled);
            }
        }
        else {
            System.out.println("error in string input");
        }
    }

    protected class TripInfo {
        protected int trip_id;
        protected Time arrival_time;
        protected Time departure_time;
        protected int stop_id;
        protected int stop_sequence;
        protected String stop_headsign;
        protected int pickup_type;
        protected int drop_off_type;
        protected double shape_dist_traveled;

        protected TripInfo(int trip_id, Time arrival_time, Time departure_time, int stop_id, int stop_sequence,
                           String stop_headsign, int pickup_type, int drop_off_type, double shape_dist_traveled) {
            this.trip_id = trip_id;
            this.arrival_time = arrival_time;
            this.departure_time = departure_time;
            this.stop_id = stop_id;
            this.stop_sequence = stop_sequence;
            this.stop_headsign = stop_headsign;
            this.pickup_type = pickup_type;
            this.drop_off_type = drop_off_type;
            this.shape_dist_traveled = shape_dist_traveled;
        }
    }
}
