import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class SGrass extends Animated{

    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    private static final String SGRASS_KEY = "seaGrass";
    private static final int SGRASS_NUM_PROPERTIES = 5;
    private static final int SGRASS_ID = 1;
    private static final int SGRASS_COL = 2;
    private static final int SGRASS_ROW = 3;
    private static final int SGRASS_ACTION_PERIOD = 4;

    public SGrass(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public static SGrass createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new SGrass(id, position, images, 4, 0,
                actionPeriod, 0);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Entity fish = Fish.createFish(FISH_ID_PREFIX + this.getID(),
                    openPt.get(), FISH_CORRUPT_MIN +
                            Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    Functions.getImageList(imageStore, FISH_KEY));
            world.addEntity(fish);
            ((Fish)fish).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

//    public void spawn(Point location, WorldModel world, EventScheduler scheduler, ImageStore imageStore)
//    {
//        SGrass newGrass = createSgrass(SGRASS_KEY, location, SGRASS_ACTION_PERIOD,
//                Functions.getImageList(imageStore,"seaGrass"));
//
//        world.addEntity(newGrass);
//        newGrass.scheduleActions(scheduler, world, imageStore);
//    }
}
