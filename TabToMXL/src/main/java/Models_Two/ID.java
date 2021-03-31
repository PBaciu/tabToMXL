package Models_Two;

public class ID {
	public String value;
    
	public ID() {
	}
	
    public ID(String value) {
    	super();
        this.value = value;
    }


	@Override
    public String toString() {
        return "ID{" +
        		"value=" + value +
                '}';
    }

}
