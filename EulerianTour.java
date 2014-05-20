/*
 * Brittany Weinert
 * baw2565
 * 
 * CS 331
 * 
 * HW7 #1
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class EulerianTour {
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
				
				//create a list of lists for each of the vertices
				int[][] graph = new int[size][size];
				
				//create boolean to make sure the vertices each have an even number of edges
				//As long as there is an even number of edges, there is a Eulerian Tour
				boolean evenNumVerts = true;
				boolean firstVertex = true;
				
				//Index used to keep track of what vertex we are on
				//lengthOfFirst gets the number of vertices for just the starting vertex
				int index = 0;
				int lengthOfFirst = 0;
				
				//read in the file and fill in the given information
				while(input.hasNext()){
					
					String vertexInfo = input.nextLine();
					int tempIndex = vertexInfo.indexOf('(') + 1;
					int tempIndexEnd = vertexInfo.indexOf(')');
					
				    vertexInfo = vertexInfo.substring(tempIndex, tempIndexEnd);
				    
				    String[] adjVerts = vertexInfo.split(",");
				    
				    if(firstVertex){
				    	lengthOfFirst = adjVerts.length - 1;
				    	firstVertex = false;
				    }
				    	
				    //if one vertex has no edges, catch it here
				    if(adjVerts.length % 2 == 0 && adjVerts.length != 0){
				    	for(int i = 0; i < adjVerts.length; i++)
				    		graph[index][i] = Integer.parseInt(adjVerts[i]) ;
				    }
				    else{
				    	evenNumVerts = false;
				    	break;
				    }
				    
				    index++;
				    
				}
				
				//This will be the output string if there is a tour
				String vertsVisited = "";
				
				//if there is an even number of edges for each vertex
				if(evenNumVerts){
					
					//keep track of which vertices were recently visited
					int currentVertex = 0;
					int nextVertex = -1;
					int lastVertex = 0;
					
					//As long as the first node has an unvisited edge
					while(lengthOfFirst != 0){
						
						vertsVisited = vertsVisited + (currentVertex + 1) + " ";
						
						//if we're at the starting vertex, decrement the #edges left for it
						if(currentVertex == 0)
							lengthOfFirst--;
						
						//the general case for picking the next edge to take:
						//remove the edge that you just used
						//take the edge listed immediately after the one just used (the next highest)
						for(int i = 0; i < size; i++){
							if(lastVertex == graph[currentVertex][i])
								graph[currentVertex][i] = 0;
							
							if(graph[currentVertex][i] > currentVertex+1){
								nextVertex = graph[currentVertex][i];
								graph[currentVertex][i] = 0;
								break;
							}		
						}
						
						//if there were no more edges that were higher than the one just taken:
						//pick the first edge left
						if(nextVertex < 0){
							for(int i = 0; i < size; i++){
								if(graph[currentVertex][i] > 0){
									nextVertex = graph[currentVertex][i];
									graph[currentVertex][i] = 0;
									break;
								}		
							}
						}
						
					
						
						lastVertex = currentVertex + 1;
						currentVertex = nextVertex - 1;
						nextVertex = -1;
						
					}
				}
				
				//Write the path to a file
				File outputFile = new File("output.txt");
				FileWriter fWriter = new FileWriter (outputFile);
			    PrintWriter output = new PrintWriter (fWriter);
				
				
				if(evenNumVerts)
					output.print(vertsVisited);
				
				else
					output.print("-1");
					
			
				
				//close the files
				input.close();
				output.close();
				
				
	}
}
