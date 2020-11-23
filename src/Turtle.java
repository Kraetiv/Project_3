import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class Turtle extends Moving{
    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";

    private static final String TURTLE_KEY = "turtle";
    private static final int TURTLE_NUM_PROPERTIES = 5;
    private static final int TURTLE_ID = 1;
    private static final int TURTLE_COL = 2;
    private static final int TURTLE_ROW = 3;
    private static final int TURTLE_LIMIT = 4;
    private static final int TURTLE_ACTION_PERIOD = 1;
    private static final int TURTLE_ANIMATION_PERIOD = 1;


    public Turtle(String id, Point position, List<PImage> images,
                   int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static Turtle createTurtle(String id, Point position,
                                  int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Turtle(id, position, images, actionPeriod, animationPeriod);
    }

    Point nextPositionTurtle(WorldModel world, Point destPos)
    {

        AStarPathingStrategy astar = new AStarPathingStrategy();

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

    public boolean moveToTurtle(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = this.nextPositionTurtle(world, target.getPosition());

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
        //find nearest Sgrass
        Optional<Entity> TurtleTarget = world.findNearest(this.getPosition(), SGrass.class); //can change this to crab
        long nextPeriod = this.getActionPeriod();

        if (TurtleTarget.isPresent())
        {
            Point tgtPos = TurtleTarget.get().getPosition();

            if (moveToTurtle(world, TurtleTarget.get(), scheduler))
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

    public void spawn(Point location, WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        Turtle turtle = createTurtle(TURTLE_KEY, location, TURTLE_ACTION_PERIOD, TURTLE_ANIMATION_PERIOD,
                Functions.getImageList(imageStore,"turtle"));

//        Optional<Entity> entity = world.getOccupant(location);

        world.addEntity(turtle);
        turtle.scheduleActions(scheduler, world, imageStore);
    }
}

