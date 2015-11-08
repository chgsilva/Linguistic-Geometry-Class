package project2;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

//Student: Carlos Henrique Goncalves e Silva
//Student ID: 104668154

public class Main {

	private static int matrix[][][];
	private static boolean sum[][][], aux[][][];
	private static Set x;
	private static Element p, q;
	private static JFrame frame = new JFrame();
	private static JGraphModelAdapter m_jgAdapter;
	private static ListenableGraph graph;
	private static JGraph jgraph;

	public static void main(String[] args) {
		readFile();
		graph = new ListenableDirectedGraph( DefaultEdge.class );
		m_jgAdapter = new JGraphModelAdapter<>(graph);
		jgraph = new JGraph(m_jgAdapter);		
		jgraph.getGraphLayoutCache();

		aux = new boolean [x.getX()][x.getY()][p.getLen()+2];

		// create a visualization using JGraph, via the adapter
		
		if(x.isTwoD()){
			if(!x.thereIsObstacle())
				p.remove();
			q  = new Element(p.getX_final(), p.getY_final(), p.getReachability(), p.getPiece(), p.getX_loc(), p.getY_loc(), p.getLen());
		}else{
//			System.out.println("entre9");
			q  = new Element(p.getX_final(), p.getY_final(), p.getZ_final(),  p.getReachability(), p.getPiece(), p.getX_loc(), p.getY_loc(), p.getZ_loc(), p.getLen());
		}

		printDistances(x, p, matrix);	
			
		frame.getContentPane().add(new JScrollPane(jgraph));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(500, 500);
	}

