public class Node
{

    private int g; //distance of current node
    private int h; //heuristic distance
    private int f; // g + h = f (total distance)
    private Node parent;
    private Point pos;

    public Node(int g, int h, int f, Point pos, Node parent)
    {
        this.g = g;
        this.h = h;
        this.f = f;
        this.parent = parent;
        this.pos = pos;
    }

    public int getF(){return this.f;}
    public int getH(){return this.h;}
    public int getG(){return this.g;}
    public Point getPos(){return pos;}
    public Node getParent(){return parent;}

//    public void setG(int g){this.g = g;}
//    public void setH(int h){this.h = h;}
//    public void setPostion(Point p){pos = p;}
}
