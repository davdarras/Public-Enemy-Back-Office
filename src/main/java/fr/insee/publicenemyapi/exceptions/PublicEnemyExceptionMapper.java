package fr.insee.publicenemyapi.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class PublicEnemyExceptionMapper implements ExceptionMapper<PublicEnemyException> {
    public Response toResponse(PublicEnemyException ex) {
        RestMessage message = ex.toRestMessage();
        return Response.status(message.getStatus())
                .entity(message)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
