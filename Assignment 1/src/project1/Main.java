package project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

//Student: Carlos Henrique Goncalves e Silva
//Student ID: 104668154

public class Main {

	private static int matrix[][][];
	private static Set x;
	private static Element p;

	public static void main(String[] args) {
		readFile();

		printDistances(x, p);
	}

	private static void readFile() {
		String[] tam;
		Scanner in;
		int  obsta[][];
		try {
			in = new Scanner( new File ("Data.txt"));
			String size = in.nextLine();
			tam = size.split(Pattern.quote("x"));

			String obstacles = in.nextLine();
			if(obstacles.equals("no")){				
				if(tam.length == 2){
					x = new Set(Integer.parseInt(tam[0]),Integer.parseInt(tam[1]), false, null);
				}else if(tam.length == 3){
					x = new Set(Integer.parseInt(tam[0]),Integer.parseInt(tam[1]), Integer.parseInt(tam[2]), false, null);
				}
			}else{
				if(tam.length == 2){
					String[] split = obstacles.split(Pattern.quote(" "));
					obsta = new int[split.length+1][2];
					String temp[] = new String[2];
					for (int i = 0; i < split.length; i++) {
						temp = split[i].split(Pattern.quote("x"));
						obsta[i][0]=Integer.parseInt(temp[0]);
						obsta[i][1]=Integer.parseInt(temp[1]);
					}
					x = new Set(Integer.parseInt(tam[0]),Integer.parseInt(tam[1]), true, obsta);
				}else if(tam.length == 3){
					String[] split = obstacles.split(Pattern.quote(" "));
					obsta = new int[split.length][3];
					String temp[] = new String[3];
					for (int i = 0; i < split.length; i++) {
						temp = split[i].split(Pattern.quote("x"));
						obsta[i][0]=Integer.parseInt(temp[0]);
						obsta[i][1]=Integer.parseInt(temp[1]);
						obsta[i][2]=Integer.parseInt(temp[2]);
					}
					x = new Set(Integer.parseInt(tam[0]),Integer.parseInt(tam[1]), Integer.parseInt(tam[2]), true, obsta);
				}
			}
			String location = in.nextLine();
			String[] temp = location.split(Pattern.quote("x"));
			int x_loc = Integer.parseInt(temp[0]);
			int y_loc = Integer.parseInt(temp[1]);
			String reach = in.nextLine();
			if(temp.length==3){
				int z_loc=Integer.parseInt(temp[2]);
				p = new Element(x_loc, y_loc, z_loc, reach, Integer.parseInt(reach));
			}else{
				p = new Element(x_loc, y_loc, reach, Integer.parseInt(reach));
			}
		} catch (FileNotFoundException e) {}

	}

	private static void printDistances(Set x, Element p) {
		if(x.isTwoD()){// is 2D
			if(x.thereIsObstacle()){//2D WITH OBSTACLES
				calculate2dObstacles(x, p);
			}else{//2D WITHOUT OBSTACLES -> 15x15 style
				calculate15x15(x, p);
			}
		}else{ // is 3D
			if(x.thereIsObstacle()){//3D WITH OBSTACLES
				Operations3d.p = p;
				Operations3d.x = x;
				
				Operations3d.calculate3dObstacles(x, p);
			}else{//3D WITHOUT OBSTACLES 
				Operations3d.p = p;
				Operations3d.x = x;
				
				Operations3d.calculate3d(x, p);
			}
		}
	}

	private static void calculate15x15(Set x, Element p) {
		matrix = new int [(x.getX()*2)][(x.getY()*2)][8]; //I will ignore the 0x and the x0 for convenience. So I need a bigger matrix 

		for (int i = 1; i < x.getX()*2; i++) {
			for (int j = 1; j < x.getY()*2; j++) {
				matrix[i][j][0] = calculateRook(x.getX(),x.getY(), i, j); 
				matrix[i][j][1] = calculateKing(x.getX(),x.getY(), i, j); 
				matrix[i][j][2] = calculateBishop(x.getX(),x.getY(), i, j);
				matrix[i][j][3] = calculateQueen(x.getX(),x.getY(), i, j);
				matrix[i][j][4] = calculateNight(x.getX(),x.getY(), i, j);
				matrix[i][j][5] = calculatePawn(x.getX(),x.getY(), i, j);
				//								matrix[i][j][6] = calculateEvaluate(x.getX(),x.getY(), i, j); //Calculate the distances for the rook
			}
		}

		System.out.println("All THE 15X15 TABLES");
		for (int piece = 0; piece < 6; piece++) {	
			moreThanOne(1, piece);
			System.out.println(piece);
			matrix[x.getX()][x.getY()][piece] = 0;
			printMatrix(piece);
		}
	}

