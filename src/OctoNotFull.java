import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class OctoNotFull extends Octo{

    private static final String SHARK_KEY = "shark";
    private static final int SHARK_ID = 1;
    private static final int SHARK_ACTION_PERIOD = 5;
    private static final int SHARK_ANIMATION_PERIOD = 6;

    public OctoNotFull(String id, Point position,
                       List<PImage> images, int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static OctoNotFull createOctoNotFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images)
    {
        return new OctoNotFull(id, position, images, resourceLimit,
                0, actionPeriod, animationPeriod);
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit())
        {
            Entity octo = OctoFull.createOctoFull(this.getID(), this.getResourceLimit(), this.getPosition(),
                    this.getActionPeriod(), this.getAnimationPeriod(), this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);

            ((OctoFull)octo).scheduleActions(scheduler, world, imageStore);
            return true;
        }
        return false;
    }

    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPositionOcto(world, target.getPosition());

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
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        Background cave = new Background("cave", Functions.getImageList(imageStore, "cave"));

        if(world.getBackgroundCell(this.getPosition()).equals(cave) && true){
            setImages(Functions.getImageList(imageStore, SHARK_KEY));
            this.actionPeriod = this.actionPeriod / 5;
        }

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activities.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public void setImages(List<PImage> image){
        this.images = image;
    }

}
