package com.filangladminlpuveggi.lpuveggii.daggerInjection


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() =  FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun  provideFirebaseFirestore() =  Firebase.firestore

//    @Provides
//    @Singleton
//    fun  provideFirebaseCommon(
//        firebaseAuth: FirebaseAuth,
//        firestore: FirebaseFirestore
//    ) = FirebaseCommon(firestore , firebaseAuth)

}