package snu.bike.ngspipeline;

public class SamplePair {
	private String normalSample;
	private String tumorSample;
	private String dir;
	
	public SamplePair()	{
		normalSample = "";
		tumorSample = "";
		dir = "./";
	}

	public SamplePair(String normalSample, String tumorSample, String dir) {
		super();
		this.normalSample = normalSample;
		this.tumorSample = tumorSample;
		this.dir = dir;
		
		if(dir.charAt(dir.length()-1) != '/')	{	// path를 처리하기위한 '/'값 입력.
			this.dir += '/';
		}
		
		this.normalSample = this.normalSample.trim();
		this.tumorSample = this.tumorSample.trim();
		this.dir = this.dir.trim();
	}
	
	public String getNormalSample() {
		return dir+normalSample;	//return contains input directory.
	}

	public String getTumorSample() {
		return dir+tumorSample;		//return contains input directory.
	}

	public String getPairedend1() {
		return dir+normalSample;	//return contains input directory.
	}

	public String getPairedend2() {
		return dir+tumorSample;		//return contains input directory.
	}
	
	@Override
	public String toString() {
		return "SamplePair [normalSample=" + normalSample + ", tumorSample=" + tumorSample + ", dir=" + dir + "]";
	}	
}