package com.example.vin.petclinicappointment.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vin.petclinicappointment.data.model.PetClinic
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import java.lang.Exception

class PetClinicDataSource(
    private val petClinicRepository: PetClinicRepository,
    private val query: String,
    private val latitude: Double? = null,
    private val longitude: Double? = null
): PagingSource<Int, PetClinic>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PetClinic> {
        return try {
            val nextPage = params.key ?: 1
            val clinicListResponse = petClinicRepository.getPetClinicList(query, latitude, longitude, pageNumber = nextPage)
            val data = clinicListResponse.data?.body()?.data ?: listOf()
            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if(data.isEmpty()) null else nextPage.plus(1)
            )
        }catch (ex: Exception){
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PetClinic>): Int? {
        return state.anchorPosition
    }
}