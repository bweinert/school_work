/*
 * Least Common Ancestor
 * 
 * CS331 Assignment 5
 * 
 * Brittany Weinert
 * baw2565
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LCA {
	
	public static void main(String[] args) throws IOException{
		
		//open up file containing representation of tree
		File file = new File("input.txt");
		Scanner input = new Scanner(file);
		
		//create another file to count the number of lines (nodes)
		Scanner countInput = new Scanner(file);
		
		int size = 0;
		
		while(countInput.hasNextLine()){
			size++;
			countInput.nextLine();
		}
		
		countInput.close();
		
		//define the solutions matrix and parent list
		int[][] solnMatrix = new int[size][size];
		int[] parentList = new int[size];
		
		//fill in the base cases:
		//1. the LCA of the first node is always the first node
		//2. the LCA of a node and itself is itself
		for(int i = 0; i < size; i++){
			solnMatrix[0][i] = 1;
			solnMatrix[i][0] = 1;
			solnMatrix[i][i] = i + 1;
		}
		
		//Also fill in the parent node of the first node, which will just be itself
		parentList[0] = 1;
		
		//read in the file and fill in the given information into the solutions matrix
		while(input.hasNext()){
			
			String nodeInfo = input.nextLine();
			int tempIndexBegin = 0;
			int tempIndexEnd = nodeInfo.indexOf(' ');
			
			int currentNode = Integer.parseInt(nodeInfo.substring(0, tempIndexEnd));
			
			//for the case that the current node has children
			//read it in by making substrings by finding the 
			//index of certain characters
			if(nodeInfo.indexOf(')') - nodeInfo.indexOf('(') > 1 ){
			
				tempIndexBegin = nodeInfo.indexOf('(');
				tempIndexEnd = nodeInfo.indexOf(',');
			
				int childA = Integer.parseInt(nodeInfo.substring(tempIndexBegin + 1, tempIndexEnd)) - 1;
			
				nodeInfo = nodeInfo.substring(tempIndexEnd);
			
				tempIndexBegin = nodeInfo.indexOf(',');
				tempIndexEnd = nodeInfo.indexOf(')');
			
				int childB = Integer.parseInt(nodeInfo.substring(tempIndexBegin + 1, tempIndexEnd)) - 1;
			
				solnMatrix[childA][childB] = currentNode;
				solnMatrix[childB][childA] = currentNode;
				
				nodeInfo = nodeInfo.substring(tempIndexEnd + 1);
				tempIndexBegin = nodeInfo.indexOf('(');
				tempIndexEnd = nodeInfo.indexOf(')');
			
				//if the current node also has a parent
				if(tempIndexEnd - tempIndexBegin > 1){
					parentList[currentNode-1] = Integer.parseInt(nodeInfo.substring(tempIndexBegin + 1, tempIndexEnd));
				}
			}
			
			//for the case in which the node is a leaf
			else{
				
				nodeInfo = nodeInfo.substring(nodeInfo.indexOf(')')+1);
			
				tempIndexBegin = nodeInfo.indexOf('(');
				tempIndexEnd = nodeInfo.indexOf(')');
			
				if(tempIndexEnd - tempIndexBegin > 1){
					parentList[currentNode-1] = Integer.parseInt(nodeInfo.substring(tempIndexBegin + 1, tempIndexEnd));
				}
			}
		}
		
		
		
		
		//fill in the remaining LCAs in the solutions matrix 
		for(int i = 1; i < size; i++){
			for(int j = 1; j < size; j++){
				
				//if the information isn't already filled in
				if(solnMatrix[i][j] == 0){	
					
					int currentNode = i;	
					
					//one node is the parent of the other
					if((currentNode+1 == parentList[j])){
						solnMatrix[i][j] = currentNode+1;
						solnMatrix[j][i] = currentNode+1;
					}
					
					//if one is not the direct parent of the other,
					//starting from the lowest node, keep jumping to 
					//it's parent node until a solution already found
					//in the matrix is discovered
					else{
						currentNode = j;
						do{
						
							currentNode = parentList[currentNode] - 1;
						
						}while(solnMatrix[i][currentNode] == 0);
					
						solnMatrix[i][j] = solnMatrix[i][currentNode];
						solnMatrix[j][i] = solnMatrix[i][currentNode];
					}
				}
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
		
		//close the files
		input.close();
		output.close();
	}

}
