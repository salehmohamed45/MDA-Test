package com.example.mda.data.repository

// Repository for data operations

import android.util.Log
import com.example.mda.data.local.dao.ActorDao
import com.example.mda.data.local.entities.ActorEntity
import com.example.mda.data.remote.api.TmdbApi
import com.example.mda.data.remote.model.Actor
import com.example.mda.data.remote.model.ActorFullDetails
import com.example.mda.data.remote.model.ActorResponse
import com.example.mda.data.remote.model.KnownFor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.collections.emptyList

class ActorsRepository(
    private val api: TmdbApi,
    private val actorDao: ActorDao? = null
) {

    suspend fun getPopularActors(page: Int = 1): Response<ActorResponse> =
        withContext(Dispatchers.IO) {
            val response = api.getPopularPeople(page = page)
            response
        }

    
    suspend fun getPopularActorsWithCache(page: Int = 1): List<ActorEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getPopularPeople(page = page)

                if (response.isSuccessful) {
                    val actors = response.body()?.results ?: emptyList()
                    actors.forEach {
                    }

                    cacheActors(actors)
                    actors.map { actor ->
                        ActorEntity(
                            id = actor.id,
                            name = actor.name,
                            profilePath = actor.profilePath,
                            biography = actor.biography,
                            birthday = actor.birthday,
                            placeOfBirth = actor.placeOfBirth,
                            knownForDepartment = actor.knownForDepartment,
                            knownFor = Gson().toJson(actor.knownFor ?: emptyList<KnownFor>())
                        )
                    }
                } else {
                    actorDao?.getAllActors()
                        ?.also {
                        }
                        ?: emptyList()
                }
            } catch (e: Exception) {
                actorDao?.getAllActors()
                    ?.also {
                    }
                    ?: emptyList()
            }
        }

    
    suspend fun cacheActors(actors: List<Actor>) = withContext(Dispatchers.IO) {
        if (actorDao == null) {
            return@withContext
        }
        actors.forEach { actor ->
            val gson = Gson()
            val knownForJson = gson.toJson(actor.knownFor)

            val entity = ActorEntity(
                id = actor.id,
                name = actor.name,
                profilePath = actor.profilePath,
                biography = actor.biography,
                birthday = actor.birthday,
                placeOfBirth = actor.placeOfBirth,
                knownForDepartment = actor.knownForDepartment,
                knownFor = knownForJson
            )
            actorDao.upsert(entity)
        }
    }

    
    suspend fun getCachedActors(): List<ActorEntity> = withContext(Dispatchers.IO) {
        val cached = actorDao?.getAllActors() ?: emptyList()
        cached
    }

    
    suspend fun getFullActorDetails(
        personId: Int,
        forceRefresh: Boolean = false
    ): ActorFullDetails? =
        withContext(Dispatchers.IO) {
            Log.d(
                "ActorsRepo",
                "getFullActorDetails called, personId=$personId, forceRefresh=$forceRefresh"
            )
            try {
                val response = api.getActorDetails(
                    personId = personId,
                    appendToResponse = "images,combined_credits,external_ids"
                )
                Log.d("ActorsRepo", "API call completed, success=${response.isSuccessful}")

                if (response.isSuccessful) {
                    val details = response.body()
                    Log.d("ActorsRepo", "Fetched details for actor: ${details?.name}")
                    details?.let {
                        actorDao?.upsert(
                            ActorEntity(
                                id = it.id,
                                name = it.name ?: "",
                                profilePath = it.profile_path,
                                biography = it.biography,
                                birthday = it.birthday,
                                placeOfBirth = it.place_of_birth,
                                knownForDepartment = null,
                                knownFor = null
                            )
                        )
                        Log.d("ActorsRepo", "Actor details cached")
                    }
                    details
                } else {
                    Log.d("ActorsRepo", "API error, using cache")
                    actorDao?.getDetails(personId)?.let { cached ->
                        Log.d("ActorsRepo", "Fetched details from cache for actor: ${cached.name}")
                        ActorFullDetails(
                            id = cached.id,
                            name = cached.name,
                            biography = cached.biography,
                            birthday = cached.birthday,
                            profile_path = cached.profilePath,
                            place_of_birth = cached.placeOfBirth,
                            combined_credits = null,
                            external_ids = null,
                            gender = null,
                            homepage = null,
                            images = null
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d(
                    "ActorsRepo",
                    "Exception in getFullActorDetails: ${e.localizedMessage}, using cache"
                )
                actorDao?.getDetails(personId)?.let { cached ->
                    Log.d("ActorsRepo", "Fetched details from cache for actor: ${cached.name}")
                    return@withContext ActorFullDetails(
                        id = cached.id,
                        name = cached.name,
                        biography = cached.biography,
                        birthday = cached.birthday,
                        profile_path = cached.profilePath,
                        place_of_birth = cached.placeOfBirth,
                        combined_credits = null,
                        external_ids = null,
                        gender = null,
                        homepage = null,
                        images = null
                    )
                }
                null
            }
        }
}
