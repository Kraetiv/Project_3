import java.util.List;
import java.util.Objects;

import processing.core.PImage;

final class Background {
   public String id;
   public List<PImage> images;
   public int imageIndex;

   public Background(String id, List<PImage> images) {
      this.id = id;
      this.images = images;
   }

   public static PImage getCurrentImage(Object entity) {
      if (entity instanceof Background) {
         return ((Background) entity).images
                 .get(((Background) entity).imageIndex);
      }
      else{
         return ((Entity) entity).getImages().get(((Entity) entity).getImageIndex());
      }
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Background that = (Background) o;

      return Objects.equals(id, that.id);
   }

}
