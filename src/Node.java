public class Node {
    private int g;
    private int h;
    private int f;
    private Point pos;
    private Node prev;

    public Node(int g, int h, int f, Point pos, Node prev){
        this.g = g;
        this.h = h;
        this.f = f;
        this.pos = pos;
        this.prev = prev;
    }

    public int getG(){return this.g;}
    public int getH(){return this.h;}
    public int getF(){return this.f;}
    public Point getPos(){return pos;}
    public Node getPrev(){return prev;}
}
