package com.example.Project1B;

import com.example.Project1B.Server.MessageDTO;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.stream.Collectors;

@Path("/chat-history")
public class ChatHistoryController {

    @EJB
    DataBaseProviderBean dataBaseProviderBean; //wstrzykniecie obiektu

    @GET
    @Path("/from/{from}/to/{to}")
    @Produces("text/plain")//oznacza to tyle co generuje dostaniemy w tym przypadku zwykły tekst
    public String getPrivateHistory(@PathParam("from") String from,
                                    @PathParam("to") String to) {
        return dataBaseProviderBean.getChatLogDAO()
                .getPrivateMessages(from, to).stream()
                .map(MessageDTO::toString)
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/group/{group}")
    @Produces("text/plain")//oznacza to tyle co generuje dostaniemy w tym przypadku zwykły tekst
    public String getGroupHistory(@PathParam("group") String group) {
        return dataBaseProviderBean.getChatLogDAO()
                .getGroupMessages(group).stream()
                .map(MessageDTO::toString)
                .collect(Collectors.joining("\n"));
    }
}