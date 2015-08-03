package com.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
 public static void main(String[] args) throws IOException {
	 
	 new Test().read();
	 
}
 
 static void read() throws IOException{
	 FileOutputStream fileOut =  new FileOutputStream("walid.txt");	
	 
	 byte[]t = {0,1,1,1,0};
	 fileOut.write(t);
	 fileOut.close();
 }
}
