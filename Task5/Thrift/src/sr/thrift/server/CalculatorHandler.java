package sr.thrift.server;

import org.apache.thrift.TException;
import sr.rpc.thrift.Calculator;

public class CalculatorHandler implements Calculator.Iface {

	int id;

	public CalculatorHandler(int id) {
		this.id = id;
	}

	@Override
	public int add(int n1, int n2) {
		System.out.println("CalcHandler#" + id + " add(" + n1 + "," + n2 + ")");
		if(n1 > 1000 || n2 > 1000) { 
			try { Thread.sleep(6000); } catch(java.lang.InterruptedException ex) { }
			System.out.println("DONE");
		}
		return n1 + n2;
	}

	@Override
	public int subtract(int n1, int n2) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int multiple(int n1, int n2) throws TException {
		System.out.println("CalcHandler#" + id + " multiple(" + n1 + "," + n2 + ")");

		return n1 * n2;
	}

}

