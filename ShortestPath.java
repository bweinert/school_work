import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Brittany Weinert
 * baw2565
 * 
 * CS 331
 * 
 * HW7 #4
 * 
 */


public class ShortestPath {
	public static void main(String[] args) throws IOException{
		//open up file containing representation of graph
		File file = new File("input.txt");
		Scanner input = new Scanner(file);
		
		//create another file to count the number of vertices
		Scanner countInput = new Scanner(file);
		
		int size = 0;
		
		while(countInput.hasNextLine()){
			size++;
			countInput.nextLine();
		}
		
		countInput.close();
		
		//create a matrix of the weights of the vertices
		int[][] graph = new int[size][size];
		
		//Index used to keep track of what vertex we are on
		int index = 0;
		
		//read in the file and fill in the given information
		while(input.hasNext()){
			
			String vertexInfo = input.nextLine();
			String[] tempArray = vertexInfo.split(" ");
			
			for(int i = 0; i < tempArray.length; i++){
				if(tempArray[i].equals("X"))
					graph[index][i] = -1;
				else
					graph[index][i] = Integer.parseInt(tempArray[i]);
			}
			
			index++;
		    
		}
		
		input.close();
		
		
		//matrix M[x][y][k] where each entry is a path that uses at most k edges
		int[][][] kMatrix = new int[size][size][size];
		
		//set all the original values in the k matrix to -1
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				for(int k = 0; k < size; k++)
					kMatrix[i][j][k] = -1;
		
		//set the boundary cases for k = 1
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(i == j)
					kMatrix[i][j][0] = 0;
				else
					kMatrix[i][j][0] = graph[i][j];
			}
		}
		
		//fill in the rest of the values in the k matrix
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = 1; k < size-1; k++){
					//Calculate the weight if there is a vertex x along the path from i to j
					int minAlongPath = Integer.MAX_VALUE;
					for(int x = 0; x < size; x++){
						if(kMatrix[i][x][k-1] != -1 && graph[x][j] != -1){
							int temp = kMatrix[i][x][k-1] + graph[x][j];
							if(temp < minAlongPath)
								minAlongPath = temp;
						}
					}
					
					
					//either the min path with be from kM[i][j][k-1], from the minAlongPath 
					//calculated above, or neither, and that would be set to 1
					if(minAlongPath > kMatrix[i][j][k-1] && kMatrix[i][j][k-1] != -1)
						kMatrix[i][j][k] = kMatrix[i][j][k-1];
					
					else if(minAlongPath != Integer.MAX_VALUE)
						kMatrix[i][j][k] = minAlongPath;
					
					else
						kMatrix[i][j][k] = -1;
			
					
				}
			}
		}
		
		//create the final matrix to be returned
		int[][] solnMatrix = new int[size][size];
		
		//find the minimum for each i,j from k = 0 to k < size
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				int minimumPath = Integer.MAX_VALUE;
				for(int k = 0; k < size; k++){
				//	System.out.println(i+ " "+j+": "+minimumPath+ "here");
					if(kMatrix[i][j][k] != -1 && kMatrix[i][j][k] < minimumPath)
						minimumPath = kMatrix[i][j][k];
				}
				
				if(minimumPath != Integer.MAX_VALUE)
					solnMatrix[i][j] = minimumPath;
			}
			
		}
		
		//Write the matrix to a file
				File outputFile = new File("output.txt");
				FileWriter fWriter = new FileWriter (outputFile);
			    PrintWriter output = new PrintWriter (fWriter);
				
				
				for(int i = 0; i < size; i++){
					for(int j = 0; j < size; j++){
						output.print(solnMatrix[i][j] + " ");
						if(j == (size-1))
							output.println();
					}
				}
				
				//close the file
				output.close();
		
	}

}
