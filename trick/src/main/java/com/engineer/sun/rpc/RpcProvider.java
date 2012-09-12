package com.engineer.sun.rpc;


/**
* RpcProvider
*
* @author william.liangf
*/
public class RpcProvider {

	public static void main(String[] args) throws Exception {
		HelloService service = new HelloServiceImpl();
		RpcFramework.export(service, 1234);
	}

}
