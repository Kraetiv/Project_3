import java.util.List;

import processing.core.PImage;

abstract public class Actions extends Entity{
    public Actions(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                Animations.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
