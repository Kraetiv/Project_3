import java.util.List;
import java.util.Optional;
import java.util.Random;

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
