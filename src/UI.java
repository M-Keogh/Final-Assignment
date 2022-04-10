import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    public static void main(String[] args) {

        System.out.println("Welcome to Vancouver Bus Information System. Please wait while the data is loading.");

        //make adjacency matrix
        ShortestPath cityRoutes = new ShortestPath("stop_times.txt", "transfers.txt");

        //make an arrayList for stop search
        StopTimesInfo theStops = new StopTimesInfo("stop_times.txt");

        //make ternary search tree for stop name search
        TernarySearch busSearch = new TernarySearch("stops.txt");

        Scanner input = new Scanner (System.in)	;

        boolean finished = false;
        while(!finished)
        {
            System.out.println("Please select an option using 1,2,3 or quit:");
            System.out.println("    -1: Shows the shortest path between 2 specified bus stops");
            System.out.println("    -2: Search for a bus stop");
            System.out.println("    -3: Shows all trips for a given arrival time");

            if  (input.hasNextLine()) {
                if (input.hasNext("quit")) {
                    System.out.println("Thank you for visiting Vancouver!");
                    finished = true ;
                } else if(input.hasNext("1")) {
                    input.nextLine();

                    boolean one = true ;
                    while(one) {
                        System.out.println("Enter the two stop numbers separated by a comma.(enter quit to exit or back to return to main)");
                        if (input.hasNextLine()) {
                            String userInput =input.nextLine();
                            String[] stops = userInput.split(",");

                            if (userInput.equalsIgnoreCase("quit")) {
                                System.out.println("Thank you for visiting Vancouver!");
                                finished = true;
                                one = false ;
                            } else if(userInput.equalsIgnoreCase("back")) {
                                one = false ;
                            } else if(stops.length != 2) {
                                System.out.println("Error enter in exactly two stop numbers");
                            } else if (stops.length == 2) {
                                try {
                                    int stop1 = Integer.parseInt(stops[0]);
                                    int stop2 = Integer.parseInt(stops[1]);

                                    System.out.println("Shortest distance from " + stop1 + " to " + stop2 + " is " +
                                            cityRoutes.shortestDistance(stop1, stop2));;
                                } catch(NumberFormatException e) {
                                    System.out.println("ERROR: enter integers only");
                                }
                            } else {
                                System.out.println("Error please enter two integers separated by a comma ");
                            }
                        }
                    }
                } else if(input.hasNext("2")) {
                    input.nextLine();
                    boolean two = true;

                    while(two) {
                        System.out.println("Search for a bus stop.(enter quit to exit or back to return to main)");
                        if (input.hasNextLine()) {
                            String userInput =input.nextLine();

                            if (userInput.equalsIgnoreCase("quit")) {
                                System.out.println("Thank you for visiting Vancouver!");
                                finished = true;
                                two = false ;
                            } else if(userInput.equalsIgnoreCase("back")) {
                                two = false ;
                            } else {
                                ArrayList<String> search = busSearch.search(userInput.toUpperCase());
                                for (String result : search) {
                                    System.out.println(result);
                                }
                            }
                        }
                    }
                } else if(input.hasNext("3")) {
                    input.nextLine();
                    boolean three = true;

                    while(three) {
                        System.out.println("Enter a time in the format of hh:mm:ss.(enter quit to exit or back to return to main)");
                        if (input.hasNextLine()) {
                            String userInput =input.nextLine();
                            String[] time = userInput.split(":");

                            if (userInput.equalsIgnoreCase("quit")) {
                                System.out.println("Thank you for visiting Vancouver!");
                                finished = true;
                                three = false ;
                            } else if(userInput.equalsIgnoreCase("back")) {
                                three = false ;
                            } else if(time.length != 3) {
                                System.out.println("Error enter in the hour, minute and second of the arrival time separated by :");
                            } else if (time.length == 3) {
                                try {
                                    int hour = Integer.parseInt(time[0]);
                                    int minute = Integer.parseInt(time[1]);
                                    int second = Integer.parseInt(time[2]);

                                    if(hour < 0 || hour > 23) {
                                        System.out.println("ERROR: Invalid hour. Please use 24 hour format");
                                    } else if(minute <0 || minute > 59) {
                                        System.out.println("ERROR: Invalid minutes. Please use a number between 00 and 59");
                                    } else if(second <0 || second > 59) {
                                        System.out.println("ERROR: Invalid seconds. Please use a number between 00 and 59");
                                    } else {
                                        List<StopTimesInfo.TripInfo> myStops= StopTimesInfo.getStopsInfo(userInput);
                                        if(myStops!=null && myStops.size()>0) {
                                            System.out.println("Number of trips found:" + myStops.size());

                                            int i=1;
                                            for(StopTimesInfo.TripInfo result:myStops)
                                            {
                                                System.out.println(i+".) "+"Trip ID:"+result.trip_id);
                                                System.out.printf("Departure Time:%s %nStop Id:%d %nStop Sequence:%d" +
                                                        "%nStop Headsign:%s %nPickup Type:%d %nDrop Off Type:%d %nShape " +
                                                        "Distance Traveled:%.3f%n", result.departure_time, result.stop_id,
                                                        result.stop_sequence, result.stop_headsign,result.pickup_type,
                                                        result.drop_off_type,result.shape_dist_traveled);
                                                System.out.println();
                                                i++;
                                            }
                                        }
                                        else {
                                            System.out.println("There are no trips at this time");
                                        }

                                    }
                                }
                                catch(NumberFormatException e) {
                                    System.out.println("ERROR: enter integers only");
                                }
                            } else {
                                System.out.println("ERROR: enter the exact arrival time separated by :");
                            }
                        }
                    }
                } else {
                    System.out.println("ERROR not a valid input please enter in 1 , 2 or 3 (or quit to exit)");
                    input.nextLine();
                }
            }
        }
        input.close();
    }
}
