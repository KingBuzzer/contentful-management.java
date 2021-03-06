package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMARole;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Service class to define the REST interface to Contentful.
 */
public interface ServiceRoles {
  @GET("/spaces/{spaceId}/roles/")
  Observable<CMAArray<CMARole>> fetchAll(@Path("spaceId") String spaceId);

  @GET("/spaces/{spaceId}/roles/{roleId}")
  Observable<CMARole> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId
  );

  @POST("/spaces/{spaceId}/roles/")
  Observable<CMARole> create(
      @Path("spaceId") String spaceId,
      @Body CMARole role
  );

  @PUT("/spaces/{spaceId}/roles/{roleId}")
  Observable<CMARole> update(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId,
      @Body CMARole role,
      @Header("X-Contentful-Version") Integer version
  );

  @DELETE("/spaces/{spaceId}/roles/{roleId}")
  Observable<Response<Void>> delete(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId
  );
}