	private static void calculate2dObstacles(Set x, Element p){
		matrix = new int[x.getX()+1][x.getY()+1][8];
		//putting obstacles
		int piece = 3;

		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			matrix [obs[i][0]][obs[i][1]][piece] = -1;
		}

		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				matrix[i][j][piece] = calculateObs(p.getX_loc(), p.getY_loc(), i, j, piece); 
			}
		}

		moreThanOne(1, piece);

		moreThanOneNeg(1, piece);
		matrix[p.getX_loc()][p.getY_loc()][piece] = 0;
		printMatrix(piece);


	}

	private static void calculateObstacles(int piece) {
		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			if(obs[i][0] == p.getX_loc() && (piece == 0 || piece == 3)){
				if(p.getY_loc()>obs[i][1]){
					for (int j = obs[i][1]; j >= 1; j--) {
						if(matrix[obs[i][0]][j][piece]!=-1)
							matrix[obs[i][0]][j][piece] = -2;
					}
				}else if(p.getY_loc()<obs[i][1]){
					for (int j = obs[i][1]; j <= x.getY(); j++) {
						if(matrix[obs[i][0]][j][piece]!=-1)
							matrix[obs[i][0]][j][piece] = -2;
					}
				}
			}else if(obs[i][1] == p.getY_loc() && (piece == 0 || piece == 3)){
				if(p.getX_loc()>obs[i][0]){
					for (int j = obs[i][0]; j > 0; j--) {
						if(matrix[obs[i][0]][j][piece]!=-1)
							matrix[j][obs[i][1]][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0]){
					for (int j = obs[i][0]; j <= x.getX(); j++) {
						if(matrix[obs[i][0]][j][piece]!=-1)
							matrix[j][obs[i][1]][piece] = -2;
					}
				}
			}else if(Math.abs(obs[i][0]-p.getX_loc()) - Math.abs(obs[i][1]-p.getY_loc()) == 0 && (piece == 2 || piece == 3)){
				if(p.getX_loc()>obs[i][0] && p.getY_loc()>obs[i][1]){
					for (int j = 1; obs[i][0]-j >0 && obs[i][1]-j >0 ; j++) {
						if(matrix[obs[i][0]][j][piece]!=-1)
							matrix[obs[i][0]-j][obs[i][1]-j][piece] = -2;
					}
				}else if(p.getX_loc()>obs[i][0] && p.getY_loc()<obs[i][1]){
					for (int j = 1; obs[i][0]+j >= 1 && obs[i][1]+j <= x.getY() ; j++) {
						if(matrix[obs[i][0]-j][obs[i][1]+j][piece]!=-1)
							matrix[obs[i][0]-j][obs[i][1]+j][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0] && p.getY_loc()>obs[i][1]){
					for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][1]+j >= 1 ; j++) {
						if(matrix[obs[i][0]+j][obs[i][1]-j][piece]!=-1)
							matrix[obs[i][0]+j][obs[i][1]-j][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0] && p.getY_loc()<obs[i][1]){
					for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][1]+j <= x.getY() ; j++) {
						if(matrix[obs[i][0]+j][obs[i][1]+j][piece]!=-1)
							matrix[obs[i][0]+j][obs[i][1]+j][piece] = -2;
					}

				}

			}
			matrix [obs[i][0]][obs[i][1]][piece] = -1;
		}

	}

	private static void moreThanOne(int pivot, int piece) {
		boolean modified = false;
		if(pivot<x.getX()){
			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if(matrix[i][j][piece]==pivot){
						calculateDD(i, j, matrix[i][j][piece], piece);
						modified = true;
					}
				}
			}
			if(modified){	
				for (int i = 1; i < matrix.length; i++) {
					for (int j = 1; j < matrix.length; j++) {
						if(matrix[i][j][piece]==0){
							moreThanOne(pivot+1, piece);
						}
					}
				}
			}
		}
	}

	private static void moreThanOneNeg(int pivot, int piece) {
		if(pivot<x.getX()){
			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if(matrix[i][j][piece]==pivot){
						calculateDDNeg(i, j, matrix[i][j][piece], piece);
					}
				}
			}

			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if((matrix[i][j][piece]) < (-1)){
						moreThanOneNeg(pivot+1, piece);
					}
				}
			}
		}
	}

	private static void calculateDD(int x1, int x2, int k, int piece) {

		int ret = 0, ll = 0, lp = 0;

		if(matrix.length == x.getX()+1 ){
			ll = x.getX()+1;
			lp = x.getY()+1;
		}else{
			ll = x.getX()*2;
			lp = x.getY()*2;
		}
		for (int i = 1; i < ll; i++) {
			for (int j = 1; j < lp; j++) {
				if(matrix[i][j][piece]==0){
					ret = callCorrect(x1, x2, i, j, piece);
					if(ret == 1){
						matrix[i][j][piece] = ret+k;
					}
				}
			}
		}
	}

	private static void calculateDDNeg(int x1, int x2, int k, int piece) {

		int ret = 0;
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				if(matrix[i][j][piece]==-2){
					ret = callCorrect(x1, x2, i, j, piece);
					if(ret == 1){
						matrix[i][j][piece] = ret+k;
					}
				}
			}
		}
	}

	private static void printMatrix(int piece) {
		int lp=0, ll=0;
		if(matrix.length == x.getX()+1 ){
			ll = x.getX();
			lp = x.getY();
		}else{
			ll = x.getX()*2;
			lp = x.getY()*2;
		}
		
		for (int i = 1; i < ll; i++) {
			for (int j = 1; j < lp; j++) {
				if(matrix[i][j][piece] == -1) {
					System.out.print("_ ");
				}else if(matrix[i][j][piece] == -2) {
					System.out.print("a ");
				}else{
					System.out.print(matrix[i][j][piece] + " ");
				}
			}
			System.out.println();
		}	
		System.out.println();
	}

