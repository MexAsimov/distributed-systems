package sr.ice.server;
// **********************************************************************
//
// Copyright (c) 2003-2019 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import com.zeroc.Ice.Current;

import Demo.A;
import Demo.Calc;

public class CalcI implements Calc
{
	private static final long serialVersionUID = -2448962912780867770L;
	long counter = 0;

	@Override
	public long add(int a, int b, Current __current) 
	{
		System.out.println(__current.id);
		System.out.println("ADD: a = " + a + ", b = " + b + ", result = " + (a+b));
		
		if(a > 1000 || b > 1000) {
			try { Thread.sleep(6000); } catch(java.lang.InterruptedException ex) { } 
		}
		
		if(__current.ctx.values().size() > 0) System.out.println("There are some properties in the context");
		
		return a + b;
	}
	
	@Override
	public long subtract(int a, int b, Current __current) {
		System.out.println(__current.id);
		System.out.println("SUBTRACT: a = " + a + ", b = " + b + ", result = " + (a-b));

		if(a > 1000 || b > 1000) {
			try { Thread.sleep(6000); } catch(java.lang.InterruptedException ex) { }
		}

		if(__current.ctx.values().size() > 0) System.out.println("There are some properties in the context");

		return a - b;
	}

	@Override
	public float avg(long[] seq, Current __current){
		System.out.println(__current.id);

		System.out.print("AVG:");
		long sum = 0;
		for(long el : seq){
			System.out.print(" "+el);
			sum += el;
		}
		System.out.println(", result = " + sum/seq.length);

		if(__current.ctx.values().size() > 0) System.out.println("There are some properties in the context");

		return (float)sum/seq.length;
	}


	@Override
	public /*synchronized*/ void op(A a1, short b1, Current current) {
		System.out.println("OP #" + (++counter));
		try { Thread.sleep(500); } catch(java.lang.InterruptedException ex) { }		
	}
}
