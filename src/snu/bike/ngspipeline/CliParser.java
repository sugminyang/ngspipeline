package snu.bike.ngspipeline;

import static java.lang.System.out;

import java.io.IOException;
import java.util.Vector;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CliParser {

	private String[] args;	//get cli commands 
	
	@Option(name="-m", aliases="-mode", usage="rna-seq/exome-seq distinguish(input only \"rna\" or \"exome\")")
	private String ngsMode;
	
	@Option(name="-e", aliases="-ef", usage="path of excution files")
	private String excutionPath;	
	
	@Option(name="-p", aliases="-P", usage="number of processes.")
	private int process;

	@Option(name="-r", aliases="--ref", usage="reference sequence fasta file")
	private String referenceSequence;	

	@Option(name="-mg", aliases="--merge", usage="assemble transcripts..")
	private String mergeFile;		
	
	//rna-seq hisat index
	@Option(name="-idx", aliases="--index", usage="hisat2 index")
	private String indexes;	
	
	@Option(name="-i", usage="")
	private String inputSet;
	
	//exome
	private String inputNormal;
	private String inputTumor;
	
	//rna
	private Vector<String> inputPair1;
	private Vector<String> inputPair2;	

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
		if(ngsMode.equalsIgnoreCase("exom"))	{
			for( String set : Utils.splitInputSets(this.inputSet))	{	// inputset1, inputset2, ...
				int idx = set.lastIndexOf("/");
				if(idx == -1)	{
					idx = 0;
				}
				
				String setID = set.substring(idx+1);
				String directory = set.substring(0, idx+1);
	//			System.out.println(setID + " : " + directory);
				String[] inputSet = Utils.recursiveFileRead(directory,setID,ngsMode);
	//			System.out.println(inputSet);
				this.inputNormal = inputSet[0];
				this.inputTumor = inputSet[1];
			}
		}
		else if(ngsMode.equalsIgnoreCase("rna"))	{
			inputPair1 = new Vector<>();
			inputPair2 = new Vector<>();
			
			int idx = inputSet.lastIndexOf("/");	
			if(idx == -1)	{
				idx = 0;
			}
			
			String directory = inputSet.substring(0, idx+1);
			
			for( String set : Utils.splitInputSets(this.inputSet))	{	// inputset1, inputset2, ...
				idx = set.lastIndexOf("/");	
				
				String setID = set.substring(idx+1);
				
//				System.out.println(setID + " : " + directory);
				String[] inputSet = Utils.recursiveFileRead(directory,setID,ngsMode);
//				System.out.println(inputSet);
				this.inputPair1.add(inputSet[0]);
				this.inputPair2.add(inputSet[1]);
			}
		}
		else	{
			System.out.println("[error] ngsmode worng !!!");
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
		
		String str = "";
		
		if(ngsMode.equalsIgnoreCase("rna"))	{
			str = "CliClass [excutionPath=" + excutionPath + ", process=" + process + ", referenceSequence="
					+ referenceSequence + "]";
		}
		else if(ngsMode.equalsIgnoreCase("exom"))	{
			str = "CliClass [excutionPath=" + excutionPath + ", process=" + process + ", referenceSequence="
					+ referenceSequence + ", inputFastaq=" + inputSet + ",\nindelFile=" + indelFile + ", snpFile="
					+ snpFile + ", cosmicFile=" + cosmicFile + ", outputFile=" + outputFile + "]";
		}
		else	{
			str = "[error] option error!! ";
		}
		
		return str;
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

	public String getNgsMode() {
		return ngsMode;
	}

	public Vector<String> getInputPair1() {
		return inputPair1;
	}

	public Vector<String> getInputPair2() {
		return inputPair2;
	}

	public String getIndexes() {
		return indexes;
	}

	public String getMergeFile() {
		return mergeFile;
	}

	
}
