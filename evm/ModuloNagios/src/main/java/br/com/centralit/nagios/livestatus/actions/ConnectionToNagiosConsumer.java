package br.com.centralit.nagios.livestatus.actions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.com.centralit.nagios.executor.query.ConnectionToNagiosProducer;
import br.com.centralit.nagios.livestatus.query.LS_Query;
import br.com.centralit.nagios.livestatus.tables.EventWithInfo;
import br.com.centralit.nagios.livestatus.tables.Hosts;
import br.com.centralit.nagios.livestatus.tables.Service;
import br.com.centralit.nagios.util.JSONConverter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author adenir
 */
@MessageDriven(name = "connectionToNagiosConsumer", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/CitsmartEVM/nagiosEventRequest"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})

public class ConnectionToNagiosConsumer implements MessageListener {

    private final int socketTimeout = -1;

    ParametersQueryNagios paramatersIncoming;

    private final Map<String, String> options = new HashMap<String, String>() {
    };

    @Inject
    ConnectionToNagiosProducer producer;

    public void setOptions(String key, String value) {
        this.options.put(key, value);
    }

    public ConnectionToNagiosConsumer() {

    }

    @Override
    public void onMessage(Message rcvMessage) {
        //TODO codificar a mensagem
        //String msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                String messageIncoming = ((TextMessage) rcvMessage).getText();
                System.out.println("Modulo Nagios:" + messageIncoming);
                //TODO 
                execute_query_nagios(messageIncoming);
            } else {
                System.out.println("***ERROR*** - Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionToNagiosConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void execute_query_nagios(String messageIncoming) throws Exception {
        ParametersQueryNagios parametersIncoming = new ParametersQueryNagios(messageIncoming);
        if (parametersIncoming.isReplyTreatQuery()) {
            LS_Query ls_query = new LS_Query(parametersIncoming);
            LS_Response ls_response = executeQuery(ls_query, parametersIncoming);
            producer.setMessage(getHostJson(ls_response, parametersIncoming));
            producer.send();
        }
    }

    /**
     * Realiza a conexao e executa a query.
     *
     * @param ls_query
     * @param parametersIncoming
     * @return LS_Response
     * @throws Exception Expected idEvent, socket_type, path_or_host, port,
     * string_query_to_nagios;
     */
    public LS_Response executeQuery(LS_Query ls_query, ParametersQueryNagios parametersIncoming) throws Exception {
        Socket socket = new Socket();

        if (parametersIncoming.getPath_or_host().isEmpty() || parametersIncoming.getPort() == -1) {
            throw new IllegalArgumentException("Server/host or/and port is invalid");
        }

        if (socketTimeout != -1) {
            socket.connect(new InetSocketAddress(parametersIncoming.getPath_or_host(), parametersIncoming.getPort()), socketTimeout);
            socket.setSoTimeout(socketTimeout);
        } else {
            socket.connect(new InetSocketAddress(parametersIncoming.getPath_or_host(), parametersIncoming.getPort()));
        }

        LS_Response response = new LS_Response(ls_query);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String nagios_query = ls_query.to_s(options);
        out.write(nagios_query, 0, nagios_query.length());
        out.flush();
        socket.shutdownOutput();

        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }

            response.add(line);
        }

        return response;
    }

    public String getHostJson(LS_Response response, ParametersQueryNagios parametersIncoming) throws Exception {

        String jjson = response.getColumnsAndValueAsJson(JSONConverter.getObjects(parametersIncoming.getTable()));

        Hosts host = JSONConverter.fromJson(jjson, Hosts.class);

        for (EventWithInfo evt : host.events) {
            parametersIncoming.setTable("services");
            parametersIncoming.setFilters("display_name = " + evt.display_name);

            System.out.println(parametersIncoming.getSocket_type() + " "
                    + parametersIncoming.getPathNagios() + " " 
                    + parametersIncoming.getTable() + " "
                    + parametersIncoming.getColumns() + " "
                    + parametersIncoming.getFilters() + " ");
            
            LS_Query ls_query = new LS_Query(parametersIncoming);
            response = executeQuery(ls_query, parametersIncoming);

            jjson = response.getColumnsAndValueAsJson(JSONConverter.getObjects(parametersIncoming.getTable()));

            Service service = JSONConverter.fromJson(jjson, Service.class);

            host.listOfservices.add(service);

        }

        //jjson =  response.getColumnsAndValueAsJson(JSONConverter.getObjects(parm_query_nagios.table));
        return JSONConverter.toJson(host);
    }

}
