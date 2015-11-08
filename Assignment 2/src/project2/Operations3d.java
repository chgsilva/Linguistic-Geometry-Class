package project2;


import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;

//Student: Carlos Henrique Goncalves e Silva
//Student ID: 104668154

public class Operations3d {

	public static int[][][][] matrix, matrix2;
	public static boolean[][][][] sum, aux;
	public static Set x;
	public static Element p, q;
	public static ListenableGraph graph;
	public static JGraphModelAdapter m_jgAdapter;

	public static ListenableGraph Control(){
		aux = new boolean [x.getX()+1][x.getY()+1][x.getZ()+1][p.getLen()+2];

		if(x.thereIsObstacle()){
			matrix = calculate3dObstacles(x, p, matrix);
			matrix2 = calculate3dObstacles(x,q, matrix2); 
		}else{
			matrix = calculate3d(x, p, matrix);
			matrix2 = calculate3d(x, q, matrix2);
		}
		format(matrix, matrix2);
		evaluateAux(0, p.getX_loc(), p.getY_loc(), p.getZ_loc());
		calculateDistances(0, p.getX_loc(), p.getY_loc(), p.getZ_loc(), 0);

		return graph;
	}

	private static Ret[] calculateDistances(int k, int x_ult, int y_ult, int z_ult, int actual) {
		Ret []ret ;
		ret = new Ret[x.getX()*x.getY()*x.getZ()];

		if(k > p.getLen()){
			ret[0] = new Ret("");
			return ret;
		}

		int xmax = x.getX(),ymax = x.getY(), zmax = x.getZ();
		for (int x = 1; x <= xmax ; x++) {
			for (int y = 1; y <= ymax; y++) {
				for (int z = 1; z <= zmax; z++) {

					if(aux[x][y][z][k]){
						actual ++;
						x_ult=x;
						y_ult=y;
						z_ult=z;
//						System.out.println(aux[1][1][3]);
						graph.addVertex(x+" "+y+" "+z);
						if(x != 0 || y!=0 || z!=0)
							positionVertexAt(x+" "+y+" "+z, x*150 +z*40, y*150+z*40);

						Ret r[];
						if(k+1 > p.getLen()){
							ret = addRet(ret, new Ret(x_ult+" "+y_ult+" "+z_ult));
							return ret;
						}
						evaluateAux(k+1, x_ult, y_ult, z_ult);
						ret = addRet(ret, new Ret(x_ult+" "+y_ult+" "+z_ult));
						r = calculateDistances(k+1, x_ult, y_ult, z_ult, actual);

						for (int i = 0; i<64; i++) {
							if(r[i]!=null){
								if(!r[i].getRet().equals("")){
									graph.addEdge(x+" "+y +" "+z, r[i].getRet());

								}
							}else{
								break;
							}
						}
					}
				}
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

	
	private static Ret[] addRet(Ret[] ret, Ret ret2) {
		for (int i = 0; i < x.getX()* x.getY(); i++) {
			if(ret[i] == null){
				ret[i] = ret2;
				break;
			}
		}
		return ret;
	}


	private static void format(int matrix[][][][], int matrix2[][][][]){
		sum = new boolean[x.getX()+1][x.getY()+1][x.getZ()+1][7];
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int l = 1; l <= x.getZ(); l++) {
					if(( matrix[i][j][l][p.getPiece()] + matrix2[i][j][l][p.getPiece()] ) == p.getLen()){
						sum[i][j][l][p.getPiece()] = true;
					}else{
						sum[i][j][l][p.getPiece()] = false;
					}
				}
			}
		}
	}


	private static void evaluateAux(int k, int x_ult, int y_ult, int z_ult){
		int found = 0;

		for (int i = 1 ; i <= x.getX(); i++) {
			for (int j = 1 ; j <= x.getY(); j++) {
				for (int l = 1; l <= x.getZ(); l++) {
					if(( matrix[i][j][l][p.getPiece()] == k) && (matrix[i][j][l][q.getPiece()] - matrix[x_ult][y_ult][z_ult][q.getPiece()]  == 1) && sum[i][j][j][p.getPiece()] ){
						// map(n) && map(1) && SUM
						aux[i][j][l][k] = true;
						found ++;
					}else{
						aux[i][j][l][k] = false;
					}

				}
			}
		}

		aux[p.getX_loc()][p.getY_loc()][p.getZ_loc()][0] = true;
	}


