/*
 * Jeffrey Cox
 * Battleship Shared Code
 * 4/2/11
 */

import java.util.Random;


public class ShipGenerator {
	private int numplayers=0;
	private boolean[][] spaceused=new boolean[rows][cols];
	private static final int shipsperplayer=5;
	private static final int rows = 26;
	private static final int cols = 39;
	ShipGenerator(int numplayers){
		this.numplayers=numplayers;
		for (int i=0; i<rows; i++){
			for (int j=0; j<cols; j++){
				spaceused[i][j]=false;
			}
		}
		
	}
	public Ship[] generateships(){
		Ship[] ships=new Ship[numplayers*shipsperplayer];
		
		int shiplength=0;
		int shipcounter=0;
		for (int i=0; i<numplayers; i++){		
			shiplength=5;
			ships[shipcounter]=new Ship(fitship(shiplength), (i+1), shiplength);
			shipcounter++;
		}		
		for (int i=0; i<numplayers; i++){
			shiplength=4;
			ships[shipcounter]=new Ship(fitship(shiplength), (i+1), shiplength);
			shipcounter++;
		}
		for (int i=0; i<numplayers; i++){
			shiplength=3;
			ships[shipcounter]=new Ship(fitship(shiplength), (i+1), shiplength);
			shipcounter++;
		}
		for (int i=0; i<numplayers; i++){
			shiplength=3;
			ships[shipcounter]=new Ship(fitship(shiplength), (i+1), shiplength);
			shipcounter++;
		}
		for (int i=0; i<numplayers; i++){
			shiplength=2;
			ships[shipcounter]=new Ship(fitship(shiplength), (i+1), shiplength);
			shipcounter++;
		}
		
		/*int counter=0;
		for (int i=0; i<rows; i++){
			for (int j=0; j<cols; j++){
				if (spaceused[i][j]==true){
					System.out.print(1 );
					counter++;
				}else System.out.print(0 );
			}
			System.out.println();
		}
		System.out.println(counter);
		ships[0].printCoords();
		ships[1].printCoords();
		ships[2].printCoords();
		ships[3].printCoords();
		ships[4].printCoords();*/
		return ships;
	}
	public Coordinates[] fitship(int shiplength){
		Random generator=new Random();
		Coordinates[] coords=new Coordinates[shiplength];
		boolean success=false;
		
		while (success==false){
			int srow=generator.nextInt(rows); //starting row of ship to be generated
			
			int scolumn=generator.nextInt(cols); //starting column of ship to be generated
			int trycount=0;
			if (spaceused[srow][scolumn]==true) continue;
			else {
				int[] shrows=new int[shiplength];//rows of ship to be made, i.e. shiprows, or shrows
				shrows[0]=srow;  
				int[] shcolumns=new int[shiplength];//columns of ship to be made, i.e., shipcolumns or shcolumns
				shcolumns[0]=scolumn; 
				int direction=generator.nextInt(4)+1;
				
				for (int j=0; j<4; j++){
					success=true;
					if (direction==1){
						for (int k=1; k<shiplength; k++){
							if ((scolumn+k)>(cols-1)){
								success=false;
								continue;
							}
							if(spaceused[srow][scolumn+k]==true){
								success=false;
								continue;
							}else{
								shrows[k]=srow;
								shcolumns[k]=scolumn+k;
							}
						}
						if (success==true){
						break;
						}
					}
					else if (direction==2){
						for (int k=1; k<shiplength; k++){
							if ((scolumn-k)<=0){
								success=false;
								continue;
							}
							if(spaceused[srow][scolumn-k]==true){
								success=false;
								continue;
							}else{
								shrows[k]=srow;
								shcolumns[k]=scolumn-k;
							}
						}
						if (success==true){
						break;
						}
					}
					else if (direction==3){
						
						for (int k=1; k<shiplength; k++){
							if ((srow+k)>(rows-1)){
								success=false;
								continue;
							}
							if(spaceused[srow+k][scolumn]==true){
								success=false;
								continue;
							}else{
								shrows[k]=srow+k;
								shcolumns[k]=scolumn;
							}
						}
						if (success==true){
						break;
						}
					}
					else if(direction==4){
						
						for (int k=1; k<shiplength; k++){
							if ((srow-k)<=0){
								success=false;
								continue;
							}
							if(spaceused[srow-k][scolumn]==true){
								success=false;
								continue;
							}else{
								shrows[k]=srow-k;
								shcolumns[k]=scolumn;
							}
						}
						if(success==true){
						break;
						}
					}
					trycount++;
					
					direction++;
					if (direction==5) direction=1;
					//System.out.println("direction "+direction);
				}
				if (success==true){
				for (int j=0; j<shiplength; j++){
					coords[j]=new Coordinates(shrows[j], shcolumns[j]);
					spaceused[shrows[j]][shcolumns[j]]=true;
					
				}
				}
				
				
	}
			
		}
		return coords;
	}
}
