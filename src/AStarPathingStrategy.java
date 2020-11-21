import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Queue<Node> nxtNodeQ = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF)
                .thenComparing(Node::getG)); //Didn't use regular queue as it uses FIFO
        List<Point> path = new ArrayList<>();

        Map<Point, Node> openLst = new HashMap<>();
        Map<Point, Node> closedLst = new HashMap<>();

        Node startNode = new Node(0, heuristic(start, end), heuristic(start, end), start, null);
        nxtNodeQ.add(startNode);

        while (!nxtNodeQ.isEmpty())
        {
            Node curNode = nxtNodeQ.poll(); //retrieves current node from queue

            if (withinReach.test(curNode.getPos(), end))
            {
                return computedPath(curNode);
            }

            //filter the neighbors and end in list
            List<Point> neighbors = potentialNeighbors
                    .apply(curNode.getPos())
                    .filter(n -> !n.equals(start) && !n.equals(end)) //for testcase 4, had extra two pts, fix if not it
                    .filter(canPassThrough)
                    .collect(Collectors.toList());

            closedLst.put(curNode.getPos(), curNode);

            for (Point n: neighbors)
            {
                if (!closedLst.containsKey(n))
                {
                    if(openLst.containsKey(n))
                    {
                        if( (curNode.getG() + 1) > openLst.get(n).getF())
                        {
                            Node betterNode = new Node(
                                    (curNode.getG() + 1), heuristic(n, end),
                                    heuristic(n, end), n, curNode
                            );
                            nxtNodeQ.add(betterNode);
                            nxtNodeQ.remove(openLst.get(n));
                            openLst.replace(n, betterNode);
                        }
                    }
                    else {
                        Node neighborNode = new Node(curNode.getG(), heuristic(n, end),
                                curNode.getF() + heuristic(n, end), n, curNode);

                        nxtNodeQ.add(neighborNode);
                        openLst.put(n, neighborNode);
                    }
                }
            }
        }
        return path; //always return path array even if empty
    }



    public int heuristic(Point current, Point goal)
    {
        return Point.distance(current, goal); //basically euclidean/pythagorean distance
    }

    public List<Point> computedPath(Node n)
    {
        List<Point> result = new ArrayList<>();

        while (n.getParent() != null)
        {
            result.add(n.getPos());
            n = n.getParent();
//            System.out.println(result.size());
        }

        Collections.reverse(result);
//        System.out.println(result.size());
        return result;

    }
}
