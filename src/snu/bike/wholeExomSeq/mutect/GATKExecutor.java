package snu.bike.wholeExomSeq.mutect;

import snu.bike.ngnpipeline.Executor;
import snu.bike.ngnpipeline.Utils;

public class GATKExecutor extends Executor{
	private String refSeq;
	private String indelFile;
	private String snpFile;
	private String inputNormal;
	private String inputTumor;
	private String outputNormal;
	private String outputTumor;
	
	public GATKExecutor(String excutionPath, String reference_sequence, String indelFile, String snpFile, String outputFile_bamtools_normal, String outputFile_bamtools_tumor) {
		super();
		
		//TODO: error echk empty path
		this.exePath = excutionPath;
		
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		this.refSeq = reference_sequence;	//reference file
		
		this.indelFile = indelFile;	//Mills ~ file
		
		this.snpFile = snpFile;	//snpdb file
		
		//TODO: error check empty filename
		this.inputFile = outputFile_bamtools_normal;	//temporary value. //TODO: revise. 
		
		this.inputNormal = outputFile_bamtools_normal;
		this.inputTumor = outputFile_bamtools_tumor;
		
		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		
		String defaultOptions = "java -Xmx2g -jar -Djava.io.tmpdir=./tmp";
		StringBuilder builder = new StringBuilder();
		
		//sample script RealignerTargetCreator.
		/*java -Xmx2g -jar -Djava.io.tmpdir=./tmp ./bin/GenomeAnalysisTK.jar -T RealignerTargetCreator 
		-R ./ref/hg19.fasta -I normal2.GR.bam -I abnormal2.GR.bam -o test.intervals 
		-known ./Mills_and_1000G_gold_standard.indels.hg19.vcf*/
		String output_RealignerTargetCreator = makeRealignerTargetCreatorCommand(builder, defaultOptions);
		builder.append("\n");
		
		//sample script IndelRealigner.
		/*java -Xmx2g -jar -Djava.io.tmpdir=./tmp ./bin/GenomeAnalysisTK.jar -T IndelRealigner 
		-R  ./ref/hg19.fasta -I normal2.GR.bam -I abnormal2.GR.bam  -targetIntervals test.intervals
		 --nWayOut '.ir.bam' -known ./Mills_and_1000G_gold_standard.indels.hg19.vcf*/
		String output_IndelRealigner = makeIndelRealignerCommand(builder, defaultOptions,output_RealignerTargetCreator);
	
		this.outputNormal = Utils.extractSampleName(this.inputNormal,".GR.ir.bam");
		
		this.outputTumor = Utils.extractSampleName(this.inputTumor,".GR.ir.bam");
		return builder.toString();
	}

	private String makeIndelRealignerCommand(StringBuilder builder, String defaultOptions, String output_RealignerTargetCreator) {
		String output_IndelRealigner = Utils.extractSampleName(this.inputFile,".ir.bam");
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "GenomeAnalysisTK.jar");
		builder.append(WHITESPACE);

		builder.append("-T IndelRealigner");
		builder.append(WHITESPACE);		

		builder.append("-R " + refSeq);
		builder.append(WHITESPACE);				
		
		builder.append("-I " + this.inputNormal);	//normal
		builder.append(WHITESPACE);

		builder.append("-I " + this.inputTumor);	//abnormal
		builder.append(WHITESPACE);		

		builder.append("-targetIntervals " + output_RealignerTargetCreator);
		builder.append(WHITESPACE);		
		
		builder.append("--nWayOut '.ir.bam'");
		builder.append(WHITESPACE);
		
		builder.append("-known " + this.snpFile);
		builder.append(WHITESPACE);
		
		return output_IndelRealigner;
	}

	private String makeRealignerTargetCreatorCommand(StringBuilder builder, String defaultOptions) {
		int idx = inputFile.indexOf("_");
		String prefix = inputFile.substring(0,idx);
		String output_RealignerTargetCreator = Utils.extractSampleName(prefix,".intervals");
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "GenomeAnalysisTK.jar");
		builder.append(WHITESPACE);

		builder.append("-T RealignerTargetCreator");
		builder.append(WHITESPACE);		

		builder.append("-R " + refSeq);
		builder.append(WHITESPACE);				
		
		builder.append("-I " + this.inputNormal);	//normal
		builder.append(WHITESPACE);
		
		builder.append("-I " + this.inputTumor);	//abnormal
		builder.append(WHITESPACE);
		
		builder.append("-o " + output_RealignerTargetCreator);
		builder.append(WHITESPACE);
		
		builder.append("-known " + this.indelFile);
		builder.append(WHITESPACE);
		
		return output_RealignerTargetCreator; 
	}

	public String outputNormal()	{
		return this.outputNormal;
	}
	
	public String outputTumor()	{
		return this.outputTumor;
	}
}
