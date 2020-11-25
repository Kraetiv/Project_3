public class Activities extends Action{
    public Activities(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Activities createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new Activities(entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler){
        if(this.entity instanceof OctoFull) {

            ((OctoFull) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof OctoNotFull) {

            ((OctoNotFull) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Atlantis) {

            ((Atlantis) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Crab) {

            ((Crab) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Turtle){
            ((Turtle) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Quake) {

            ((Quake) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Fish) {

            ((Fish) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof SGrass) {

            ((SGrass) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Shark) //new add
        {
            ((Shark) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof CursorChar) //determines actions
        {
            ((CursorChar) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
        if(this.entity instanceof Bacon) //determines actions
        {
            ((Bacon) this.entity).execute(this.world,
                    this.imageStore, scheduler);
        }
    }

}
