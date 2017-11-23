package snu.bike.wholeExomSeq.mutect;

import snu.bike.ngspipeline.Executor;

public class MutectExecutor extends Executor{
	private String refSeq;
	private String cosmicFile;
	private String snpFile;
	private String inputNormal;
	private String inputTumor;

	public MutectExecutor(String executionPath, String reference_sequence, String cosmicFile, String snpFile,
			String inputNormal, String inputTumor) {
		super();
		this.exePath = executionPath;
		this.refSeq = reference_sequence;
		this.cosmicFile = cosmicFile;
		this.snpFile = snpFile;
		this.inputNormal = inputNormal;
		this.inputTumor = inputTumor;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";

		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		/*/dev1/ksha/java6/jdk1.6.0_33/bin/java -Xmx2g -jar ./bin/muTect-1.1.4.jar
		 *  --analysis_type MuTect --reference_sequence ./ref/hg19.fasta --cosmic ./b37_cosmic_v54_120711.vcf  
		 *  --dbsnp ./dbsnp_138.hg19.vcf  --input_file:normal ./normal2.FINAL.bam --input_file:tumor ./abnormal2.FINAL.bam 
		 *  --out pro_power_call_stats.out --coverage_file pro_power_coverage.wig.txt --vcf pro_power.vcf */
		String defaultOption1 = "/dev1/ksha/java6/jdk1.6.0_33/bin/java -Xmx2g -jar";
		String defaultOption2 = "--out pro_power_call_stats.out --coverage_file pro_power_coverage.wig.txt --vcf pro_power.vcf";
		
		StringBuilder builder = new StringBuilder();
		makMuTectCommand(builder,defaultOption1,defaultOption2);
		
		return builder.toString();
	}
	
	private void makMuTectCommand(StringBuilder builder, String defaultOption1, String defaultOption2) {

		builder.append(defaultOption1);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "muTect.jar");
		builder.append(WHITESPACE);

		builder.append("--analysis_type MuTect");
		builder.append(WHITESPACE);		

		builder.append("--reference_sequence " + refSeq);
		builder.append(WHITESPACE);				
		
		builder.append("--cosmic " + this.cosmicFile);	//cosmic
		builder.append(WHITESPACE);

		builder.append("--dbsnp " + this.snpFile);	//snp
		builder.append(WHITESPACE);		

		builder.append("--input_file:normal " + this.inputNormal);
		builder.append(WHITESPACE);		
		
		builder.append("--input_file:tumor " + this.inputTumor);
		builder.append(WHITESPACE);
		
		builder.append(defaultOption2);
		builder.append(WHITESPACE);
		builder.append("\n");
		
		builder.append("grep -v REJECT pro_power.vcf > mutect_result.vcf");
		builder.append("\n");
		builder.append("grep -v REJECT pro_power_call_stats.out > mutect_result.out");
	}

}
