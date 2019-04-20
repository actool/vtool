package view;

public class ViewConstants {
	public static final String TAU = "tau";

	public static final String typeAutomaticLabel = "?in, !out";
	public static final String typeManualLabel = "define I/O manually";
	public static final String LTS_CONST = "LTS";
	public static final String IOLTS_CONST = "IOLTS";
	public static final String tabIOCO = "IOCO Conformance";
	public static final String tabLang = "Language Based Conformance";
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

	public static final String msgImp = "The implementation transitions are not labeled with '?' and '!'\n ";
	public static final String msgModel = "The model transitions are not labeled with '?' and '!'\n ";
	public static final String exceptionMessage = "An unexpected error ocurred \n";
	public static final String invalidRegex = " Invalid regex! \n";
	public static final String selectModel = "Select the kind of model \n";
	public static final String selectImplementation = "The field Implementation is required \n";
	public static final String selectSpecification = "The field Model is required \n";
	public static final String selectIoltsLabel = "It is necessary how the IOLTS labels will be distinguished \n";
	public static final String selectInpOut = "The fields Input and Output is required \n";
	public static final String selectIolts = "The informed model must be IOLTS \n";

}
