import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class Fish extends Actions{
    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;

    public Fish(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public static Fish createFish(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Fish(id, position, images, actionPeriod, 0);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity crab = Crab.createCrab(this.getID() + CRAB_ID_SUFFIX,
                pos, this.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                Functions.getImageList(imageStore, CRAB_KEY));
        world.addEntity(crab);
        ((Crab)crab).scheduleActions(scheduler, world, imageStore);
    }
}
