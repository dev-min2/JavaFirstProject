package CommonUtils;

public class TripleTuple<A,B,C> {
	private A firstObj;
	private B secondObj;
	private C thirdObj;
	
	public TripleTuple(A firstObj, B secondObj, C thirdObj) {
        this.firstObj = firstObj;
        this.secondObj = secondObj;
        this.thirdObj = thirdObj;
    }

    public A getFirstObj() {
        return firstObj;
    }

    public B getSecondObj() {
        return secondObj;
    }

    public C getThirdObj() {
        return thirdObj;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;        
        
        if(obj == null || this.getClass() != obj.getClass())
        	return false;
 
        TripleTuple tuple = (TripleTuple)obj;
        if(!firstObj.equals(tuple.firstObj) || !secondObj.equals(tuple.secondObj) || !thirdObj.equals(tuple.thirdObj))
        	return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        return 31 * (firstObj.hashCode() + secondObj.hashCode() + thirdObj.hashCode());
    }
}
