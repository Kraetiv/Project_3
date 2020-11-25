import processing.core.PApplet;
import processing.core.PImage;

import java.security.Key;
import java.util.List;
import java.util.Optional;

public class CursorChar extends Moving
{

    private static final String QUAKE_KEY = "quake";

    private static final String CURSOR_KEY = "cursor";
    private static final int CURSOR_NUM_PROPERTIES = 5;
    private static final int CURSOR_ID = 1;
    private static final int CURSOR_COL = 2;
    private static final int CURSOR_ROW = 3;
    private static final int CURSOR_ACTION_PERIOD = 4;
    private static final int CURSOR_ANIMATION_PERIOD = 4;

    public CursorChar(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static CursorChar createCursor(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new CursorChar(id, position, images, actionPeriod, animationPeriod);
    }

    public boolean moveToCursor(WorldModel world, Entity target, EventScheduler scheduler)
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

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        // want it to spawn instead of moving
//        Optional<Entity> cursorTarget = world.findNearest(this.getPosition(), SGrass.class); //can change this to crab
        long nextPeriod = this.getActionPeriod();

        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                nextPeriod);
//        spawn(new Point(0,0), world, scheduler, imageStore);
    }

}
