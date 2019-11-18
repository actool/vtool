package view;

public class ViewConstants {
	public static final String TAU = "tau";

	public static final String typeAutomaticLabel = "?in, !out";
	public static final String typeManualLabel = "define I/O manually";
	public static final String LTS_CONST = "LTS";
	public static final String IOLTS_CONST = "IOLTS";
	public static final String tabIOCO = "IOCO Conformance";
	public static final String tabLang = "Language Based Conformance";
	public static final String tabTSGeneration = "Test Suite Generation";
	public static final String tabTestRun = "Run Test";
	public static final String toolName = "Everest";
	public static final String[] models = new String[] { "", "IOLTS", "LTS" };
	public static final String folderIconPath = "/img/folder.png";
	public static final String titleFrameImgImplementation = "Implementation - ";
	public static final String titleFrameImgSpecification = "Model - ";
	

	// message
	/*public static final String modelWithoutTransition = "Model without transition, if you selected the option "
			+ typeAutomaticLabel + " the transitions must contain such tags(!/?) \n";
	public static final String implementationWithoutTransition = "Model without transition, if you selected the option "
			+ typeAutomaticLabel + " the transitions must contain such tags(!/?) \n";*/

	public static final String msgImp = "Decoration ? and ! are missing in the input files \n";//"The implementation transitions are not labeled with '?' and '!'\n ";
	public static final String msgModel = "Decoration ? and ! are missing in the input files \n";//"The model transitions are not labeled with '?' and '!'\n ";
	public static final String exceptionMessage = "An unexpected error ocurred \n";
	public static final String invalidRegex = " Invalid regex! \n";
	public static final String selectModel = "Select the type model \n";//"Select the kind of model \n";
	public static final String selectImplementation = "Select the IUT model \n";//"The field Implementation is required \n";
	public static final String selectSpecification = "Select the specification model \n";//"The field Model is required \n";
	public static final String selectIoltsLabel = "Choose the I/O partition mode";//"It is necessary how the IOLTS labels will be distinguished \n";
	public static final String selectInpOut = "Define the input and output labels \n";//"The fields Input and Output is required \n";
	public static final String selectIolts = "IOCO relation is defined over IOLTS models \n";//"The informed model must be IOLTS \n";
	public static final String selectIolts_gen = "Test generation is defined over IOLTS models \n";//"The informed model must be IOLTS \n";
	public static final String labelInpOut="There labels that were not set as input/output \n";
	public static final String mInteger="The parameter max IUT states must be integer \n";
}
