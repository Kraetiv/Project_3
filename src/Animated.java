import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

abstract public class Animated extends Actions{
    public Animated(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod)
    {
        super(id, position, images,0, 0, actionPeriod, animationPeriod);
    }

    public void scheduleActions (EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        //scheduler.scheduleEvent(this,
                //Animations.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
