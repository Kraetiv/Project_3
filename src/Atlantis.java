import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;


public class Atlantis extends Actions{
    private static final int ATLANTIS_ANIMATION_PERIOD = 70;
    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    public Atlantis(String id, Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public static Atlantis createAtlantis(String id, Point position,
                                        List<PImage> images)
    {
        return new Atlantis(id, position, images, 0, 0);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Animations.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