	public static int[][][][] calculate3d(Set x, Element p, int[][][][] matrix) {
		matrix = new int [(x.getX())+1][(x.getY())+1][x.getZ()+1][8]; //I will ignore the 0x and the x0 for convenience. So I need a bigger matrix 
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int k = 1; k <= x.getZ(); k++) {
					matrix[i][j][k][p.getPiece()] = callCorrect(p.getX_loc(),p.getY_loc(), p.getZ_loc(), i, j, k, p.getPiece()); 
				}
			}
		}

		moreThanOne(1, p.getPiece(), matrix);
		matrix[p.getX_loc()][p.getY_loc()][p.getZ_loc()][p.getPiece()] = 0;
		return matrix;
	}

	public static int[][][][] calculate3dObstacles(Set x, Element p, int[][][][] matrix){
		matrix = new int[x.getX()+1][x.getY()+1][x.getZ()+1][8];

		int piece = p.getPiece();

		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			matrix [obs[i][0]][obs[i][1]][obs[i][2]][piece] = -1;
		}

		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int k = 1; k <= x.getZ(); k++) {
					matrix[i][j][k][piece] = calculateObs(p.getX_loc(), p.getY_loc(), p.getZ_loc(), i, j, k, piece, matrix); 
				}
			}
		}

		moreThanOne(1, piece, matrix);
		moreThanOneNeg(1, piece, matrix);
		matrix[p.getX_loc()][p.getY_loc()][p.getZ_loc()][piece] = 0;
		//		printMatrix(piece);

		return matrix;
	}

	public static void calculateObstacles(int piece, int[][][][] matrix) {
		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			if(obs[i][0] == p.getX_loc() && obs[i][2] == p.getZ_loc() && (piece == 0 || piece == 3)){
				if(p.getY_loc()>obs[i][1]){
					for (int j = obs[i][1]; j >= 1; j--) {
						matrix[obs[i][0]][j][obs[i][2]][piece] = -2;
					}
				}else if(p.getY_loc()<obs[i][1]){
					for (int j = obs[i][1]; j <= x.getY(); j++) {
						matrix[obs[i][0]][j][obs[i][2]][piece] = -2;
					}
				}
			}else if(obs[i][1] == p.getY_loc() && obs[i][2] == p.getZ_loc() && (piece == 0 || piece == 3)){
				if(p.getX_loc()>obs[i][0]){
					for (int j = obs[i][0]; j > 0; j--) {
						matrix[j][obs[i][1]][obs[i][2]][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0]){
					for (int j = obs[i][0]; j <= x.getX(); j++) {
						matrix[j][obs[i][1]][obs[i][2]][piece] = -2;
					}
				}
			}else if(obs[i][0] == p.getX_loc() && obs[i][1] == p.getY_loc() && (piece == 0 || piece == 3)){
				if(p.getZ_loc()>obs[i][2]){
					for (int j = obs[i][2]; j > 0; j--) {
						matrix[obs[i][0]][obs[i][1]][j][piece] = -2;
					}
				}else if(p.getZ_loc()<obs[i][2]){
					for (int j = obs[i][2]; j <= x.getZ(); j++) {
						matrix[obs[i][0]][obs[i][1]][j][piece] = -2;
					}
				}
			}else if(Math.abs(obs[i][0]-p.getX_loc()) - Math.abs(obs[i][1]-p.getY_loc()) == 0 && p.getZ_loc() == obs[i][2] && (piece == 2 || piece == 3)){
				if(p.getX_loc()>obs[i][0] && p.getY_loc()>obs[i][1]){
					for (int j = 1; obs[i][0]-j >0 && obs[i][1]-j >0 ; j++) {
						if(matrix[obs[i][0]-j][obs[i][1]-j][obs[i][2]][piece]!=-1)
							matrix[obs[i][0]-j][obs[i][1]-j][obs[i][2]][piece] = -2;
					}
				}else if(p.getX_loc()>obs[i][0] && p.getY_loc()<obs[i][1]){
					for (int j = 1; obs[i][0]+j >= 1 && obs[i][1]+j <= x.getY() ; j++) {
						if(matrix[obs[i][0]-j][obs[i][1]+j][obs[i][2]][piece]!=-1)
							matrix[obs[i][0]-j][obs[i][1]+j][obs[i][2]][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0] && p.getY_loc()>obs[i][1]){
					for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][1]+j >= 1 ; j++) {
						if(matrix[obs[i][0]+j][obs[i][1]-j][obs[i][2]][piece]!=-1)
							matrix[obs[i][0]+j][obs[i][1]-j][obs[i][2]][piece] = -2;
					}
				}else if(p.getX_loc()<obs[i][0] && p.getY_loc()<obs[i][1]){
					for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][1]+j <= x.getY() ; j++) {
						if(matrix[obs[i][0]+j][obs[i][1]+j][obs[i][2]][piece]!=-1)
							matrix[obs[i][0]+j][obs[i][1]+j][obs[i][2]][piece] = -2;
					}

				}else if(Math.abs(obs[i][0]-p.getX_loc()) - Math.abs(obs[i][2]-p.getZ_loc()) == 0 && p.getY_loc() == obs[i][1] && (piece == 2 || piece == 3)){
					if(p.getX_loc()>obs[i][0] && p.getZ_loc()>obs[i][2]){
						for (int j = 1; obs[i][0]-j >0 && obs[i][2]-j >0 ; j++) {
							if(matrix[obs[i][0]-j][obs[i][1]][obs[i][2]-j][piece]!=-1)
								matrix[obs[i][0]-j][obs[i][1]][obs[i][2]-j][piece] = -2;
						}
					}else if(p.getX_loc()>obs[i][0] && p.getY_loc()<obs[i][1]){
						for (int j = 1; obs[i][0]-j >= 1 && obs[i][1]+j <= x.getY() ; j++) {
							if(matrix[obs[i][0]-j][obs[i][1]][obs[i][2]+j][piece]!=-1)
								matrix[obs[i][0]-j][obs[i][1]][obs[i][2]+j][piece] = -2;
						}
					}else if(p.getX_loc()<obs[i][0] && p.getY_loc()>obs[i][1]){
						for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][2]-j >= 1 ; j++) {
							if(matrix[obs[i][0]+j][obs[i][1]][obs[i][2]-j][piece]!=-1)
								matrix[obs[i][0]+j][obs[i][1]][obs[i][2]-j][piece] = -2;
						}
					}else if(p.getX_loc()<obs[i][0] && p.getY_loc()<obs[i][2]){
						for (int j = 1; obs[i][0]+j <= x.getX() && obs[i][2]+j <= x.getY() ; j++) {
							if(matrix[obs[i][0]+j][obs[i][1]][obs[i][2]+j][piece]!=-1)
								matrix[obs[i][0]+j][obs[i][1]][obs[i][2]+j][piece] = -2;
						}

					}else if(Math.abs(obs[i][0]-p.getX_loc()) - Math.abs(obs[i][2]-p.getZ_loc()) == 0 && p.getX_loc() == obs[i][0] && (piece == 2 || piece == 3)){
						if(p.getY_loc()>obs[i][1] && p.getZ_loc()>obs[i][2]){
							for (int j = 1; obs[i][1]-j >0 && obs[i][2]-j >0 ; j++) {
								if(matrix[obs[i][0]][obs[i][1]-j][obs[i][2]-j][piece]!=-1)
									matrix[obs[i][0]][obs[i][1]-j][obs[i][2]-j][piece] = -2;
							}
						}else if(p.getZ_loc()>obs[i][2] && p.getY_loc()<obs[i][1]){
							for (int j = 1; obs[i][2]+j >= 1 && obs[i][1]+j <= x.getY() ; j++) {
								if(matrix[obs[i][0]][obs[i][1]-j][obs[i][2]+j][piece]!=-1)
									matrix[obs[i][0]][obs[i][1]-j][obs[i][2]+j][piece] = -2;
							}
						}else if(p.getX_loc()<obs[i][2] && p.getY_loc()>obs[i][1]){
							for (int j = 1; obs[i][1]+j <= x.getY() && obs[i][1]+j >= 1 ; j++) {
								if(matrix[obs[i][0]][obs[i][1]+j][obs[i][2]-j][piece]!=-1)
									matrix[obs[i][0]][obs[i][1]+j][obs[i][2]-j][piece] = -2;
							}
						}else if(p.getX_loc()<obs[i][2] && p.getY_loc()<obs[i][1]){
							for (int j = 1; obs[i][2]+j <= x.getZ() && obs[i][1]+j <= x.getY() ; j++) {
								if(matrix[obs[i][0]][obs[i][1]+j][obs[i][2]+j][piece]!=-1)
									matrix[obs[i][0]][obs[i][1]+j][obs[i][2]+j][piece] = -2;
							}
						}
					}

				}
			}
			matrix [obs[i][0]][obs[i][1]][obs[i][2]][piece] = -1;
		}

	}

	public static void moreThanOne(int pivot, int piece, int[][][][] matrix) {
		boolean modified = false;
		if(pivot<x.getX()){
			for (int i = 1; i <= x.getX(); i++) {
				for (int j = 1; j <= x.getY(); j++) {
					for (int k = 1; k <= x.getZ(); k++) {
						if(matrix[i][j][k][piece]==pivot){
							calculateDD(i, j, k, pivot, piece, matrix);
							modified = true;
						}
					}
				}
			}
			if(modified){	
				for (int i = 1; i <= x.getX(); i++) {
					for (int j = 1; j <= x.getY(); j++) {
						for (int k = 1; k <= x.getZ(); k++) {
							if(matrix[i][j][k][piece]==0){
								moreThanOne(pivot+1, piece, matrix);
							}
						}
					}
				}
			}
		}
	}

	public static void moreThanOneNeg(int pivot, int piece, int[][][][] matrix) {
		if(pivot<x.getX()){
			for (int i = 1; i <= x.getX(); i++) {
				for (int j = 1; j <= x.getY(); j++) {
					for (int k = 1; k < x.getZ(); k++) {
						if(matrix[i][j][k][piece]==pivot){
							calculateDDNeg(i, j, k, pivot, piece, matrix);
						}
					}
				}
			}

			for (int i = 1; i < x.getX(); i++) {
				for (int j = 1; j < x.getY(); j++) {
					for (int k = 1; k < x.getZ(); k++) {
						if((matrix[i][j][k][piece]) < (-1)){
							moreThanOneNeg(pivot+1, piece, matrix);
						}
					}
				}
			}
		}
	}

	public static void calculateDD(int x1, int x2, int x3, int k, int piece, int[][][][] matrix) {
		int ret = 0;
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int q = 1; q <= x.getZ(); q++) {
					if(matrix[i][j][q][piece]==0){
						ret = callCorrect(x1, x2, x3, i, j, q, piece);
						if(ret == 1){
							matrix[i][j][q][piece] = ret+k;
						}
					}
				}
			}
		}
	}

	public static void calculateDDNeg(int x1, int x2, int x3, int k, int piece, int[][][][] matrix) {

		int ret = 0;
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int q = 1; q <= x.getZ(); q++) {
					if(matrix[i][j][q][piece]==-2){
						ret = callCorrect(x1, x2, x3, i, j, q, piece);
						if(ret == 1){
							matrix[i][j][q][piece] = ret+k;
						}
					}
				}
			}
		}
	}

	public static void printMatrix(int piece, int[][][][] matrix) {
		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int k = 1; k <= x.getZ(); k++) {

					if(matrix[i][j][k][piece] == -1) {
						System.out.print("_ ");
					}else if(matrix[i][j][k][piece] == -2) {
						System.out.print("a ");
					}else{
						//						System.out.print(" "+i+"x"+j+"x"+k+" ");
						System.out.print(matrix[i][j][k][piece] + " ");
					}
				}
				System.out.println();
			}
			System.out.println();
		}	
		System.out.println();
	}
	
	public static void printMatrix(int piece, boolean[][][][] matrix) {
		for (int i = 0; i < x.getX(); i++) {
			for (int j = 0; j < x.getY(); j++) {
				for (int k = 0; k < x.getZ(); k++) {
						System.out.print(matrix[i][j][k][piece] + " ");	
				}
				System.out.println();
			}
			System.out.println();
		}	
		System.out.println();
	}


	public static int calculateKing(int x1, int x2, int x3, int y1, int y2, int y3) {

		if((Math.abs(x3-y3)<=1) && (Math.abs(x2-y2)<=1) && (Math.abs(x1-y1)<=1)){
			return 1;
		}
		return 0;
	}

	public static int calculateRook(int x1, int x2,int x3, int y1, int y2, int y3) { //ROOK
		if((x1==y1 && x3==y3 ) || (x2==y2 && x3==y3) || (x1==y1 && x2==y2)){
			return 1;
		}
		return 0;
	}

	public static int calculateBishop(int x1, int x2,int x3, int y1, int y2, int y3){
		if(((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0 && x3==y3) ||
				((Math.abs(x1-y1) - Math.abs(x3-y3)) == 0 && x2==y2) ||
				((Math.abs(x2-y2) - Math.abs(x3-y3)) == 0 && x1==y1)  ){
			return 1;
		}

		return 0;
	}

	public static int calculateQueen(int x1, int x2,int x3, int y1, int y2, int y3){
		if(((Math.abs(x1-y1) - Math.abs(x2-y2)) == 0 && x3==y3) ||
				((Math.abs(x1-y1) - Math.abs(x3-y3)) == 0 && x2==y2) ||
				((Math.abs(x2-y2) - Math.abs(x3-y3)) == 0 && x1==y1) ||
				((Math.abs(x2-y2) - Math.abs(x3-y3) == 0 && Math.abs(x1-y1) - Math.abs(x3-y3) == 0)) ||
				(x1==y1 && x3==y3 ) || (x2==y2 && x3==y3) || (x1==y1 && x2==y2)){
			return 1;
		}


		return 0;
	}

	public static int calculateNight(int x1, int x2,int x3, int y1, int y2, int y3){

		if( ((Math.abs(x1-y1) == 1) && (Math.abs(x2-y2) == 2) && (Math.abs(x3-y3) == 0) ) || 
				((Math.abs(x1-y1) == 2) && (Math.abs(x2-y2) == 1) && (Math.abs(x3-y3) == 0) ) ||
				((Math.abs(x1-y1) == 2) && (Math.abs(x2-y2) == 0) && (Math.abs(x3-y3) == 1) ) ||
				((Math.abs(x1-y1) == 1) && (Math.abs(x2-y2) == 0) && (Math.abs(x3-y3) == 2) ) ||
				((Math.abs(x1-y1) == 0) && (Math.abs(x2-y2) == 2) && (Math.abs(x3-y3) == 1) ) ||
				((Math.abs(x1-y1) == 0) && (Math.abs(x2-y2) == 1) && (Math.abs(x3-y3) == 2) ) ){
			//			System.out.println("entrou");
			return 1;

		}

		return 0;
	}

	public static int calculateObs(int x1, int x2,int x3, int y1, int y2, int y3, int piece, int[][][][] matrix){
		if(matrix[y1][y2][y3][piece]==-1){//obstacle
			calculateObstacles(piece, matrix);
			matrix[y1][y2][y3][piece]=-1;
			return matrix[y1][y2][y3][piece];	
		}else if((1==callCorrect(x1, x2, x3, y1, y2, y3, piece)) && (matrix[y1][y2][y3][piece]!=-2) ){
			return 1;
		}else{
			return matrix[y1][y2][y3][piece];	
		}
	}

	public static int callCorrect(int x1, int x2, int x3, int i, int j, int k, int piece) {
		int ret = 0;
		switch (piece) {
		case 0://ROOK				
			ret = calculateRook(x1, x2, x3, i, j, k);
			break;

		case 1://KING
			ret = calculateKing(x1, x2, x3, i, j, k);
			break;

		case 2://BISHOP
			ret = calculateBishop(x1, x2, x3, i, j, k);
			break;

		case 3://QUEEN
			ret = calculateQueen(x1, x2, x3, i, j, k);
			break;

		case 4://KNIGHT
			ret = calculateNight(x1, x2, x3, i, j, k);
			break;

		case 5://PAWN
			//			ret = calculatePawn(x1, x2, x3, i, j, k);
			break;

		case 6://EVALUATE
			ret = calculateEvaluate(x1, x2, x3, i, j, k);
			break;

		case 7://EVALUATE
			ret = 10000;
			break;
		}

		return ret;
	}

	public static int calculateEvaluate(int x1, int x2,int y1, int y2, int j, int k){
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
