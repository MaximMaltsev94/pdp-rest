package pdp.pdprest.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pdp.pdprest.domain.TaskEntity;
import pdp.pdprest.service.TaskService;

import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/pdp-task")
public class TaskResource {

    private TaskService taskService = TaskService.getInstance();

    ObjectMapper objectMapper = new ObjectMapper();

    //      We use query parameters to filter resources
    @GET
    @Produces(APPLICATION_JSON)
    public Response getAll(@QueryParam("firstName") String firstName,
                           @QueryParam("lastName") String lastName,
                           @QueryParam("sort")
                           @Pattern(regexp = "^(firstName|lastName):(asc|desc)$") String sort) {

        return Response.ok(taskService.search(firstName, lastName, sort)).build();
    }



    //      We use Path parameters for unique identification of resource
    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response getAll(@PathParam("id") Long id) {
        return Response.ok(taskService.get(id)).build();
    }

    //      We use POST request to create sub-resource, but we don't know exact URI of resource
    //      e.g. POST /pdp-task/ will insert new resource
    @POST
    @Consumes(APPLICATION_JSON)
    public Response create(TaskEntity taskEntity) throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(taskEntity);
        System.out.println("create - " + s);
        if(taskEntity.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("id not allowed").build();
        }
        long insertedId = taskService.add(taskEntity);
        return Response.ok("inserted id - " + insertedId).build();
    }


    //      We use PUT request in two cases: for create or save resource
    //      1. for brand-new URI we create new resource
    //         e.g PUT /pdp-task/666 will create new resource if resource with id = 666 not exists.
    //      2. for existing URI PUT operation will replace resource
    //         e.g PUT /pdp-task/1 will override existing resource
    @PUT
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, TaskEntity taskEntity) throws JsonProcessingException {
        taskEntity.setId(id);
        taskService.save(taskEntity);
        return Response.ok().build();
    }


    @DELETE
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        taskService.delete(id);
        return Response.ok().build();
    }

}
