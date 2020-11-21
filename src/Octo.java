import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import processing.core.PImage;

abstract public class Octo extends Moving{
    protected int resourceLimit;
    protected int resourceCount;

    public Octo(String id, Point position, List<PImage> images,
                int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    Point nextPositionOcto(WorldModel world, Point destPos)
    {

        SingleStepPathingStrategy single = new SingleStepPathingStrategy();

//        AStarPathingStrategy astar = new AStarPathingStrategy();

        BiPredicate<Point, Point> reach = Point::adjacent;


        Predicate<Point> passThrough = (point) -> {

            Optional<Entity> occupant = world.getOccupant(point);

            return !(world.isOccupied(point) || occupant.isPresent() && (occupant.get().getClass() == Fish.class));
        };

        List<Point> posList = single.computePath(getPosition(), destPos, passThrough, reach, PathingStrategy.CARDINAL_NEIGHBORS);
        //List<Point> posList = astar.computePath(getPosition(), destPos, passThrough, reach, getNeighbors);

        if(posList.size() == 0)
        {
            return getPosition();
        }
        return posList.get(0);
    }



//        int horiz = Integer.signum(destPos.x - position.x);
//        Point newPos = new Point(position.x + horiz, position.y);
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(occupant.get() instanceof OreEntity)))
//        {
//            int vert = Integer.signum(destPos.y - position.y);
//            newPos = new Point(position.x, position.y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(occupant.get() instanceof OreEntity)))
//            {
//                newPos = position;
//            }
//        }
//
//        return newPos;

    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public int getResourceLimit(){ return resourceLimit;}
    public int getResourceCount(){ return resourceCount;}
}
