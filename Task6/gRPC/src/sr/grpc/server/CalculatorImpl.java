package sr.grpc.server;

import io.grpc.Metadata;
import io.grpc.Status;
import sr.grpc.gen.ArithmeticOpResult;
import sr.grpc.gen.CalculatorGrpc.CalculatorImplBase;

public class CalculatorImpl extends CalculatorImplBase 
{
	@Override
	public void add(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<sr.grpc.gen.ArithmeticOpResult> responseObserver) 
	{
		System.out.println("addRequest (" + request.getArg1() + ", " + request.getArg2() +")");
		int val = request.getArg1() + request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		if(request.getArg1() > 100 && request.getArg2() > 100) try { Thread.sleep(5000); } catch(java.lang.InterruptedException ex) { }
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void subtract(sr.grpc.gen.ArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<sr.grpc.gen.ArithmeticOpResult> responseObserver) 
	{
		System.out.println("subtractRequest (" + request.getArg1() + ", " + request.getArg2() +")");

		responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT.withDescription("Bad arguments").asRuntimeException());

		int val = request.getArg1() - request.getArg2();
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(val).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	@Override
	public void multipleN(sr.grpc.gen.ArithmeticNOpArguments request,
			 io.grpc.stub.StreamObserver<sr.grpc.gen.ArithmeticOpResult> responseObserver)
	{
		System.out.println("multipleNRequest (" + request.getArgList() + ")");

		int res = 1;
		for(Integer val : request.getArgList()){
			if((res>0 && val>0 && val*res < 0) || (res>0 && val<0 && val*res>0) || (res<0 && val<0 && val*res<0)){
				responseObserver.onError(Status.CANCELLED.withDescription("Exceeded int min/max size").asException());
			} // dying server isn't the best way, but doesn't matter now - exception given
			res *= val;
		}
		ArithmeticOpResult result = ArithmeticOpResult.newBuilder().setRes(res).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}


}
