package snu.bike.wholeExomSeq;

import static java.lang.System.out;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CliParser {

	private String[] args;	//get cli commands 

	@Option(name="-e", aliases="-ef", usage="path of excution files")
	private String excutionPath;	
	
	@Option(name="-p", aliases="-P", usage="number of processes.")
	private int process;

	@Option(name="-r", aliases="--ref", usage="reference sequence fasta file")
	private String referenceSequence;	

	@Option(name="-i", usage="")
	private String inputSet;
	private String inputNormal;
	private String inputTumor;

	@Option(name="-indel", usage="")
	private String indelFile;

	@Option(name="-snp", usage="")
	private String snpFile;

	@Option(name="-c", usage="")
	private String cosmicFile;

	@Option(name="-o", usage="")
	private String outputFile;

	public int getProcess() {
		return process;
	}

	public String getReferenceSequence() {
		return referenceSequence;
	}

	public String getIndelFile() {
		return indelFile;
	}

	public String getSnpFile() {
		return snpFile;
	}

	public String getCosmicFile() {
		return cosmicFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public CliParser(String[] args) throws IOException {
		this.args = args;
		
		doParsing();
//		System.out.println(this.inputSet);
		for( String set : Utils.splitInputSets(this.inputSet))	{
			int idx = set.lastIndexOf("/");
			if(idx == -1)	{
				idx = 0;
			}
			
			String setID = set.substring(idx+1);
			String directory = set.substring(0, idx+1);
//			System.out.println(setID + " : " + directory);
			String[] inputSet = Utils.recursiveFileRead(directory,setID);
//			System.out.println(inputSet);
			this.inputNormal = inputSet[0];
			this.inputTumor = inputSet[1];
		}
		
	}

	public String getExecutionPath() {
		return excutionPath;
	}

	private void doParsing() throws IOException
	{
		final CmdLineParser parser = new CmdLineParser(this);
		if (args.length < 1)
		{
			parser.printUsage(out);
			System.exit(-1);
		}
		try
		{
			parser.parseArgument(args);
		}
		catch (CmdLineException clEx)
		{
			out.println("ERROR: Unable to parse command-line options: " + clEx);
		}
		
		out.println(this.toString());
	}

	@Override
	public String toString() {
		return "CliClass [excutionPath=" + excutionPath + ", process=" + process + ", referenceSequence="
				+ referenceSequence + ", inputFastaq=" + inputSet + ",\nindelFile=" + indelFile + ", snpFile="
				+ snpFile + ", cosmicFile=" + cosmicFile + ", outputFile=" + outputFile + "]";
	}

	public String getInputNormal() {
		return inputNormal;
	}

	public void setInputNormal(String inputNormal) {
		this.inputNormal = inputNormal;
	}

	public String getInputTumor() {
		return inputTumor;
	}

	public void setInputTumor(String inputTumor) {
		this.inputTumor = inputTumor;
	}

	
}
