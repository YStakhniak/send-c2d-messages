package com.mycompany.app;

import com.microsoft.azure.sdk.iot.service.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class App {
    private static final String connectionString = "HostName=vksbp-iothub-01-dev.azure-devices.net;DeviceId=MyJavaDevice;SharedAccessKey=Nq5UbH1MMojpSdghC8QtwPflcdQ6pMWT8hGB9+G5VSo=";
    private static final String deviceId = "MyJavaDevice";
    private static final IotHubServiceClientProtocol protocol =
            IotHubServiceClientProtocol.AMQPS;
    public static void main(String[] args) throws IOException,
            URISyntaxException, Exception {
        ServiceClient serviceClient = ServiceClient.createFromConnectionString(
                connectionString, protocol);

        if (serviceClient != null) {
            serviceClient.open();
            FeedbackReceiver feedbackReceiver = serviceClient
                    .getFeedbackReceiver();
            if (feedbackReceiver != null) feedbackReceiver.open();

            Message messageToSend = new Message("Cloud to device message.");
            messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);

            serviceClient.send(deviceId, messageToSend);
            System.out.println("Message sent to device");

            FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
            if (feedbackBatch != null) {
                System.out.println("Message feedback received, feedback time: "
                        + feedbackBatch.getEnqueuedTimeUtc().toString());
            }

            if (feedbackReceiver != null) feedbackReceiver.close();
            serviceClient.close();
        }
    }
}
