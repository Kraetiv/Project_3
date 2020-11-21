import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

abstract public class Octo extends Moving{
    protected int resourceLimit;
    protected int resourceCount;

    public Octo(String id, Point position, List<PImage> images,
                int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public int getResourceLimit(){ return resourceLimit;}
    public int getResourceCount(){ return resourceCount;}
}
