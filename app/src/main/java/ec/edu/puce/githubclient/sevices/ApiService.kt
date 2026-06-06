package ec.edu.puce.githubclient.sevices

import ec.edu.puce.githubclient.models.RepoRequest
import ec.edu.puce.githubclient.models.Repository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("user/repos")
    suspend fun getRepositories(): List<Repository>

    @POST("user/repos")
    suspend fun createRepository(
        @Body request: RepoRequest
    ): Response<Unit>
}
