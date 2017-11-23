package snu.bike.ngnpipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Executor {
	protected final String WHITESPACE = " ";
	protected String script_path = "./default.sh"; 
	protected String inputFile;
	protected String outputFile;
	protected String exePath;
	/**
	 * make command script.
	 * */
	protected abstract String makeCommand();	
	
	/**
	 * shell script execute.
	 * */
	public void excute() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("/bin/sh " + script_path);
			process.waitFor();
			
			//TODO: to get print logs from each library.
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			String s = null;
			while((s = stdInput.readLine()) != null)	{
				System.out.println(s);
			}
			
			while((s = stdError.readLine()) != null)	{
				System.out.println(s);
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param shPath location made shell script file
	 * 
	 * @doc write a command in a shell script.
	 * 
	 * */
	protected void makeShellFile(String shPath)	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(shPath));
			String command = makeCommand();
			
			System.out.println(command);
			out.write(command); out.newLine();
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * return outputFileName
	 * connect to next step
	 * */
	public String getOutputFile() {
		return outputFile;
	}
}
