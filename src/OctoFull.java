import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class OctoFull extends Octo{

    private static final String SHARK_KEY = "shark";
    private static final int SHARK_ID = 1;
    private static final int SHARK_ACTION_PERIOD = 5;
    private static final int SHARK_ANIMATION_PERIOD = 6;

    public OctoFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static OctoFull createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new OctoFull(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Atlantis.class);

        Background cave = new Background("cave", Functions.getImageList(imageStore, "cave"));

        if(world.getBackgroundCell(this.getPosition()).equals(cave)){
            this.transformShark(world, scheduler, imageStore);
        }

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((Actions)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activities.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        OctoNotFull octo = OctoNotFull.createOctoNotFull(this.getID(),
                this.getResourceLimit(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToFull(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition()))
        {
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

    public void transformShark(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Shark shark = new Shark(SHARK_KEY, getPosition(),
                Functions.getImageList(imageStore, "shark") , resourceLimit, resourceCount, actionPeriod / 5, animationPeriod);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(shark);
        shark.scheduleActions(scheduler, world, imageStore);
    }

    public void setImages(List<PImage> image){
        this.images = image;
    }

}
