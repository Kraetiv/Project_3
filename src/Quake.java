import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class Quake extends Actions {
    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position,
                 List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point position, List<PImage> images) {
        return new Quake(QUAKE_ID, position, images,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activities.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                Animations.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
