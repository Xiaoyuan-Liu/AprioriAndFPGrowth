import java.util.ArrayList;

public class FPTreeNode {
    private int count;
    private String item;
    private FPTreeNode fatherNode;
    private ArrayList<FPTreeNode> childNode;
    FPTreeNode nextNode;
    FPTreeNode(){
        count =0;
        item=null;
        childNode = new ArrayList<FPTreeNode>();
    }
    FPTreeNode(int count, String item){
        this.count=count;
        this.item=item;
        childNode=new ArrayList<FPTreeNode>();
    }
    FPTreeNode(int count, String item,FPTreeNode fatherNode){
        this.count=count;
        this.item=item;
        this.fatherNode=fatherNode;
        childNode=new ArrayList<FPTreeNode>();
    }
    public void addNode(FPTreeNode child){
        childNode.add(child);
    }
    public FPTreeNode getNode(int index){
        return childNode.get(index);
    }
    public int incCount(int co){
        count +=co;
        return count;
    }
    public int hasChild(String str){
        for(int i = 0; i < childNode.size();i++) {
            if (childNode.get(i).item.equals(str)) {
                return i;
            }
        }
        return -1;
    }
    public int getCount(){
        return count;
    }
    public int getChildSize(){
        return childNode.size();
    }
    public String getItem(){
        return item;
    }
    public FPTreeNode getNextNode(){
        return nextNode;
    }
    public void setNextNode(FPTreeNode ne){
        nextNode=ne;
    }
    public FPTreeNode getFatherNode(){
        return fatherNode;
    }
}
