package FunctionTest;

import snu.bike.ngspipeline.Utils;

public class FindLibraryNameTest {

	public static void main(String[] args) {
		String exePath = "/Users/dean/Downloads/sample/";
		String libName = "bamtools.jar";
		System.out.println(Utils.checkLibraryName(exePath, libName));
	}

}
