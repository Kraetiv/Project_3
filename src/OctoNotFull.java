import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class OctoNotFull extends Octo{

    private static final String SHARK_KEY = "shark";
    private static final int SHARK_NUM_PROPERTIES = 7;
    private static final int SHARK_ID = 1;
    private static final int SHARK_COL = 2;
    private static final int SHARK_ROW = 3;
    private static final int SHARK_LIMIT = 4;
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
        if(world.getBackgroundCell(this.getPosition()).equals(cave)){
            //System.out.println("hi");
            //transformShark(world, scheduler, imageStore);
            setImages(Functions.getImageList(imageStore, "shark"));
            this.actionPeriod = this.actionPeriod / 10;
            this.resourceLimit = 10000;
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

    public void transformShark(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        //Point pos = this.getPosition();
        //Entity shark = new Shark(SHARK_KEY, pos,
                //Functions.getImageList(imageStore, "shark") , 1000, resourceCount, actionPeriod / 5, 0);
        OctoNotFull octo = OctoNotFull.createOctoNotFull(this.getID(),
                1000, this.getPosition(), this.getActionPeriod() / 5, this.getAnimationPeriod(),
                Functions.getImageList(imageStore, "shark"));
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

    public void setImages(List<PImage> image){
        this.images = image;
    }

}
