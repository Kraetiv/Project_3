import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import processing.core.PImage;

public class Crab extends Moving{
    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";

    public Crab(String id, Point position, List<PImage> images,
                int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Crab createCrab(String id, Point position,
                                    int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Crab(id, position, images, actionPeriod, animationPeriod);
    }



    Point nextPositionCrab(WorldModel world, Point destPos)
    {

        AStarPathingStrategy astar = new AStarPathingStrategy();


//        Function<Point, Stream<Point>> getNeighbors = (start) -> {
//            List<Point> potentialNeighbors = new ArrayList<>();
//            potentialNeighbors.add(new Point(start.x + 1, start.y));
//            potentialNeighbors.add(new Point(start.x, start.y + 1));
//            potentialNeighbors.add(new Point(start.x - 1, start.y));
//            potentialNeighbors.add(new Point(start.x, start.y -1));
//            return (Stream<Point>) potentialNeighbors;
//        };

        List<Point> strat = astar.computePath(this.getPosition(),
                destPos, x -> !world.isOccupied(x) && world.withinBounds(x),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        if(strat.size() == 0){
            return this.getPosition();
        }

        return strat.get(0);
    }


    public boolean moveToCrab(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionCrab(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(this.getPosition(), SGrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(world, crabTarget.get(), scheduler))
            {
                Entity quake = Quake.createQuake(tgtPos,
                        Functions.getImageList(imageStore, QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                ((Quake)quake).scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
