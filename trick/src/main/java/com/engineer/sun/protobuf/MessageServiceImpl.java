package com.engineer.sun.protobuf;


import com.google.protobuf.*;

/**
 * User: sunluning
 * Date: 12-9-15 下午12:12
 */
public class MessageServiceImpl implements BlockingService {
    @Override
    public Descriptors.ServiceDescriptor getDescriptorForType() {
        return this.getDescriptorForType();
    }

    @Override
    public Message callBlockingMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request) throws ServiceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Message getRequestPrototype(Descriptors.MethodDescriptor method) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Message getResponsePrototype(Descriptors.MethodDescriptor method) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