	private static Ret[] calculateDistances(int k, int x_ult, int y_ult, int actual) {
		Ret []ret ;
		ret = new Ret[x.getX()*x.getY()];

		if(k > p.getLen()){
			ret[0] = new Ret("");
			return ret;

		}
		
		int xmax = x.getX(),ymax = x.getY();
		for (int x = 0; x < xmax ; x++) {
			for (int l = 0; l < ymax; l++) {
				if(aux[x][l][k]){
					actual ++;
					x_ult=x;
					y_ult=l;
					graph.addVertex(x+" "+l);
					if(x != 0 || l!=0)
						positionVertexAt(x+" "+l, x*150, l*150);
					
					Ret r[];
					if(k+1 > p.getLen()){
						ret = addRet(ret, new Ret(x_ult+" "+y_ult));
						return ret;
					}
					evaluateAux(k+1, x_ult, y_ult);
					//						printMatrix(k+1, aux);
					ret = addRet(ret, new Ret(x_ult+" "+y_ult));
					r = calculateDistances(k+1, x_ult, y_ult, actual);

					for (int i = 0; i<64; i++) {
						if(r[i]!=null){
							if(!r[i].getRet().equals("")){
								graph.addEdge(x+" "+l, r[i].getRet());

							}
						}else{
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	private static Ret[] addRet(Ret[] ret, Ret ret2) {
		for (int i = 0; i < x.getX()* x.getY(); i++) {
			if(ret[i] == null){
				ret[i] = ret2;
				break;
			}
		}
		return ret;
	}

	private static void evaluateAux(int k, int x_ult, int y_ult){
		int found = 0;

		for (int i = 0 ; i < x.getX(); i++) {
			for (int j = 0 ; j < x.getY(); j++) {
				int v1 = 8 - p.getX_loc() +i;
				int v2 = 8 - p.getY_loc() +j;	

				int o1 = 8 - x_ult + i;
				int o2 = 8 - y_ult + j;	

				if(( matrix[v1][v2][p.getPiece()] == k) && (matrix[o1][o2][q.getPiece()]  == 1) && sum[i][j][p.getPiece()] ){
					// map(n) && map(1) && SUM
					aux[i][j][k] = true;
					found ++;
				}else{
					aux[i][j][k] = false;
				}
			}
		}

		aux[p.getX_loc()][p.getY_loc()][0] = true;
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
			//inicial
			String location = in.nextLine();
			String[] temp = location.split(Pattern.quote("x"));
			int x_loc = Integer.parseInt(temp[0]);
			int y_loc = Integer.parseInt(temp[1]);
			
			//final
			String locationFinal = in.nextLine();
			String[] temp2 = locationFinal.split(Pattern.quote("x"));
			int x_locF = Integer.parseInt(temp2[0]);
			int y_locF = Integer.parseInt(temp2[1]);
			
			//lenght
			String len = in.nextLine();
			int lenght = Integer.parseInt(len);
			
			String reach = in.nextLine();
			if(temp.length==3){
				int z_loc=Integer.parseInt(temp[2]);
				int z_locF=Integer.parseInt(temp2[2]);
				p = new Element(x_loc, y_loc, z_loc, reach, Integer.parseInt(reach), x_locF, y_locF, z_locF, lenght);
			}else{
				p = new Element(x_loc, y_loc, reach, Integer.parseInt(reach), x_locF, y_locF, lenght);
			}
		} catch (FileNotFoundException e) {}
		
	}

	private static int[][][] printDistances(Set x, Element p, int matrix[][][]) {
		if(x.isTwoD()){// is 2D
			if(x.thereIsObstacle()){//2D WITH OBSTACLES
				OperationsObstacle.p = p;
				OperationsObstacle.x = x;
				OperationsObstacle.q = q;
				OperationsObstacle.aux = aux;
				OperationsObstacle.graph = graph;
				OperationsObstacle.m_jgAdapter = m_jgAdapter;
				graph = OperationsObstacle.Control();
								
				return null;
			}else{//2D WITHOUT OBSTACLES -> 15x15 style\
				Main.matrix = calculate15x15(x, p, matrix);
				format();//evaluate sum
				evaluateAux(0, p.getX_loc(), p.getY_loc());
				calculateDistances(0, p.getX_loc(), p.getY_loc(), 0);
				
				return null;
			}
		}else{ // is 3D
			if(x.thereIsObstacle()){//3D WITH OBSTACLES
				Operations3d.p = p;
				Operations3d.q = q;
				Operations3d.x = x;
				Operations3d.graph = graph;
				Operations3d.m_jgAdapter = m_jgAdapter;
				graph = Operations3d.Control();
				
				Operations3d.Control();
			}else{//3D WITHOUT OBSTACLES 
				Operations3d.p = p;
				Operations3d.q = q;
				Operations3d.x = x;
				Operations3d.graph = graph;
				Operations3d.m_jgAdapter = m_jgAdapter;
				graph = Operations3d.Control();
				
				Operations3d.Control();
				return null;
			}
		}
		//tirar isso depois
		return null;
	}

	private static int[][][] calculate15x15(Set x, Element p, int matrix[][][]) {
		matrix = new int [(x.getX()*2)][(x.getY()*2)][8]; //I will ignore the 0x and the x0 for convenience. So I need a bigger matrix 

		for (int i = 1; i < x.getX()*2; i++) {
			for (int j = 1; j < x.getY()*2; j++) {
				matrix[i][j][0] = calculateRook(x.getX(),x.getY(), i, j); 
				matrix[i][j][1] = calculateKing(x.getX(),x.getY(), i, j); 
				matrix[i][j][2] = calculateBishop(x.getX(),x.getY(), i, j);
				matrix[i][j][3] = calculateQueen(x.getX(),x.getY(), i, j);
				matrix[i][j][4] = calculateNight(x.getX(),x.getY(), i, j);
				//				matrix[i][j][5] = calculatePawn(x.ge/tX(),x.getY(), i, j);
			}
		}

		for (int piece = 0; piece < 6; piece++) {	
			moreThanOne(1, piece, matrix);
			matrix[x.getX()][x.getY()][piece] = 0;
		}
		return matrix;
	}

	private static int[][][] calculate2dObstacles(Set x, Element p, int[][][] matrix2){
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

		moreThanOne(1, piece, matrix);

		moreThanOneNeg(1, piece);
		matrix[p.getX_loc()][p.getY_loc()][piece] = 0;

		return matrix;
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

	private static void moreThanOne(int pivot, int piece, int[][][] matrix) {
		boolean modified = false;
		if(pivot<x.getX()){
			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if(matrix[i][j][piece]==pivot){
						calculateDD(i, j, matrix[i][j][piece], piece, matrix);
						modified = true;
					}
				}
			}
			if(modified){	
				for (int i = 1; i < matrix.length; i++) {
					for (int j = 1; j < matrix.length; j++) {
						if(matrix[i][j][piece]==0){
							moreThanOne(pivot+1, piece, matrix);
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

	private static void calculateDD(int x1, int x2, int k, int piece, int[][][] matrix) {

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

	private static void format(){
		sum = new boolean[x.getX()+1][x.getY()+1][7];
		for (int i = 0; i < x.getX(); i++) {
			for (int j = 0; j < x.getY(); j++) {
				int v1 = 8 - p.getX_loc() +i;
				int v2 = 8 - p.getY_loc() +j;	
				int o1 = 8 - q.getX_loc() +i;
				int o2 = 8 - q.getY_loc() +j;	
				if(( matrix[v1][v2][p.getPiece()] + matrix[o1][o2][q.getPiece()] ) == p.getLen()){
					sum[i][j][p.getPiece()] = true;
				}else{
					sum[i][j][p.getPiece()] = false;
				}
			}
		}
	}

	private static void printMatrix(int piece, int[][][] matrix) {
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
				}else if(matrix[i][j][piece] == -3){
					System.out.print("= ");
				}else{
					System.out.print(matrix[i][j][piece] + " ");
				}
			}
			System.out.println();
		}	
		System.out.println();
	}

	private static void printMatrix(int piece, boolean[][][] matrix) {
		int lp=0, ll=0;


		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix.length; j++) {

				System.out.print(matrix[i][j][piece] + " ");

			}
			System.out.println();
		}	
		System.out.println();
	}

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
	
	private static void createGraph(){
		if(x.isTwoD())
		for (int i = 1; i < x.getX(); i++) {
			for (int j = 1; j < x.getY(); j++) {
				graph.addVertex(i+" "+j);
				positionVertexAt(i+" "+j, i*100, j*100);
			}
		}
		frame.pack();
	}
	
	private static void positionVertexAt( Object vertex, int x, int y ) {
		DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
		Map              attr = cell.getAttributes(  );

		GraphConstants.setBounds( attr, new Rectangle( x, y, 30, 30 ) );

		Map cellAttr = new HashMap(  );
		cellAttr.put( cell, attr );
		m_jgAdapter.edit( cellAttr, null, null, null );
	}

}
