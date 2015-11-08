package project1;

public class Set {
	private int x,y,z;
	private boolean two;
	private boolean obstacles;
	private int[][] obsta;

	public Set(int x, int y, int z, boolean obstacle, int obsta[][]){
		this.x = x;
		this.y = y;
		this.z = z;
		this.two = false;
		this.obstacles = obstacle;
		this.obsta = obsta;
	}

	public Set(int x, int y, boolean obstacles, int obsta[][]){
		this.x = x;
		this.y = y;
		this.two = true;
		this.obstacles = obstacles;
		this.obsta = obsta;
	}
	
	public int[][] getObstacles(){
		return obsta;
	}

	public boolean isTwoD() {
		return two;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {

		return this.y;
	}
	public int getZ() {

		return this.z;
	}
	
	public boolean thereIsObstacle(){
		return this.obstacles;
	}
}
