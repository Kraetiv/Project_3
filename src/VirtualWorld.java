import java.awt.datatransfer.SystemFlavorMap;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import processing.core.*;
import java.util.Random;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 1280;
   private static final int VIEW_HEIGHT = 1080;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;
   static CursorChar cc;
   Random rand = new Random();

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      cc = CursorChar.createCursor("cursor", new Point(0,0), 0, 0,
              Functions.getImageList(imageStore,"cursor"));

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;

      //triggers mouse event
      registerMethod("mouseEvent", this);
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      this.view.drawViewport();
   }

   public void keyPressed()
   {
      int dx = 0;
      int dy = 0;

      if (key == CODED) {

         switch (keyCode) {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
//         world.viewport.shiftview look for that
      }
//         System.out.println("ijashdfiouahdsiofh");

         Point newPt = new Point(this.cc.getPosition().getX() + dx, this.cc.getPosition().getY() + dy);
         if(!(key == ' ') && !world.isOccupied(newPt))
         {
            world.removeEntity(cc);
            scheduler.unscheduleAllEvents(cc);
            cc = CursorChar.createCursor("cursor", newPt,
                    0, 0, Functions.getImageList(imageStore, "cursor"));
            world.addEntity(cc); //adds my submarine
            System.out.println("heheheheheh");
            //world.moveEntity(this.cc, newPt);
         }

         if(key == ' ')
         {
            Point loc = new Point(newPt.getX(), newPt.getY() + 1);
            Bacon newBacon = new Bacon("bacon", loc,
                    Functions.getImageList(imageStore,"seaGrass"),
                    10, 10, 0 ,0);

            System.out.println("Spawning now!");
//            world.addEntity(newBacon);

            if(!world.isOccupied(loc))
            {
               System.out.println("but is it really?");
               world.addEntity(newBacon);
               newBacon.scheduleActions(scheduler, world, imageStore);
            }
         }

         else{
            System.out.println("goes here now!");
            Point pt = new Point(cc.getPosition().getX(), cc.getPosition().getY());
//            if( cc.getPosition().getX() > 0
//                    && cc.getPosition().getX() < VIEW_WIDTH
//                    && cc.getPosition().getY() > 0
//                    && cc.getPosition().getY() < VIEW_HEIGHT
//            )

               if (key == UP) {
                  System.out.println("hahahahahah");
                  pt.setY(pt.getY() - 1);
               }
               if (key == DOWN) {
                  pt.setY(pt.getY() + 1);
               }
               if (key == LEFT) {
                  pt.setX(pt.getX() - 1);
               }
               if (key == RIGHT) {
                  pt.setX(pt.getX() + 1);
               }

         }
   }

   public void mouseEvent(processing.event.MouseEvent newEvent) // for our new entity, creates event
   {
      if(newEvent.getAction() == processing.event.MouseEvent.PRESS)
      {
         int x = newEvent.getX()/ TILE_WIDTH;
         int y = newEvent.getY()/ TILE_HEIGHT;

         int random_int = (int)(Math.random() * (3));

         Point pressed = view.getViewport().viewportToWorld(x, y);
         Turtle turtle = Turtle.createTurtle("turtle", new Point(mouseX, mouseY),
                 0, 0, Functions.getImageList(imageStore,"turtle") );

         SGrass newGrass = SGrass.createSgrass("Sgrass", new Point(mouseX, mouseY), 0,
                 Functions.getImageList(imageStore,"seaGrass"));

         turtle.spawn(pressed, world, scheduler, imageStore);
//         newGrass.spawn(new Point(x, y), world, scheduler, imageStore);

//         SGrass newGrass = SGrass.createSgrass("SGrass", new Point(mouseX, mouseY), );

         view.drawCave(x, y, imageStore);
      }

   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         Functions.getImageList(imageStore, DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         if (entity instanceof Actions)
            ((Actions)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
