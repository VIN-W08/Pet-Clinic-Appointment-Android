package com.example.vin.petclinicappointment.di

import android.content.Context
import com.example.vin.petclinicappointment.data.UserDataStore
import com.example.vin.petclinicappointment.data.repository.AppointmentRepository
import com.example.vin.petclinicappointment.data.repository.LocationRepository
import com.example.vin.petclinicappointment.data.repository.PetClinicRepository
import com.example.vin.petclinicappointment.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppointmentRepository(): AppointmentRepository
            = AppointmentRepository()

    @Singleton
    @Provides
    fun providePetClinicRepository(): PetClinicRepository
            = PetClinicRepository()

    @Singleton
    @Provides
    fun provideLocationRepository(): LocationRepository
        = LocationRepository()

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataStore: UserDataStore
    ): UserRepository
            = UserRepository(userDataStore)

    @Singleton
    @Provides
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore =
        UserDataStore(context)
}