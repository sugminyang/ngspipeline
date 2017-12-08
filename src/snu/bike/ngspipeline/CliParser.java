package snu.bike.ngspipeline;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileReader;
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
	
	@Option(name="-i", usage="multiinput file read")
	private String inputFile;
	
	//exome
	private Vector<String> inputNormal;
	private Vector<String> inputTumor;
	
	//rna
	private Vector<SamplePair> inputPair;

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

		//file read.
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String lineString = "";
		String inputDir = "";
		boolean flag = false;
		inputPair = new Vector<SamplePair>();
		
		while((lineString=reader.readLine()) != null) {
			String[] splitedLine = lineString.split("\t");
			
			if(flag)	{
				inputPair.add(new SamplePair(splitedLine[0], splitedLine[1],inputDir));
				System.out.println(inputPair.get(inputPair.size()-1));
			}
			else if(lineString.contains("input_dir"))	{
				if(splitedLine.length == 2)	{
					inputDir = splitedLine[1];	
				}
			}
			else if(lineString.contains("pair_1") && lineString.contains("pair_2"))	{
				flag = true;
			}
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
					+ referenceSequence + ", inputFastaq=" + inputFile + ",\nindelFile=" + indelFile + ", snpFile="
					+ snpFile + ", cosmicFile=" + cosmicFile + ", outputFile=" + outputFile + "]";
		}
		else	{
			str = "[error] option error!! ";
		}
		
		return str;
	}

	public Vector<String> getInputNormal() {
		return inputNormal;
	}


	public Vector<String> getInputTumor() {
		return inputTumor;
	}


	public String getNgsMode() {
		return ngsMode;
	}

	public Vector<SamplePair> getInputPair() {
		return inputPair;
	}

	public String getIndexes() {
		return indexes;
	}

	public String getMergeFile() {
		return mergeFile;
	}

	
}
