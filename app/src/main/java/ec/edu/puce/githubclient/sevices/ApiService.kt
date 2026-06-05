package ec.edu.puce.githubclient.sevices

import ec.edu.puce.githubclient.models.Repository
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(value ="/user/repos")
    suspend fun getRepositories (
        @Query(value = "sort") created: String = "created",
        @Query(value = "direction") direction: String = "desc",
        @Query(value = "afiliation") afiliation: String = "owner", // Escrito exactamente con una 'f' como en el diff
        @Query(value = "per_page") perPage: Int = 100,
        @Query(value = "t") t: String = "${System.currentTimeMillis()}",
    ) : List<Repository>
}