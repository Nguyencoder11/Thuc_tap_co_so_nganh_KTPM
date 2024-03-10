import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BusRoutingSystem {
    private static final int BUS_SPEED = 20; // km/h
    private static final int BUS_CAPACITY = 60; // so luong khach tren xe buyt
    private static final int BUS_INTERVAL = 10; // cu moi BUS_INTERVAL thi se co 1 xe buyt di qua diem dung (phut) 

    public static void main(String[] args) {
        // Khoi tao danh sach cac diem dung
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop(1, "Stop 1", 0, 0));
        stops.add(new Stop(2, "Stop 2", 3, 4));
        stops.add(new Stop(3, "Stop 3", 6, 2));
        stops.add(new Stop(4, "Stop 4", 9, 5));
        stops.add(new Stop(5, "Stop 5", 12, 2));
        stops.add(new Stop(6, "Stop 6", 15, 3));
        stops.add(new Stop(7, "Stop 7", 18, 0));
        stops.add(new Stop(8, "Stop 8", 21, 4));
        stops.add(new Stop(9, "Stop 9", 24, 1));
        stops.add(new Stop(10, "Stop 10", 27, 3));

        // Toi uu hoa lo trinh xe buyt su dung thuat toan greedy
        List<Integer> route = optimizeBusRoute(stops, 3);
        System.out.println("Bus route: " + route);
    }

    // Su dung thuat toan greedy de toi uu hoa thoi gian cho xe buyt
    public static List<Integer> optimizeBusRoute(List<Stop> stops, int numBuses) {
        List<Integer> route = new ArrayList<>();
        int currentStop = 1;
        route.add(currentStop);

        while (route.size() < stops.size()) {
            int nextStop = -1;
            int minWaitingTime = Integer.MAX_VALUE;

            for (Stop stop : stops) { // Tim stop co thoi gian cho nho nhat trong nhung stop con lai

                // Neu day la diem dung hien tai, bo qua no
                if (stop.id == currentStop) {
                    continue;
                }

                // Neu diem dung nay da duoc them vao lo trinh, bo qua no
                if (route.contains(stop.id)) {
                    continue;
                }

                // Tinh thoi gian cho cua xe buyt tai diem dung nay
                int waitingTime = calculateWaitingTime(currentStop, stop, numBuses, stops);

                // Neu thoi gian cho nho hon thoi gian cho nho nhat hien tai, chon diem dung nay lam diem dung tiep theo
                if (waitingTime < minWaitingTime) {
                    nextStop = stop.id;
                    minWaitingTime = waitingTime;
                }
            }

            // Them diem dung tiep theo vao lo trinh
            route.add(nextStop);
            currentStop = nextStop;
        }

        return route;
    }

    // Ham tinh thoi gian cho cua xe buyt tai diem dung
    public static int calculateWaitingTime(int currentStop, Stop nextStop, int numBuses, List<Stop> stops) {

        // Tinh khoang cach giua cac diem dung
        double distance = Math.sqrt(Math.pow(nextStop.x - stops.get(currentStop - 1).x, 2) 
                + Math.pow(nextStop.y - stops.get(currentStop - 1).y, 2));

        // Tinh thoi gian di chuyen giua cac diem dung
        int travelTime = (int) Math.ceil(distance / BUS_SPEED * 60);  // doi ve phut

        // Tinh so luot di qua diem dung nay trong mot gio (60 phut)
        int numPassesPerHour = 60 / BUS_INTERVAL;

        // Tinh so luot khach da cho tren xe tai diem dung nay
        int numWaitingPassengers = nextStop.passengers - numPassesPerHour * travelTime / numBuses;

        //Tinh thoi gian cho tai diem dung nay
        int waitingTime = (int) Math.ceil((double) numWaitingPassengers / BUS_CAPACITY * numBuses * BUS_INTERVAL);

        return waitingTime;
    }
}

class Stop {
    public int id;
    public String name;
    public int x; // toa do x tren map
    public int y; // toa do y tren map
    public int passengers;

    public Stop(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.passengers = new Random().nextInt(100); 
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", passengers=" + passengers +
                '}';
    }
}
