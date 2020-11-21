import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class Moving extends Actions{
    public Moving(String id, Point position, List<PImage> images,
                  int actionPeriod, int animationPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod);
    }

    public Point nextPosition(WorldModel world, Point p)
    {
        int horiz = Integer.signum(p.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
        {
            int vert = Integer.signum(p.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Fish.class)))
            {
                newPos = this.getPosition();
            }
        }
        return newPos;
    }
}
