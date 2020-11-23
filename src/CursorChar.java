import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class CursorChar extends Actions
{
    private static final int CURSOR_ANIMATION_REPEAT_COUNT = 7;

    public CursorChar(String id, Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public static CursorChar createCursorChar(String id, Point position,
                                          List<PImage> images)
    {
        return new CursorChar(id, position, images, 0, 0);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Animations.createAnimationAction(this, CURSOR_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

}
