package snu.bike.wholeExomSeq.mutect;

import snu.bike.wholeExomSeq.Executor;
import snu.bike.wholeExomSeq.Utils;

public class GATKRecaliExecutor extends Executor{
	private String OUTPUT_GATK_RECALI = ".FINAL.bam";
	private String refSeq;
	private String indelFile;
	private String snpFile;
	
	
	public GATKRecaliExecutor(String executionPath, String reference_sequence, String indelFile, String snpFile,String output_gatk) {
		super();
		this.exePath = executionPath;
		this.refSeq = reference_sequence;
		this.indelFile = indelFile;
		this.snpFile = snpFile;
		this.inputFile = output_gatk;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_GATK_RECALI);
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		/*java -Xmx2g -jar -Djava.io.tmpdir=./tmp   ./bin/GenomeAnalysisTK.jar
		 *  -T BaseRecalibrator -R ./ref/hg19.fasta -knownSites ./Mills_and_1000G_gold_standard.indels.hg19.vcf  
		 *  -knownSites ./dbsnp_138.hg19.vcf -I abnormal2.GR.ir.bam -o abnormal2.grp*/
		String defaultOptions = "java -Xmx2g -jar -Djava.io.tmpdir=./tmp";
		StringBuilder builder = new StringBuilder();
		String output_BaseRecalibrator = makeBaseRecalibratorCommand(builder,defaultOptions);
		builder.append("\n");
		
		/*java -Xmx2g -jar -Djava.io.tmpdir=./tmp   ./bin/GenomeAnalysisTK.jar -T PrintReads 
		 * -R ./ref/hg19.fasta -I abnormal2.GR.ir.bam -BQSR abnormal2.grp  
		 * --read_filter MappingQualityZero -o abnormal2.FINAL.bam*/
		String output_PrintReads = makePrintReadsCommand(builder,defaultOptions,output_BaseRecalibrator);
		
		return builder.toString();
	}
	
	private String makePrintReadsCommand(StringBuilder builder, String defaultOptions,String output_BaseRecalibrator) {
		String output_PrintReads = Utils.extractSampleName(this.inputFile,".FINAL.bam");
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "GenomeAnalysisTK.jar");
		builder.append(WHITESPACE);

		builder.append("-T PrintReads");
		builder.append(WHITESPACE);		

		builder.append("-R " + refSeq);
		builder.append(WHITESPACE);				
		
		builder.append("-I " + this.inputFile);
		builder.append(WHITESPACE);	
		
		builder.append("-BQSR " + output_BaseRecalibrator);	//normal
		builder.append(WHITESPACE);

		builder.append("--read_filter MappingQualityZero" );	//abnormal
		builder.append(WHITESPACE);		
		
		builder.append("-o " +output_PrintReads);
		builder.append(WHITESPACE);
		
		return output_PrintReads;
	}
	
	private String makeBaseRecalibratorCommand(StringBuilder builder, String defaultOptions) {
		String output_BaseRecalibrator = Utils.extractSampleName(this.inputFile,".grp");
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "GenomeAnalysisTK.jar");
		builder.append(WHITESPACE);

		builder.append("-T BaseRecalibrator");
		builder.append(WHITESPACE);		

		builder.append("-R " + refSeq);
		builder.append(WHITESPACE);				
		
		builder.append("-knownSites " + indelFile);	//normal
		builder.append(WHITESPACE);

		builder.append("-knownSites " + snpFile);	//abnormal
		builder.append(WHITESPACE);		

		builder.append("-I " + this.inputFile);
		builder.append(WHITESPACE);		
		
		builder.append("-o " +output_BaseRecalibrator);
		builder.append(WHITESPACE);
		
		return output_BaseRecalibrator;
	}
	
}
