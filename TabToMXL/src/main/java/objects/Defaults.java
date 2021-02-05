package objects;

public class Defaults {
	private double millimeters = 6.99911;	//maybe put this in an object called scaling?
	private int tenths = 40;
	
	private double pageHeight = 1596.77;
	private double pageWidth = 1233.87;
	
	private String pageMargin[] = {"even", "odd"};
	private double leftMargin = 85.7252;
	private double rightMargin = 85.7252;
	private double topMargin = 85.7252;
	private double bottomMargin = 85.7252;
	
	private String fontFamily = "Edwin";
	private int fontSize = 10;
	
	/*
	 * Do I really need this class? 
	 * We can just write the default directly into the file and just keep the page layout as portrait
	 */
}
