package project1;

public class Element {
	private int x_loc, y_loc, z_loc;
	private String reachability;
	private int id;
	
	public Element(int x, int y, int z, String reachability, int id){
		this.x_loc = x;
		this.y_loc = y;
		this.z_loc = z;
		this.reachability = reachability;
		this.id = id;
	}
	
	public Element(int x, int y, String reachability, int id){
		this.x_loc = x;
		this.y_loc = y;
		this.reachability = reachability;
		this.id = id;
	}
	
	public int getX_loc() {
		return x_loc;
	}
	public int getY_loc() {
		return y_loc;
	}
	public int getZ_loc() {
		return z_loc;
	}
	public int getPiece() {
		return id;
	}
	public String getReachability() {
		return this.reachability;
	}
}