//	private static void printMatrix(int matrix[][]) {
//		for (int i = 1; i < x.getX()+1; i++) {
//			for (int j = 1; j < x.getY()+1; j++) {
//				System.out.print(matrix[i][j] + " ");
//			}
//			System.out.println();
//		}	
//		System.out.println();
//	}

	private static int calculateKing(int x1, int x2, int y1, int y2) {

		if(Math.abs(x1-y1)<=1 && Math.abs(x2-y2)<=1){
			return 1;
		}
		return 0;
	}

	private static int calculateRook(int x1, int x2,int y1, int y2) { //ROOK
		if(x1==y1 || x2==y2){
			return 1;
		}
		return 0;
	}

	private static int calculateBishop(int x1, int x2,int y1, int y2){
		if((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0){
			return 1;
		}

		return 0;
	}

	private static int calculateQueen(int x1, int x2,int y1, int y2){
		if(((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0) || (x1==y1) || (x2==y2)){
			return 1;
		}


		return 0;
	}

	private static int calculateNight(int x1, int x2,int y1, int y2){
		if(((Math.abs(x1-y1) ==  1) && (Math.abs(x2-y2) == 2) ) || ((Math.abs(x1-y1) ==  2) && (Math.abs(x2-y2) == 1) )){
			return 1;
		}

		return 0;

	}
	private static int calculatePawn(int x1, int x2,int y1, int y2){
		if((Math.abs(x1-y1) == 1) && (Math.abs(x2-y2) == 0)){
			return 1;
		}

		return 0;
	}

	private static int calculateObs(int x1, int x2,int y1, int y2, int piece){
		if(matrix[y1][y2][piece]==-1){//obstacle
			calculateObstacles(piece);
			matrix[y1][y2][piece]=-1;
			//			System.out.println(matrix[y1][y2][piece]);
			return matrix[y1][y2][piece];	
		}else if((1==callCorrect(x1, x2, y1, y2, piece)) && (matrix[y1][y2][piece]!=-2) ){
			return 1;
		}else{
			return matrix[y1][y2][piece];	
		}
	}

	private static int callCorrect(int x1, int x2, int i, int j, int piece) {
		int ret = 0;
		switch (piece) {
		case 0://ROOK				
			ret = calculateRook(x1, x2, i, j);
			break;

		case 1://KING
			ret = calculateKing(x1, x2, i, j);
			break;

		case 2://BISHOP
			ret = calculateBishop(x1, x2, i, j);
			break;

		case 3://QUEEN
			ret = calculateQueen(x1, x2, i, j);
			break;

		case 4://KNIGHT
			ret = calculateNight(x1, x2, i, j);
			break;

		case 5://PAWN
			ret = calculatePawn(x1, x2, i, j);
			break;

		case 6://EVALUATE
			ret = calculateEvaluate(x1, x2, i, j);
			break;

		case 7://EVALUATE
			ret = 10000;
			break;
		}

		return ret;
	}

	private static int calculateEvaluate(int x1, int x2,int y1, int y2){
		try {

			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine se = sem.getEngineByName("JavaScript");

			String exp = p.getReachability();
			exp = exp.replace("x1", ""+x1);
			exp = exp.replace("x2", ""+x1);
			exp = exp.replace("y1", ""+y1);
			exp = exp.replace("y2", ""+y2);

			if((boolean) se.eval(exp)){
				return 1;
			}

		} catch (ScriptException e) {
			System.out.println("Invalid Expression");
		}

		return 0;
	}
}
