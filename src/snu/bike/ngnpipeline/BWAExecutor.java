package snu.bike.ngnpipeline;

public class BWAExecutor extends Executor{
	private final String OUTPUT_BWA = ".sam";
	private int process;
	private String refSeq;
	
	public BWAExecutor(String excutionPath, int process, String referenceSequence, String inputFile) {
		super();
//		System.out.println("################"+this.getClass().getSimpleName());	//to get a classname.
		this.exePath = excutionPath; 
		this.process = process;
		this.refSeq = referenceSequence;
		this.inputFile = inputFile;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_BWA);
		
		makeShellFile(this.script_path);
		
	}
	
	/**
	 * 
	 * @return coordinated string : command line equation
	 * 
	 * @doc return a string that can be executed in command line.
	 * 
	 * */
	@Override
	protected String makeCommand()	{
//		String command = excutionPath + "bwa mem -M -t " + process + " "
//		+ referenceSequence + " " + inputFile + " "
//		+ "> " + outputFile;
//
//		System.out.println(command);
		String defaultOptions = "bwa mem -M -t";
		StringBuilder builder = new StringBuilder();
		builder.append(exePath);
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(process);
		builder.append(WHITESPACE);
		
		builder.append(refSeq);
		builder.append(WHITESPACE);
		
		builder.append(inputFile);
		builder.append(WHITESPACE);
		
		builder.append(">");
		builder.append(WHITESPACE);
		builder.append(outputFile);
		
		return builder.toString();
	}

}

