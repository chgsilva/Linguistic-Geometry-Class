package project1;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

//Student: Carlos Henrique Goncalves e Silva
//Student ID: 104668154

public class Operations3d {

	public static int matrix[][][][];
	public static Set x;
	public static Element p;

	public static void calculate3d(Set x, Element p) {
		matrix = new int [(x.getX())+1][(x.getY())+1][x.getZ()+1][8]; //I will ignore the 0x and the x0 for convenience. So I need a bigger matrix 

		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int k = 1; k <= x.getZ(); k++) {

					matrix[i][j][k][p.getPiece()] = callCorrect(p.getX_loc(),p.getY_loc(), p.getZ_loc(), i, j, k, p.getPiece()); 
//					matrix[i][j][k][1] = calculateKing(p.getX_loc(),p.getY_loc(), p.getZ_loc(), i, j, k); 
//					matrix[i][j][k][2] = calculateBishop(p.getX_loc(),p.getY_loc(), p.getZ_loc(), i, j, k); 
//					matrix[i][j][k][3] = calculateQueen(p.getX_loc(),p.getY_loc(), p.getZ_loc(), i, j, k); 
//					matrix[i][j][k][4] = calculateNight(p.getX_loc(), p.getY_loc(), p.getZ_loc(), i, j, k); //					matrix[i][j][k][5] = calculatePawn(x.getX(),x.getY(), x.getZ(), i, j, k);
				}
			}
		}

//		for (int piece = 0; piece < 6; piece++) {	
			moreThanOne(1, p.getPiece());
			System.out.println(p.getPiece());
			matrix[p.getX_loc()][p.getY_loc()][p.getZ_loc()][p.getPiece()] = 0;
			printMatrix(p.getPiece());
//		}
	}

	public static void calculate3dObstacles(Set x, Element p){
		matrix = new int[x.getX()+1][x.getY()+1][x.getZ()+1][8];

		int piece = p.getPiece();

		int [][]obs = x.getObstacles();
		for (int i = 0; i < obs.length; i++) {
			matrix [obs[i][0]][obs[i][1]][obs[i][2]][piece] = -1;
		}

		for (int i = 1; i <= x.getX(); i++) {
			for (int j = 1; j <= x.getY(); j++) {
				for (int k = 1; k <= x.getZ(); k++) {
					matrix[i][j][k][piece] = calculateObs(p.getX_loc(), p.getY_loc(), p.getZ_loc(), i, j, k, piece); 
				}
			}
		}

		moreThanOne(1, piece);
		moreThanOneNeg(1, piece);
		matrix[p.getX_loc()][p.getY_loc()][p.getZ_loc()][piece] = 0;
		printMatrix(piece);


	}

	public static void calculateObstacles(int piece) {
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

	public static void moreThanOne(int pivot, int piece) {
		boolean modified = false;
		if(pivot<x.getX()){
			for (int i = 1; i <= x.getX(); i++) {
				for (int j = 1; j <= x.getY(); j++) {
					for (int k = 1; k <= x.getZ(); k++) {
						if(matrix[i][j][k][piece]==pivot){
							calculateDD(i, j, k, pivot, piece);
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
								moreThanOne(pivot+1, piece);
							}
						}
					}
				}
			}
		}
	}

	public static void moreThanOneNeg(int pivot, int piece) {
		if(pivot<x.getX()){
			for (int i = 1; i <= x.getX(); i++) {
				for (int j = 1; j <= x.getY(); j++) {
					for (int k = 1; k < x.getZ(); k++) {
						if(matrix[i][j][k][piece]==pivot){
							calculateDDNeg(i, j, k, pivot, piece);
						}
					}
				}
			}

			for (int i = 1; i < x.getX(); i++) {
				for (int j = 1; j < x.getY(); j++) {
					for (int k = 1; k < x.getZ(); k++) {
						if((matrix[i][j][k][piece]) < (-1)){
							moreThanOneNeg(pivot+1, piece);
						}
					}
				}
			}
		}
	}

	public static void calculateDD(int x1, int x2, int x3, int k, int piece) {
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

	public static void calculateDDNeg(int x1, int x2, int x3, int k, int piece) {

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

	public static void printMatrix(int piece) {
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

	public static int calculateObs(int x1, int x2,int x3, int y1, int y2, int y3, int piece){
		if(matrix[y1][y2][y3][piece]==-1){//obstacle
			calculateObstacles(piece);
			matrix[y1][y2][y3][piece]=-1;
			//			System.out.println(matrix[y1][y2][piece]);
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
