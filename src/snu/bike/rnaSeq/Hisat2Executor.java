package snu.bike.rnaSeq;

import snu.bike.ngspipeline.Executor;
import snu.bike.ngspipeline.Utils;

public class Hisat2Executor extends Executor{
	private final String OUTPUT_HISAT2 = ".sam";
	private int process;
	private String indexPath;
	private String pair1;
	private String pair2;
	
	public Hisat2Executor(String excutionPath, int process, String inputPair1, String inputPair2, String indexPath) {
		super();
//		System.out.println("################"+this.getClass().getSimpleName());	//to get a classname.
		this.exePath = excutionPath; 
		this.process = process;
		this.pair1 = inputPair1;
		this.pair2 = inputPair2;

		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.indexPath = indexPath;
		
		System.out.println(pair1 + ", " + pair2);
		this.outputFile = Utils.extractSampleName(Utils.getSampleName(pair1),OUTPUT_HISAT2);
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		/**
		 * ./bin/hisat2 -p 4 --dta -x ./chrX_data/indexes/chrX_tran 
		 * -1 ./chrX_data/samples/ERR188044_chrX_1.fastq.gz 
		 * -2 ./chrX_data/samples/ERR188044_chrX_2.fastq.gz 
		 * -S ERR188044_chrX.sam 
		 * */
		
		String defaultOptions = "--dta -x";
		StringBuilder builder = new StringBuilder();
		builder.append(exePath + "hisat2");
		builder.append(WHITESPACE);
		
		builder.append("-p");
		builder.append(WHITESPACE);
		builder.append(process);
		builder.append(WHITESPACE);
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(indexPath + "chrX_tran");
		builder.append(WHITESPACE);

		builder.append("-1");
		builder.append(WHITESPACE);
		builder.append(pair1);
		builder.append(WHITESPACE);
		
		builder.append("-2");
		builder.append(WHITESPACE);		
		builder.append(pair2);
		builder.append(WHITESPACE);

		builder.append("-S");
		builder.append(WHITESPACE);
		builder.append(outputFile);
		builder.append(WHITESPACE);		
		
		return builder.toString();		
	}

}
