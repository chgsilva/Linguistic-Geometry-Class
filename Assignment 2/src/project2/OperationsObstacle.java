package project2;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;

public class OperationsObstacle {

	public static Set x;
	public static Element p, q;
	public static boolean[][][] sum, aux;
	public static int [][][] matrix, matrix2;
	public static ListenableGraph graph;
	public static JGraphModelAdapter m_jgAdapter;

	
	public void values(Set x, Element p){
		this.x = x;
		this.p = p;
	}

	public static ListenableGraph Control(){
		matrix = calculate2dObstacles(x, p, matrix);
		matrix2 = calculate2dObstacles(x, q, matrix2);
		format(matrix, matrix2);
		evaluateAux(0, p.getX_loc(), p.getY_loc());
		calculateDistances(0, p.getX_loc(), p.getY_loc(), 0);
		return graph;
	}

	
	public static int[][][] calculate2dObstacles(Set x, Element p, int[][][] matrix){
		matrix = new int[x.getX()+1][x.getY()+1][8];
		//putting obstacles
		int piece = p.getPiece();

		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			matrix [obs[i][0]][obs[i][1]][piece] = -1;
		}

		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				matrix[i][j][piece] = calculateObs(p.getX_loc(), p.getY_loc(), i, j, piece, matrix); 
			}
		}

		moreThanOne(1, piece, matrix);
		moreThanOneNeg(1, piece, matrix);

		matrix[p.getX_loc()][p.getY_loc()][piece] = 0;

		return matrix;
	}

	public static void moreThanOne(int pivot, int piece, int[][][] matrix) {
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

	public static void moreThanOneNeg(int pivot, int piece, int [][][] matrix) {
		if(pivot<x.getX()){
			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if(matrix[i][j][piece]==pivot){
						calculateDDNeg(i, j, matrix[i][j][piece], piece, matrix);
					}
				}
			}

			for (int i = 1; i < matrix.length; i++) {
				for (int j = 1; j < matrix.length; j++) {
					if((matrix[i][j][piece]) < (-1)){
						moreThanOneNeg(pivot+1, piece, matrix);
					}
				}
			}
		}
	}


	public static int calculateObs(int x1, int x2,int y1, int y2, int piece, int[][][] matrix){
		if(matrix[y1][y2][piece]==-1){//obstacle
			calculateObstacles(piece, matrix);
			matrix[y1][y2][piece]=-1;
			//			System.out.println(matrix[y1][y2][piece]);
			return matrix[y1][y2][piece];	
		}else if((1==callCorrect(x1, x2, y1, y2, piece)) && (matrix[y1][y2][piece]!=-2) ){
			return 1;
		}else{
			return matrix[y1][y2][piece];	
		}
	}

	public static void calculateDD(int x1, int x2, int k, int piece, int[][][] matrix) {

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

	public static void calculateDDNeg(int x1, int x2, int k, int piece, int[][][] matrix) {

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

	public static void calculateObstacles(int piece, int[][][] matrix) {
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

	public static int callCorrect(int x1, int x2, int i, int j, int piece) {
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

	public static int calculateKing(int x1, int x2, int y1, int y2) {

		if(Math.abs(x1-y1)<=1 && Math.abs(x2-y2)<=1){
			return 1;
		}
		return 0;
	}

	public static int calculateRook(int x1, int x2,int y1, int y2) { //ROOK
		if(x1==y1 || x2==y2){
			return 1;
		}
		return 0;
	}

	public static int calculateBishop(int x1, int x2,int y1, int y2){
		if((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0){
			return 1;
		}

		return 0;
	}

	public static int calculateQueen(int x1, int x2,int y1, int y2){
		if(((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0) || (x1==y1) || (x2==y2)){
			return 1;
		}


		return 0;
	}

	public static int calculateNight(int x1, int x2,int y1, int y2){
		if(((Math.abs(x1-y1) ==  1) && (Math.abs(x2-y2) == 2) ) || ((Math.abs(x1-y1) ==  2) && (Math.abs(x2-y2) == 1) )){
			return 1;
		}

		return 0;

	}
	public static int calculatePawn(int x1, int x2,int y1, int y2){
		if((Math.abs(x1-y1) == 1) && (Math.abs(x2-y2) == 0)){
			return 1;
		}

		return 0;
	}

	public static int calculateEvaluate(int x1, int x2,int y1, int y2){
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

	private static void format(int matrix[][][], int matrix2[][][]){
		sum = new boolean[x.getX()+1][x.getY()+1][7];
		for (int i = 0; i < x.getX(); i++) {
			for (int j = 0; j < x.getY(); j++) {
				if(( matrix[i][j][p.getPiece()] + matrix2[i][j][q.getPiece()] ) == p.getLen()){
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


	private static void evaluateAux(int k, int x_ult, int y_ult){
		int found = 0;

		for (int i = 0 ; i < x.getX(); i++) {
			for (int j = 0 ; j < x.getY(); j++) {
				System.out.println(i + " " + j);
				if(( matrix[i][j][p.getPiece()] == k) &&   ((matrix[i][j][p.getPiece()] - matrix[x_ult][y_ult][p.getPiece()]) == 1) && sum[i][j][p.getPiece()] ){
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

	
	private static void positionVertexAt( Object vertex, int x, int y ) {
		DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
		Map              attr = cell.getAttributes(  );

		GraphConstants.setBounds( attr, new Rectangle( x, y, 30, 30 ) );

		Map cellAttr = new HashMap(  );
		cellAttr.put( cell, attr );
		m_jgAdapter.edit( cellAttr, null, null, null );
	}

	
}

