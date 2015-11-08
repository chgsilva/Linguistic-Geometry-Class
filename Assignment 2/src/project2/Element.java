package project2;


public class Element {
	private int x_loc, y_loc, z_loc;
	private String reachability;
	private int id;
	private int x_locFinal, y_locFinal, z_locFinal;
	private int len;
	
	public Element(int x, int y, int z, String reachability, int id, int xFinal, int yFinal, int zFinal, int len){
		this.x_loc = x;
		this.y_loc = y;
		this.z_loc = z;
		this.x_locFinal = xFinal;
		this.z_locFinal = yFinal;
		this.y_locFinal = zFinal;
		this.reachability = reachability;
		this.id = id;
		this.len = len;
	}
	
	public Element(int x, int y, String reachability, int id, int xFinal, int yFinal, int len){
		this.x_loc = x;
		this.y_loc = y;
		this.reachability = reachability;
		this.id = id;
		this.x_locFinal = xFinal;
		this.y_locFinal = yFinal;
		this.len = len;
	}
	
	public int getLen(){
		return this.len;
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
	public int getX_final() {
		return x_locFinal;
	}
	public int getY_final() {
		return y_locFinal;
	}
	public int getZ_final() {
		return z_loc;
	}
	
	public int getPiece() {
		return id;
	}
	public String getReachability() {
		return this.reachability;
	}
	
	public void changeCoord(){
		int varx = this.x_loc;
		int vary = this.y_loc;
		int varz = this.z_loc;
		
		this.x_loc = this.x_locFinal;
		this.y_loc = this.y_locFinal;
		this.z_loc = this.z_locFinal;
		
		this.x_locFinal = varx;
		this.y_locFinal = vary;
		this.z_locFinal = varz;	
	}

	public void remove() {
		this.x_loc--;
		this.y_loc--;
		this.x_locFinal--;
		this.y_locFinal--;
	}
}
