import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

/*
Functions - everything our virtual world is doing right now - is this a good design?
 */

final class Functions
{
   public static final Random rand = new Random();

   private static final String OCTO_KEY = "octo";
   private static final int OCTO_NUM_PROPERTIES = 7;
   private static final int OCTO_ID = 1;
   private static final int OCTO_COL = 2;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String FISH_KEY = "fish";
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_ID = 1;
   private static final int FISH_COL = 2;
   private static final int FISH_ROW = 3;
   private static final int FISH_ACTION_PERIOD = 4;

   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;
   private static final int ATLANTIS_ANIMATION_PERIOD = 70;
   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;

   private static final String BACON_KEY = "bacon";
   private static final int BACON_NUM_PROPERTIES  = 5;
   private static final int BACON_ID = 1;
   private static final int BACON_COL = 2;
   private static final int BACON_ROW = 3;
   private static final int BACON_ACTION_PERIOD = 3;

   private static final String SHARK_KEY = "shark";
   private static final int SHARK_NUM_PROPERTIES = 7;
   private static final int SHARK_ID = 1;
   private static final int SHARK_COL = 2;
   private static final int SHARK_ROW = 3;
   private static final int SHARK_LIMIT = 4;
   private static final int SHARK_ACTION_PERIOD = 5 / 2;
   private static final int SHARK_ANIMATION_PERIOD = 6;

   private static final String CURSOR_KEY = "cursor";
   private static final int CURSOR_NUM_PROPERTIES = 5;
   private static final int CURSOR_ID = 1;
   private static final int CURSOR_COL = 2;
   private static final int CURSOR_ROW = 3;
   private static final int CURSOR_ACTION_PERIOD = 4;
   private static final int CURSOR_ANIMATION_PERIOD = 4;

   private static final String TURTLE_KEY = "turtle";
   private static final int TURTLE_NUM_PROPERTIES = 5;
   private static final int TURTLE_ID = 1;
   private static final int TURTLE_COL = 2;
   private static final int TURTLE_ROW = 3;
   private static final int TURTLE_LIMIT = 4;
   private static final int TURTLE_ACTION_PERIOD = 1;
   private static final int TURTLE_ANIMATION_PERIOD = 1;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final int COLOR_MASK = 0xffffff;
   private static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   private static final int PROPERTY_KEY = 0;

//   private CursorChar cc = new CursorChar();

   public static void loadImages(Scanner in, ImageStore imageStore,
      PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(imageStore.images, in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
               lineNumber));
         }
         lineNumber++;
      }
   }

   public static void processImageLine(Map<String, List<PImage>> images,
      String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   public static List<PImage> getImages(Map<String, List<PImage>> images,
      String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   public static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
      ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
         case BGND_KEY:
            return parseBackground(properties, world, imageStore);
         case OCTO_KEY:
            return parseOcto(properties, world, imageStore);
         case OBSTACLE_KEY:
            return parseObstacle(properties, world, imageStore);
         case FISH_KEY:
            return parseFish(properties, world, imageStore);
         case ATLANTIS_KEY:
            return parseAtlantis(properties, world, imageStore);
         case SGRASS_KEY:
            return parseSgrass(properties, world, imageStore);
            case SHARK_KEY:
               return parseShark(properties, world, imageStore);
            case TURTLE_KEY:
               return parseTurtle(properties, world, imageStore);
            case CURSOR_KEY:
               return parseCursor(properties, world, imageStore);
            case BACON_KEY:
               return parseBacon(properties, world, imageStore);

         }
      }

      return false;
   }

   public static List<PImage> getImageList(ImageStore imageStore, String key)
   {
      return imageStore.images.getOrDefault(key, imageStore.defaultImages);
   }

   public static boolean parseCursor(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == CURSOR_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[CURSOR_COL]),
                 Integer.parseInt(properties[CURSOR_ROW]));

         CursorChar entity = CursorChar.createCursor((properties[CURSOR_ID]),
                 pt,
                 Integer.parseInt(properties[CURSOR_ACTION_PERIOD]),
                 Integer.parseInt(properties[CURSOR_ANIMATION_PERIOD]),
                 getImageList(imageStore, CURSOR_KEY));
         world.tryAddEntity(entity);
      }
      return properties.length == CURSOR_NUM_PROPERTIES;
   }

   public static boolean parseBackground(String [] properties,
      WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
            Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
            new Background(id, getImageList(imageStore, id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseOcto(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
            Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = OctoNotFull.createOctoNotFull(properties[OCTO_ID],
            Integer.parseInt(properties[OCTO_LIMIT]),
            pt,
            Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
            Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 getImageList(imageStore, OCTO_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   public static boolean parseShark(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == SHARK_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[SHARK_COL]),
                 Integer.parseInt(properties[SHARK_ROW]));
         Entity entity = OctoNotFull.createOctoNotFull(properties[SHARK_ID],
                 Integer.parseInt(properties[SHARK_LIMIT]),
                 pt,
                 Integer.parseInt(properties[SHARK_ACTION_PERIOD]),
                 Integer.parseInt(properties[SHARK_ANIMATION_PERIOD]),
                 getImageList(imageStore, SHARK_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SHARK_NUM_PROPERTIES;
   }

   public static boolean parseTurtle(String [] properties, WorldModel world, // update turtle
                                       ImageStore imageStore)
   {
      if (properties.length == TURTLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[TURTLE_COL]),
                 Integer.parseInt(properties[TURTLE_ROW]));
         Entity entity = Turtle.createTurtle(properties[TURTLE_ID],
                 pt,
                 Integer.parseInt(properties[TURTLE_ACTION_PERIOD]),
                 Integer.parseInt(properties[TURTLE_ANIMATION_PERIOD]),
                 getImageList(imageStore, TURTLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == TURTLE_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
            Integer.parseInt(properties[OBSTACLE_COL]),
            Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
            pt, getImageList(imageStore, OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseFish(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
            Integer.parseInt(properties[FISH_ROW]));
         Entity entity = Fish.createFish(properties[FISH_ID],
            pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 getImageList(imageStore, FISH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   public static boolean parseAtlantis(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
            Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = Atlantis.createAtlantis(properties[ATLANTIS_ID],
            pt, getImageList(imageStore, ATLANTIS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   public static boolean parseSgrass(String [] properties, WorldModel world,
      ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
            Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = SGrass.createSgrass(properties[SGRASS_ID],
            pt,
            Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 getImageList(imageStore, SGRASS_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   public static boolean parseBacon(String [] properties, WorldModel world,
                                     ImageStore imageStore)
   {
      if (properties.length == BACON_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BACON_COL]),
                 Integer.parseInt(properties[BACON_ROW]));
         Entity entity = SGrass.createSgrass(properties[BACON_ID],
                 pt,
                 Integer.parseInt(properties[BACON_ACTION_PERIOD]),
                 getImageList(imageStore, BACON_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == BACON_NUM_PROPERTIES;
   }

}
