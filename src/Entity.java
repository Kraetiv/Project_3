import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

abstract public class Entity{
   private String id;
   private Point position;
   public List<PImage> images;
   private int imageIndex;
   public int actionPeriod;
   public int animationPeriod;

   public Entity (String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public void nextImage(){
      this.imageIndex = (this.getImageIndex()+ 1) % this.getImages().size();
   }
   public String getID(){ return id;}
   int getImageIndex(){ return imageIndex;}
   int getAnimationPeriod(){ return animationPeriod;}
   public void setPosition(Point p) { this.position = p;}
   public List<PImage> getImages() { return this.images;}
   public Point getPosition() { return this.position;}
   public int getActionPeriod(){return actionPeriod;}
}