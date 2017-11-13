package snu.bike.wholeExomSeq;

public class SamtoolsExecutor extends Executor{
	private final String OUTPUT_SAMTOOLS = ".filtered.bam";
	
	public SamtoolsExecutor()	{}
	
	public SamtoolsExecutor(String exePath, String outputFile_bwa) {
		super();
		
		this.exePath = exePath;
		this.inputFile = outputFile_bwa;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_SAMTOOLS);
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		//samtools view -bSh -F 0x800 test.sam -o test.filtered.bam

		StringBuilder builder = new StringBuilder();
//		builder.append(exePath);	//TODO: After samtools lib install under /bin folder. then erase this comment line.
		builder.append("samtools view -bSh -F 0x800");
		builder.append(WHITESPACE);
		
		builder.append(inputFile);
		builder.append(WHITESPACE);
		
		builder.append("-o");
		builder.append(WHITESPACE);
		
		builder.append(outputFile);
		
		builder.append("\n");
		builder.append("rm");
		builder.append(WHITESPACE);
		builder.append(inputFile);	//remove intermediate creation file.
		
		return builder.toString();
	}
}


