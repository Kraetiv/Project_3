import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computedPath(Point start, List<Point> path, Node result) {
        while(result.getPos() != start){
            path.add(result.getPos());
            result = result.getPrev();
            //System.out.println(path.size());
        }
        Collections.reverse(path);
        //System.out.println(path.size());
        return path;
    }

    public int heuristics(Point cur, Point end){return Point.distance(cur, end);}

    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> computed = new ArrayList<>();
        Queue<Node> open = new PriorityQueue<>(Comparator.comparingInt(Node::getF).thenComparing(Node :: getG));
        Node starting = new Node(0, heuristics(start, end), heuristics(start, end), start, null);
        Map<Point,Node> openedMap = new HashMap<Point, Node>();
        Map<Point,Node> closedMap = new HashMap<Point, Node>();
        open.add(starting);
        Node cur;

        while(open.size() != 0){
            cur = open.remove();
            if(withinReach.test(cur.getPos(), end)){ //checking end and current position
                return computedPath(start, computed, cur);
            }
            List<Point> neighbors = potentialNeighbors.apply(cur.getPos())
                    .filter(canPassThrough)
                    .filter(x -> !x.equals(start) && !x.equals(end))
                    .collect(Collectors.toList());

            for (Point neighbor : neighbors){
                if(!closedMap.containsKey(neighbor)){
                    int temp = cur.getG() + 1;
                    if(openedMap.containsKey(neighbor) && temp < openedMap.get(neighbor).getG()){
                        Node newNode = new Node(temp, heuristics(neighbor, end), heuristics(neighbor, end) + temp, neighbor, cur);
                        open.add(newNode);
                        open.remove(openedMap.get(neighbor));
                        openedMap.replace(neighbor, newNode);

                    }
                    else{
                        Node newNeighbor = new Node(cur.getG(), heuristics(neighbor, end),
                                cur.getF() + heuristics(neighbor,end), neighbor, cur);
                        open.add(newNeighbor);
                        openedMap.put(neighbor, newNeighbor);
                    }
                }
                closedMap.put(cur.getPos(), cur);
            }
        }
        return computed;
    }
}

