import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class CursorChar extends Moving
{
    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";

    public CursorChar(String id, Point position, List<PImage> images,
                  int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static CursorChar createCursorChar(String id, Point position,
                                      int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new CursorChar(id, position, images, actionPeriod, animationPeriod);
    }
//
//    Point nextPositionTurtle(WorldModel world, Point destPos)
//    {
//
//        AStarPathingStrategy astar = new AStarPathingStrategy();
//
//        List<Point> strat = astar.computePath(this.getPosition(),
//                destPos, x -> !world.isOccupied(x) && world.withinBounds(x),
//                Point::adjacent,
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//
//        if(strat.size() == 0){
//            return this.getPosition();
//        }
//
//        return strat.get(0);
//    }
//
    public boolean moveToCursorChar(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

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
//
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        //find nearest Sgrass
        Optional<Entity> TurtleTarget = world.findNearest(this.getPosition(), SGrass.class); //can change this to crab
        long nextPeriod = this.getActionPeriod();

        if (TurtleTarget.isPresent())
        {
            Point tgtPos = TurtleTarget.get().getPosition();

            if (moveToCursorChar(world, TurtleTarget.get(), scheduler))
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
