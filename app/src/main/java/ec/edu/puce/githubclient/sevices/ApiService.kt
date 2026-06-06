package ec.edu.puce.githubclient.sevices

import ec.edu.puce.githubclient.models.RepoRequest
import ec.edu.puce.githubclient.models.Repository
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("user/repos")
    suspend fun getRepositories(): List<Repository>

    @POST("user/repos")
    suspend fun createRepository(
        @Body request: RepoRequest
    ): Response<Unit>

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body request: RepoRequest
    ): Response<Repository>

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Unit>
}
